package com.ness.myteam.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Table(name = "position_job_role")
public class PositionJobRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "position_id")
	private Position position;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "job_role_id")
	private JobRole jobRole;

    @Column(name = "valid_from", nullable = false)
    private Date validFrom;

    @Column(name = "valid_to", nullable = true)
    private Date validTo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public JobRole getJobRole() {
		return jobRole;
	}

	public void setJobRole(JobRole jobRole) {
		this.jobRole = jobRole;
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


	@Override
	public String toString() {
		return "PositionJobRole [id=" + id + ", jobRole=" + jobRole + "]";
	}
}
