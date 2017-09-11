package com.ness.myteam.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ness.myteam.domain.Position;

public interface PositionRepository extends CrudRepository<Position, Integer> {

	public Position findByEmail(String email);
	
	@Query("select p from Position p where "
			+ "(p.joinDate <= :#{#startDate} or p.validTo <= :#{#startDate}) "
			+ "(rr.validFrom <= :#{#date} and rr.validTo > :#{#date}) order by rr.id desc")
	List<Position> find(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
