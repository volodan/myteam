package com.ness.myteam.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ness.myteam.domain.JobRole;
import com.ness.myteam.domain.RoleRate;
import com.ness.myteam.dto.JobRoleDTO;
import com.ness.myteam.repository.JobRoleRepository;
import com.ness.myteam.repository.RoleRateRepository;
import com.ness.myteam.util.ValidationUtils;

@Controller
@RequestMapping("/api/jobrole")
public class JobRoleController {

	private static final Logger LOGGER = Logger.getLogger(JobRoleController.class);
	
	private static final String DATE_OUT_FORMAT = "dd.MM.yyyy";
	
	private static final String CSV_DELIMITER = ",";
	
	private static final SimpleDateFormat DATE_OUT_FORMATER = new SimpleDateFormat(DATE_OUT_FORMAT);

	@Autowired
	private JobRoleRepository jobRoleRepository;
	
	@Autowired
	private RoleRateRepository roleRateRepository;
	
	@Autowired
	private ValidationUtils validationUtils;
	
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public ResponseEntity<String> handleFileUpload(
    		@RequestParam("file") MultipartFile file) {

    	Reader r = null;
    	BufferedReader br = null;
    	int counter = 0;
    	
    	try {
			r = new InputStreamReader(file.getInputStream());
			br = new BufferedReader(r);
			
			String line = null;

			while((line = br.readLine() ) != null) {
				String[] vals = line.split(CSV_DELIMITER);
				if (vals.length != 5) {
					LOGGER.warn("Unable to import project with values: " + line);
					continue;
				}
				
				JobRole jobRole = new JobRole();
				jobRole.setName(vals[0].trim());
				
				LOGGER.debug("Processing Job Role : " + jobRole.getName());
				// check if such projec doesn't already exist
				if (jobRoleRepository.findByName(jobRole.getName()) != null) { 
					LOGGER.warn("Job Role already exists: " + jobRole.getName());
					continue;
				}
				
				jobRoleRepository.save(jobRole);
				
				// create rate for role firstly
				RoleRate rate = new RoleRate();
				rate.setCurrency(vals[1].trim());
				rate.setValue(Double.parseDouble(vals[2].trim()));
				rate.setValidFrom(DATE_OUT_FORMATER.parse(vals[3].trim()));
				rate.setValidTo(DATE_OUT_FORMATER.parse(vals[4].trim()));
				rate.setJobRole(jobRole);
				roleRateRepository.save(rate);
				
				counter++;
			}
			
		} catch (Exception e) {
			LOGGER.warn("Unable to process file: " + file.getOriginalFilename(), e);
			return new ResponseEntity<String>("Unable to process file: " + file.getOriginalFilename(), HttpStatus.INTERNAL_SERVER_ERROR) ;
		} finally {
			if (br != null) { try { br.close(); } catch (IOException iEx) { LOGGER.warn("Error closing buf. reader for: " + file.getOriginalFilename(), iEx);} }
			if (r != null) { try { r.close(); } catch (IOException iEx) { LOGGER.warn("Error closing reader for: " + file.getOriginalFilename(), iEx);} }
		}

		if (counter > 0) {
			LOGGER.info("Number of job role created: " + counter);
		}
		
        return new ResponseEntity<String>("Done", HttpStatus.OK);
	}	
    
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<JobRoleDTO>> listJobRoles() {
    	List<JobRoleDTO> jobRoles = new ArrayList<>();
    	for (JobRole jr : jobRoleRepository.findAll()) {
    		jobRoles.add(assemble(jr));
    	}
        return new ResponseEntity<List<JobRoleDTO>>(jobRoles, HttpStatus.OK);
    }
	
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> createProject(@RequestBody JobRoleDTO jobRoleDTO) {
    	try {
    		validationUtils.validateNotEmpty("name", jobRoleDTO.getName());
    		validationUtils.validateNotEmpty("currency", jobRoleDTO.getRateCurrency());
    		validationUtils.validateNotEmpty("value", jobRoleDTO.getRateValue());
    		validationUtils.validateNotEmpty("validFrom", jobRoleDTO.getValidFrom());
    		validationUtils.validateNotEmpty("validTo", jobRoleDTO.getValidTo());
    		
        	JobRole tempJobRole = jobRoleRepository.findByName(jobRoleDTO.getName());
        	if (tempJobRole != null) {
    			LOGGER.warn("JobRole already exists: " + jobRoleDTO.getName());
    	        return new ResponseEntity<String>("JobRole already exist", HttpStatus.PRECONDITION_FAILED);
        	}
    		JobRole jobRole = new JobRole();
    		jobRole.setName(jobRoleDTO.getName().trim());
    		jobRoleRepository.save(jobRole);
    		
    		// create rate for role firstly
    		RoleRate rate = new RoleRate();
    		rate.setCurrency(jobRoleDTO.getRateCurrency().trim());
    		rate.setValue(jobRoleDTO.getRateValue());
    		rate.setValidFrom(DATE_OUT_FORMATER.parse(jobRoleDTO.getValidFrom().trim()));
    		rate.setValidTo(DATE_OUT_FORMATER.parse(jobRoleDTO.getValidTo()));
    		rate.setJobRole(jobRole);
    		roleRateRepository.save(rate);	 
    		
		} catch (Exception e) {
			LOGGER.error("Unable to save job role", e);
	        return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
        return new ResponseEntity<String>("JobRole Created", HttpStatus.CREATED);
    }
    
  
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<String> updateProject(@RequestBody JobRoleDTO jrDTO) {
    	if (jrDTO.getId() == null) {
			LOGGER.info("No ID passed for project to be updated!");
	        return new ResponseEntity<String>("No ID present", HttpStatus.PRECONDITION_FAILED);
    	}
    	
    	JobRole tempJR = jobRoleRepository.findOne(jrDTO.getId());
    	if (tempJR == null) {
			LOGGER.info("No Job Role found for given ID!" + jrDTO.getId());
	        return new ResponseEntity<String>("No Job role present", HttpStatus.PRECONDITION_FAILED);
    	}
    	
    	if (!StringUtils.isEmpty(jrDTO.getName()) && !jrDTO.getName().equals(tempJR.getName())) {
    		LOGGER.debug(String.format("Name is going to change from {} to {}", tempJR.getName(), jrDTO.getName()));
    		tempJR.setName(jrDTO.getName());
    	}
 
    	return new ResponseEntity<String>("Job Role Updated", HttpStatus.OK);
    }
    
    @RequestMapping(value = "/addRate", method = RequestMethod.POST)
    public ResponseEntity<String> addRate(@RequestBody JobRoleDTO jrDTO) {
    	try {
    		validationUtils.validateNotEmpty("id", jrDTO.getId());
    		validationUtils.validateNotEmpty("currency", jrDTO.getRateCurrency());
    		validationUtils.validateNotEmpty("value", jrDTO.getRateValue());
    		validationUtils.validateNotEmpty("validFrom", jrDTO.getValidFrom());
    		validationUtils.validateNotEmpty("validTo", jrDTO.getValidTo());
    		
        	JobRole tempJR = jobRoleRepository.findOne(jrDTO.getId());
        	if (tempJR == null) {
    			LOGGER.info("No Job Role found for given ID!" + jrDTO.getId());
    	        return new ResponseEntity<String>("No Job role present", HttpStatus.PRECONDITION_FAILED);
        	}
        	
    		// create rate for role firstly
    		RoleRate rate = new RoleRate();
    		rate.setCurrency(jrDTO.getRateCurrency().trim());
    		rate.setValue(jrDTO.getRateValue());
    		rate.setValidFrom(DATE_OUT_FORMATER.parse(jrDTO.getValidFrom().trim()));
    		rate.setValidTo(DATE_OUT_FORMATER.parse(jrDTO.getValidTo()));
    		rate.setJobRole(tempJR);
    		roleRateRepository.save(rate);	 
    		
		} catch (Exception e) {
			LOGGER.error("Unable to save job role with new rate", e);
	        return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	return new ResponseEntity<String>("New rate for job role created", HttpStatus.OK);
     }
    
    private JobRoleDTO assemble(JobRole jr) {
    	JobRoleDTO jrDTO = new JobRoleDTO();
    	jrDTO.setName(jr.getName());
    	jrDTO.setId(jr.getId());
    	
    	List<RoleRate> roleRates = roleRateRepository.findActualRoleRateByJobRole(jr, new Date());
    	
    	// should be only one... but anyway, take 1st one
    	RoleRate rr = roleRates.size() > 0 ? roleRates.get(0) : null;
    	if (rr != null) {
    		jrDTO.setRateCurrency(rr.getCurrency());
    		jrDTO.setRateValue(rr.getValue());
    		jrDTO.setValidFrom(DATE_OUT_FORMATER.format(rr.getValidFrom()));
    		jrDTO.setValidTo(DATE_OUT_FORMATER.format(rr.getValidTo()));
    	}

    	return jrDTO;
    }
    
}
