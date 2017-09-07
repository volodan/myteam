package com.ness.myteam.dto;

public class PositionBaseDTO {

	private String email;
	private String firstname;
	private String lastname;
	private String joinDate;
	private Boolean billable;
	private Double billablePercentage;
	private Double rate;
	private Double salary;
	private Boolean contractor;
	private Double cogs;
	private Double GM;
	private String jobRole;
	private String project;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	public Boolean getBillable() {
		return billable;
	}
	public void setBillable(Boolean billable) {
		this.billable = billable;
	}
	public Double getBillablePercentage() {
		return billablePercentage;
	}
	public void setBillablePercentage(Double billablePercentage) {
		this.billablePercentage = billablePercentage;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Double getSalary() {
		return salary;
	}
	public void setSalary(Double salary) {
		this.salary = salary;
	}
	public Boolean getContractor() {
		return contractor;
	}
	public void setContractor(Boolean contractor) {
		this.contractor = contractor;
	}
	public Double getCogs() {
		return cogs;
	}
	public void setCogs(Double cogs) {
		this.cogs = cogs;
	}
	public Double getGM() {
		return GM;
	}
	public void setGM(Double gM) {
		GM = gM;
	}
	public String getJobRole() {
		return jobRole;
	}
	public void setJobRole(String jobRole) {
		this.jobRole = jobRole;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	
	
}
