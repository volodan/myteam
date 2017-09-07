package com.ness.myteam.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ness.myteam.domain.Billability;
import com.ness.myteam.domain.JobRole;
import com.ness.myteam.domain.Position;
import com.ness.myteam.domain.PositionJobRole;
import com.ness.myteam.domain.PositionProject;
import com.ness.myteam.domain.Project;
import com.ness.myteam.domain.RoleRate;
import com.ness.myteam.domain.Salary;
import com.ness.myteam.repository.BillabilityRepository;
import com.ness.myteam.repository.JobRoleRepository;
import com.ness.myteam.repository.PositionJobRoleRepository;
import com.ness.myteam.repository.PositionProjectRepository;
import com.ness.myteam.repository.PositionRepository;
import com.ness.myteam.repository.ProjectRepository;
import com.ness.myteam.repository.SalaryRepository;

@Controller
@RequestMapping("/api/position")
public class PositionController {

	private static final Logger LOGGER = Logger.getLogger(PositionController.class);
	
	private static final String DATE_OUT_FORMAT = "dd.MM.yyyy";
	
	private static final String CSV_DELIMITER = ",";
	
	private static final SimpleDateFormat DATE_OUT_FORMATER = new SimpleDateFormat(DATE_OUT_FORMAT);

	@Autowired
	private JobRoleRepository jobRoleRepository;
	
	@Autowired
	private PositionRepository positionRepository;
	
	@Autowired
	private BillabilityRepository billabilityRepository;

	@Autowired
	private SalaryRepository salaryRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private PositionProjectRepository positionProjectRepository;
	
	@Autowired
	private PositionJobRoleRepository positionJobRoleRepository;
	
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
				if (vals.length != 11) {
					LOGGER.warn("Unable to import position with values: " + line);
					continue;
				}
				
				Position position = new Position();
				position.setEmail(vals[0].trim());
				position.setFirstname(vals[1].trim());
				position.setSurname(vals[2].trim());
				position.setJoinDate(DATE_OUT_FORMATER.parse(vals[5].trim()));
				position.setProposedJoinDate(DATE_OUT_FORMATER.parse(vals[5].trim()));
				position.setContractor(Boolean.parseBoolean(vals[10].trim()));
				
				LOGGER.debug("Processing Position (employee) : " + position.getEmail());
				// check if such projec doesn't already exist
				if (positionRepository.findByEmail(position.getEmail()) != null) { 
					LOGGER.warn("Position (employee) already exists: " + position.getEmail());
					continue;
				}
				positionRepository.save(position);
				
				// now add salary
				Salary salary = new Salary();
				salary.setApproved(true); // true since importing existing employees
				salary.setCurrency(vals[3].trim());
				salary.setValue(Double.parseDouble(vals[4].trim()));
				salary.setValidFrom(new Date()); // actual date so far - then change if needed
				salary.setPosition(position);
				salaryRepository.save(salary);
				
				//... add also billability
				Billability bill = new Billability();
				bill.setBillable(Boolean.parseBoolean(vals[6].trim()));
				bill.setPercentage(Double.parseDouble(vals[7].trim()));
				bill.setValidFrom(DATE_OUT_FORMATER.parse(vals[5].trim())); // by default from join date
				bill.setPosition(position);
				billabilityRepository.save(bill);
				
				// assign to project - can be done now - it is not critical part
				Project project = projectRepository.findByName(vals[9].trim());	
				if (project != null) { 
					LOGGER.info("adding position: " + position.getEmail() + " to project " + project.getName());
					PositionProject posProj = new PositionProject();
					posProj.setPosition(position);
					posProj.setProject(project);
					posProj.setValidFrom(new Date()); // from today - record creation date
					positionProjectRepository.save(posProj);
				} else { 
					LOGGER.warn("Project: " + vals[7].trim() + " not found!");
				}
				
				JobRole jobRole = jobRoleRepository.findByName(vals[8].trim());
				if (jobRole !=null) {
					LOGGER.info("adding position " + position.getEmail() + " to job role " + jobRole.getName());
					PositionJobRole posJobRole = new PositionJobRole();
					posJobRole.setJobRole(jobRole);
					posJobRole.setPosition(position);
					posJobRole.setValidFrom(new Date()); // from today - record creation date
					positionJobRoleRepository.save(posJobRole);
				} else {
					LOGGER.warn("Job Role: " + vals[8].trim() + " not found!");
				}
				
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
			LOGGER.info("Number of positions created: " + counter);
		}
		
        return new ResponseEntity<String>("Done", HttpStatus.OK);
	}	


}
