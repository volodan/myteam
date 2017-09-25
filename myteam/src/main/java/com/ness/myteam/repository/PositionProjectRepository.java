package com.ness.myteam.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ness.myteam.domain.Position;
import com.ness.myteam.domain.PositionProject;

public interface PositionProjectRepository  extends CrudRepository<PositionProject, Integer> {

	@Query("SELECT pp FROM PositionProject pp WHERE "
			+ "( (pp.validFrom is null OR pp.validFrom <= :#{#startDate} ) AND ( pp.validTo is null OR pp.validTo > :#{#endDate} ) ) "
			+ " AND pp.position = :#{#position} order by pp.id desc")
	List<PositionProject> findPositionProjectForPositionsMonthAndYear(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("position") Position position);
	
	
}
