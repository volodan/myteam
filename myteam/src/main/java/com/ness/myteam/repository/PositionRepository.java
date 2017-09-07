package com.ness.myteam.repository;

import org.springframework.data.repository.CrudRepository;

import com.ness.myteam.domain.Position;

public interface PositionRepository extends CrudRepository<Position, Integer> {

	public Position findByEmail(String email);
}
