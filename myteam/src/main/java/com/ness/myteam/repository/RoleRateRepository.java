package com.ness.myteam.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ness.myteam.domain.JobRole;
import com.ness.myteam.domain.RoleRate;

public interface RoleRateRepository extends CrudRepository<RoleRate, Integer> {

	@Query("select rr from RoleRate rr where rr.jobRole = :#{#jobRole} and "
			+ "(rr.validFrom <= :#{#date} and rr.validTo > :#{#date}) order by rr.id desc")
	List<RoleRate> findActualRoleRateByJobRole(@Param("jobRole") JobRole jobRole, @Param("date") Date date);
	
}
