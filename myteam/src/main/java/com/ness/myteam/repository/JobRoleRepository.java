package com.ness.myteam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ness.myteam.domain.JobRole;
import com.ness.myteam.domain.RoleRate;

public interface JobRoleRepository extends CrudRepository<JobRole, Integer>  {

	public JobRole findByName(String name);
	


}
