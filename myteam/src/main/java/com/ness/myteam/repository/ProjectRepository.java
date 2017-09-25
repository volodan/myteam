package com.ness.myteam.repository;

import org.springframework.data.repository.CrudRepository;

import com.ness.myteam.domain.Project;

public interface ProjectRepository extends CrudRepository<Project, Integer> {

	public Project findByName(String name);
	
	public Iterable<Project> findByActual(Boolean actual);
}
