package com.ness.myteam.domain;

import java.util.Date;
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

/**
 * Project inside EDC, can be closed already, but we need audit data
 * @author P3501100
 */
@Entity
@Table(name = "project")
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

    @Column(length = 100, name = "name", nullable = true)
    private String name;

    @Column(length = 1000, name = "description", nullable = true)
    private String description;
	
    @Column(name = "valid_from", nullable = false)
    private Date validFrom;

    @Column(name = "valid_to", nullable = true)
    private Date validTo;
    
    @Column(name = "actual", nullable = false)
    private Boolean actual;
	
    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY, 
    		cascade = CascadeType.ALL)
	private List<PositionProject> positionProjects;

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

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public Boolean getActual() {
		return actual;
	}

	public void setActual(Boolean actual) {
		this.actual = actual;
	}

	public List<PositionProject> getPositionProjects() {
		return positionProjects;
	}

	public void setPositionProjects(List<PositionProject> positionProjects) {
		this.positionProjects = positionProjects;
	}

	public Project(Integer id, String name, String description, Date validFrom, Date validTo, Boolean actual) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.actual = actual;
	}

	public Project() {
		super();
	}
	
}
