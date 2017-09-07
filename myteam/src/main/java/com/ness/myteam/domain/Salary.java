package com.ness.myteam.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Salary is tight to actual Position/Employee if position is filled. Can be changed in time
 * @author P3501100
 */
@Entity
@Table(name = "salary")
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(length = 1000, name = "note", nullable = true)
    private String note;
    
    @Column(name = "valid_from", nullable = false)
    private Date validFrom;

    @Column(name = "valid_to", nullable = true)
    private Date validTo;

    @Column(name = "approved", nullable = false)
    private Boolean approved;
    
    @Column(name = "currency", nullable = false)
    private String currency;
    
    @Column(name = "value", nullable = false)
    private Double value;
	
    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "Salary [validFrom=" + validFrom + ", currency=" + currency + ", value=" + value + "]";
	}

    
}
