package com.smsActivate.ui.model;


import lombok.Getter;

@Getter

public enum ServiceCode {

    GOOGLE("go"),
    TELEGRAM("tg"),
    KAKAOTALK("kt"),
    ANYOTHER("ot");

    private String serviceCode;

    ServiceCode(String serviceCode){
        this.serviceCode = serviceCode;
    }

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

    
    
}
