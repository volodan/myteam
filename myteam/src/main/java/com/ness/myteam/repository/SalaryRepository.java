package com.ness.myteam.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ness.myteam.domain.Position;
import com.ness.myteam.domain.Salary;

public interface SalaryRepository  extends CrudRepository<Salary, Integer>{

	@Query("SELECT s FROM Salary s WHERE "
			+ "( (s.validFrom is null OR s.validFrom <= :#{#startDate} ) AND ( s.validTo is null OR s.validTo > :#{#endDate} ) ) "
			+ " AND s.position = :#{#position} order by s.id desc")
	List<Salary> findSalaryForPositionsMonthAndYear(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("position") Position position);
	
}
