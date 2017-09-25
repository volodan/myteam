package com.ness.myteam.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "job_role")
public class JobRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(length = 100, name = "name", nullable = true)
    private String name;

    @Column(length = 1000, name = "description", nullable = true)
    private String description;
	
    @OneToMany(mappedBy = "jobRole", fetch = FetchType.LAZY, 
    		cascade = CascadeType.ALL)
	private List<RoleRate> roleRates;
    
    @OneToMany(mappedBy = "jobRole", fetch = FetchType.LAZY, 
    		cascade = CascadeType.ALL)
	private List<PositionJobRole> positionJobRoles;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<RoleRate> getRoleRates() {
		return roleRates;
	}

	public void setRoleRates(List<RoleRate> roleRates) {
		this.roleRates = roleRates;
	}

	public List<PositionJobRole> getPositionJobRoles() {
		return positionJobRoles;
	}

	public void setPositionJobRoles(List<PositionJobRole> positionJobRoles) {
		this.positionJobRoles = positionJobRoles;
	}

	@Override
	public String toString() {
		return "JobRole [id=" + id + ", name=" + name + ", roleRates=" + roleRates + "]";
	}
}
