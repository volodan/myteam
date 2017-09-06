package com.ness.myteam.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ness.myteam.domain.Project;
import com.ness.myteam.dto.ProjectDTO;
import com.ness.myteam.repository.ProjectRepository;

@Controller
@RequestMapping("/api/project")
public class ProjectController {

	private static final Logger LOGGER = Logger.getLogger(ProjectController.class);
	
	private static final String DATE_OUT_FORMAT = "dd.MM.yyyy";
	
	private static final String CSV_DELIMITER = ",";
	
	private static final SimpleDateFormat DATE_OUT_FORMATER = new SimpleDateFormat(DATE_OUT_FORMAT);

	@Autowired
	private ProjectRepository projectRepository;
	
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
				if (vals.length != 3) {
					LOGGER.warn("Unable to import project with values: " + line);
					continue;
				}
				
				Project project = new Project();
				project.setName(vals[0].trim());
				project.setValidFrom(DATE_OUT_FORMATER.parse(vals[1].trim()));
				project.setActual(Boolean.parseBoolean(vals[2].trim()));
				
				LOGGER.debug("Processing Project : " + project.getName());
				// check if such projec doesn't already exist
				if (projectRepository.findByName(project.getName()) != null) { 
					LOGGER.warn("Project already exists: " + project.getName());
					continue;
				}
				
				projectRepository.save(project);
				counter++;
			}
			
		} catch (Exception e) {
			LOGGER.warn("Unable to process file: " + file.getOriginalFilename(), e);
		} finally {
			if (br != null) { try { br.close(); } catch (IOException iEx) { LOGGER.warn("Error closing buf. reader for: " + file.getOriginalFilename(), iEx);} }
			if (r != null) { try { r.close(); } catch (IOException iEx) { LOGGER.warn("Error closing reader for: " + file.getOriginalFilename(), iEx);} }
		}

		if (counter > 0) {
			LOGGER.info("Number of projects created: " + counter);
		}
		
        return new ResponseEntity<String>("Done", HttpStatus.OK);
	}

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<ProjectDTO>> listProjects() {
    	List<ProjectDTO> projects = new ArrayList<>();
    	for (Project p : projectRepository.findAll()) {
    		projects.add(assemble(p));
    	}
        return new ResponseEntity<List<ProjectDTO>>(projects, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ResponseEntity<List<ProjectDTO>> listActiveProjects() {
    	List<ProjectDTO> projects = new ArrayList<>();
    	for (Project p : projectRepository.findAll()) {
    		projects.add(assemble(p));
    	}
        return new ResponseEntity<List<ProjectDTO>>(projects, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> createProject(@RequestBody ProjectDTO projectDTO) {
    	Project tempProject = projectRepository.findByName(projectDTO.getName());
    	if (tempProject != null) {
			LOGGER.warn("Project already exists: " + projectDTO.getName());
	        return new ResponseEntity<String>("User already exist", HttpStatus.PRECONDITION_FAILED);
    	}
    	
    	try {
			projectRepository.save(build(projectDTO));
		} catch (ParseException e) {
			LOGGER.error("Unable to save user", e);
	        return new ResponseEntity<String>("Failed to save user", HttpStatus.INTERNAL_SERVER_ERROR);
		}
        return new ResponseEntity<String>("User Created", HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<String> updateProject(@RequestBody ProjectDTO projectDTO) {
    	
    	
        return new ResponseEntity<String>("User Updated", HttpStatus.OK);
    }
    
    private ProjectDTO assemble(Project p) {
    	return new ProjectDTO(p.getId(), p.getName(), p.getDescription(), DATE_OUT_FORMATER.format(p.getValidFrom()),
    			p.getValidTo() != null ? DATE_OUT_FORMATER.format(p.getValidTo()) : null, p.getActual());
    }
    
    private Project build(ProjectDTO p) throws ParseException {
    	return new Project(p.getId(), p.getName(), p.getDescription(), 
    			p.getValidFrom() != null ? DATE_OUT_FORMATER.parse(p.getValidFrom()) : null,
    	    	p.getValidTo() != null ? DATE_OUT_FORMATER.parse(p.getValidTo()) : null, 
    		    p.getActual());
    }
}
