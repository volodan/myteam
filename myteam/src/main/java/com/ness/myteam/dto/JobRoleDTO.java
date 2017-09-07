package com.ness.myteam.dto;

public class JobRoleDTO {

	private Integer id;
    private String name;
    private Double rateValue;
    private String rateCurrency;
    private String validFrom;
    private String validTo;
    
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
	public Double getRateValue() {
		return rateValue;
	}
	public void setRateValue(Double rateValue) {
		this.rateValue = rateValue;
	}
	public String getRateCurrency() {
		return rateCurrency;
	}
	public void setRateCurrency(String rateCurrency) {
		this.rateCurrency = rateCurrency;
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
	public JobRoleDTO(Integer id, String name, Double rateValue, String rateCurrency, String validFrom,
			String validTo) {
		super();
		this.id = id;
		this.name = name;
		this.rateValue = rateValue;
		this.rateCurrency = rateCurrency;
		this.validFrom = validFrom;
		this.validTo = validTo;
	}
	public JobRoleDTO() {
		super();
	}
}
