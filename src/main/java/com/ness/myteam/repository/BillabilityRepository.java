package com.ness.myteam.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ness.myteam.domain.Billability;
import com.ness.myteam.domain.Position;

public interface BillabilityRepository extends CrudRepository<Billability, Integer> {

	@Query("SELECT b FROM Billability b WHERE "
			+ "( (b.validFrom is null OR b.validFrom <= :#{#startDate} ) AND ( b.validTo is null OR b.validTo > :#{#endDate} ) ) "
			+ " AND b.position = :#{#position} order by b.id desc")
	List<Billability> findBillabilityForPositionsMonthAndYear(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("position") Position position);
	
}
