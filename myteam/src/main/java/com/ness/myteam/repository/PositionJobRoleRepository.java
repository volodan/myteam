package com.ness.myteam.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ness.myteam.domain.Position;
import com.ness.myteam.domain.PositionJobRole;

public interface PositionJobRoleRepository  extends CrudRepository<PositionJobRole, Integer> {

	@Query("SELECT pjr FROM PositionJobRole pjr WHERE "
			+ "( (pjr.validFrom is null OR pjr.validFrom <= :#{#startDate} ) AND ( pjr.validTo is null OR pjr.validTo > :#{#endDate} ) ) "
			+ " AND pjr.position = :#{#position} order by pjr.id desc")
	List<PositionJobRole> findPositionJobRoleForPositionsMonthAndYear(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("position") Position position);
	
}
