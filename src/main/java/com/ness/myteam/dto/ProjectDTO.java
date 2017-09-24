package com.ness.myteam.dto;

public class ProjectDTO {

	private Integer id;
    private String name;
    private String description;
    private String validFrom;
    private String validTo;
    private Boolean actual;
    
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
	public String getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}
	public String getValidTo() {
		return validTo;
	}
	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}
	public Boolean getActual() {
		return actual;
	}
	public void setActual(Boolean actual) {
		this.actual = actual;
	}
	public ProjectDTO(Integer id, String name, String description, String validFrom, String validTo, Boolean actual) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.actual = actual;
	}
	public ProjectDTO() {
		super();
	}	
}
