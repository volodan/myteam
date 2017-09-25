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
	
	@Query("SELECT rr FROM RoleRate rr WHERE "
			+ "( (rr.validFrom is null OR rr.validFrom <= :#{#startDate} ) AND ( rr.validTo is null OR rr.validTo > :#{#endDate} ) ) "
			+ " AND rr.jobRole = :#{#jobRole} order by rr.id desc")
	List<RoleRate> findRoleRateForJobRoleMonthAndYear(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("jobRole") JobRole jobRole);
}
