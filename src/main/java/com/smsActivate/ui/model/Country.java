package com.smsActivate.ui.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class Country {

    private int country_code;
    private String country_name;
    private int available_count;
    private boolean call_forward;
    private BigDecimal price;
	public int getCountry_code() {
		return country_code;
	}
	public void setCountry_code(int country_code) {
		this.country_code = country_code;
	}
	public String getCountry_name() {
		return country_name;
	}
	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}
	public int getAvailable_count() {
		return available_count;
	}
	public void setAvailable_count(int available_count) {
		this.available_count = available_count;
	}
	public boolean isCall_forward() {
		return call_forward;
	}
	public void setCall_forward(boolean call_forward) {
		this.call_forward = call_forward;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

    
    
}
