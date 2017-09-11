package com.ness.myteam.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.ness.myteam.dto.PositionBaseDTO;
import com.ness.myteam.dto.ProjectDTO;
import com.ness.myteam.repository.BillabilityRepository;
import com.ness.myteam.repository.JobRoleRepository;
import com.ness.myteam.repository.PositionJobRoleRepository;
import com.ness.myteam.repository.PositionProjectRepository;
import com.ness.myteam.repository.PositionRepository;
import com.ness.myteam.repository.ProjectRepository;
import com.ness.myteam.repository.RoleRateRepository;
import com.ness.myteam.repository.SalaryRepository;

@Controller
@RequestMapping("/api/position")
public class PositionController {

	private static final Logger LOGGER = Logger.getLogger(PositionController.class);
	
	private static final String DATE_OUT_FORMAT = "dd.MM.yyyy";
	
	private static final String CSV_DELIMITER = ",";
	
	private static final SimpleDateFormat DATE_OUT_FORMATER = new SimpleDateFormat(DATE_OUT_FORMAT);
	
	private static final Double OVERHEAD_EMPLOYEE = 590.0;
	
	private static final Double OVERHEAD_CONTRACTOR = 300.0;
	
	private static final Double SUPER_BRUTTO_COEF = 1.35;

	@Autowired
	private JobRoleRepository jobRoleRepository;
	
	@Autowired
	private RoleRateRepository roleRateRepository;
	
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

    @RequestMapping(value = "/all/{month}/{year}", method = RequestMethod.GET)
    public ResponseEntity<List<PositionBaseDTO>> listPositions(
    		@PathVariable(required=true) Integer month, @PathVariable(required=true) Integer year) {
    	
	    	Calendar startMonth = new GregorianCalendar();
	    	startMonth.set(year, month - 1, 1, 0, 0, 1);
	
	    	// has to do manually - cause end of month is dynamic
	    	Calendar endMonth = new GregorianCalendar();
	    	endMonth.set(Calendar.YEAR, year);
	    	endMonth.set(Calendar.MONTH, month - 1);
	    	endMonth.set(Calendar.DAY_OF_MONTH, startMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
	    	endMonth.set(Calendar.HOUR_OF_DAY, 23);
	    	endMonth.set(Calendar.MINUTE, 59);
	    	endMonth.set(Calendar.SECOND, 59);
	
	    	LOGGER.debug("Loading employees/position data for period betweed " + 
	    			DATE_OUT_FORMATER.format(startMonth.getTime()) + " and " + DATE_OUT_FORMATER.format(endMonth.getTime()));
	    	
	    	List<Position> positionsInMonth = positionRepository.findPositionsForMonthAndYear(startMonth.getTime(), endMonth.getTime());
	    	
	    	List<PositionBaseDTO> retPositions = new ArrayList<>();
	    	for (Position position : positionsInMonth) {
	    		PositionBaseDTO posDto = new PositionBaseDTO();
	    		posDto.setEmail(position.getEmail());
	    		posDto.setFirstname(position.getFirstname());
	    		posDto.setLastname(position.getSurname());
	    		posDto.setContractor(position.getContractor());
	    		posDto.setJoinDate(position.getJoinDate() != null ? DATE_OUT_FORMATER.format(position.getJoinDate()) : null);
	    		
	    		List<Billability> bills = billabilityRepository.findBillabilityForPositionsMonthAndYear(startMonth.getTime(), endMonth.getTime(), position);
	    		Billability bill = bills.size() > 0 ? bills.get(0) : null;
	    		
	    		// if is not billable, no other calculations make sense
	    		if (bill != null) {
	    			posDto.setBillable(bill.getBillable());
	    			posDto.setBillablePercentage(bill.getPercentage());
	    		}
	    		
	    		List<Salary> sals = salaryRepository.findSalaryForPositionsMonthAndYear(startMonth.getTime(), endMonth.getTime(), position);
	    		Salary sal = sals.size() > 0 ? sals.get(0) : null;
	    		if (sal != null) {
	    			posDto.setSalary(sal.getValue()); // FIXME add currency
	    		}
	    		
	    		List<PositionProject> pps = positionProjectRepository.findPositionProjectForPositionsMonthAndYear(startMonth.getTime(), endMonth.getTime(), position);
	    		PositionProject pp = pps.size() > 0 ? pps.get(0) : null;
	    		Project prj = pp!=null ? pp.getProject() : null;
	    		
	    		if (prj != null) {
	    			posDto.setProject(prj.getName());
	    		}
	    		
	    		List<PositionJobRole> pjrs = positionJobRoleRepository.findPositionJobRoleForPositionsMonthAndYear(startMonth.getTime(), endMonth.getTime(), position);
	    		PositionJobRole pjr = pjrs.size() > 0 ? pjrs.get(0) : null;
	    		JobRole jr = pjr!=null ? pjr.getJobRole() : null;
	    		
	    		if (jr != null) {
	    			posDto.setJobRole(jr.getName());
	    			
	    			// makes sense only if job role is not null!
		    		List<RoleRate> rrs = roleRateRepository.findRoleRateForJobRoleMonthAndYear(startMonth.getTime(), endMonth.getTime(), jr);
		    		RoleRate rr = rrs.size() > 0 ? rrs.get(0) : null;
		    		
	    			posDto.setRate(rr != null ? rr.getValue() : null);
	    		}
	    		
	    		// calculate COGS and GM - billability, salary and rate has to be non null!
	    		if (posDto.getBillable() != null && posDto.getBillablePercentage() != null && posDto.getRate() != null && posDto.getSalary() != null && posDto.getContractor() != null) {
	    			Double cogs = posDto.getSalary() * SUPER_BRUTTO_COEF + (posDto.getContractor().equals(Boolean.TRUE) ? OVERHEAD_CONTRACTOR : OVERHEAD_EMPLOYEE);
	    			posDto.setCogs(cogs);
	    			
	    			Double gm = ( ( posDto.getBillable().equals(Boolean.TRUE) ? ( (posDto.getBillablePercentage()/100.0) * posDto.getRate() ) : 0.0 ) - cogs ) / posDto.getRate();
	    			posDto.setGM(gm * 100.0);
	    		}
	    			    		
	    		retPositions.add(posDto);
	    	}
    	
        return new ResponseEntity<List<PositionBaseDTO>>(retPositions, HttpStatus.OK);
    }
}
