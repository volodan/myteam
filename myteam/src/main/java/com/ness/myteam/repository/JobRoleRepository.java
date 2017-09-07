package com.ness.myteam.repository;

import org.springframework.data.repository.CrudRepository;

import com.ness.myteam.domain.JobRole;

public interface JobRoleRepository extends CrudRepository<JobRole, Integer>  {

	public JobRole findByName(String name);
}
