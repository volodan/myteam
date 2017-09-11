package com.ness.myteam.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ness.myteam.domain.Position;

public interface PositionRepository extends CrudRepository<Position, Integer> {

	public Position findByEmail(String email);
	
	@Query("SELECT p FROM Position p WHERE "
			+ "( ( (p.joinDate is null OR p.joinDate <= :#{#startDate} ) OR ( p.proposedJoinDate is null OR p.proposedJoinDate <= :#{#startDate} ) ) "
			+ "AND ( p.leaveDate is null OR p.leaveDate > :#{#endDate} ) ) order by p.surname asc")
	List<Position> findPositionsForMonthAndYear(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
