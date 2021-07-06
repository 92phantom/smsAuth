package com.smsActivate.ui.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.sms_activate.SMSActivateApi;
import ru.sms_activate.error.base.SMSActivateBaseException;

import java.math.BigDecimal;

@Component
@Getter
@Setter
public class UserImpl implements User{

    private String API_KEY;
    private BigDecimal user_balance;
    private BigDecimal user_cashback;

    private SMSActivateApi smsActivateApi;

    private boolean user_login;

    public String getAPI_KEY() {
		return API_KEY;
	}

	public void setAPI_KEY(String aPI_KEY) {
		API_KEY = aPI_KEY;
	}

	public BigDecimal getUser_balance() {
		return user_balance;
	}

	public void setUser_balance(BigDecimal user_balance) {
		this.user_balance = user_balance;
	}

	public BigDecimal getUser_cashback() {
		return user_cashback;
	}

	public void setUser_cashback(BigDecimal user_cashback) {
		this.user_cashback = user_cashback;
	}

	public SMSActivateApi getSmsActivateApi() {
		return smsActivateApi;
	}

	public void setSmsActivateApi(SMSActivateApi smsActivateApi) {
		this.smsActivateApi = smsActivateApi;
	}

	public boolean isUser_login() {
		return user_login;
	}

	public void setUser_login(boolean user_login) {
		this.user_login = user_login;
	}

	@Override
    public boolean login(String API_KEY){

        try {

            smsActivateApi = new SMSActivateApi(API_KEY);
            user_balance = smsActivateApi.getBalanceAndCashBack().getBalanceAndCashBack();
            user_cashback = smsActivateApi.getBalanceAndCashBack().getCashBack();

            user_balance = BigDecimal.valueOf(user_balance.longValue() * 120);

            user_login = true;
            return true;
        }
        catch(SMSActivateBaseException e){

            user_login = false;
            return false;
        }

    }

    @Override
    public BigDecimal reloadBlanace() {

        try {
            user_balance = smsActivateApi.getBalanceAndCashBack().getBalanceAndCashBack();
            user_cashback = smsActivateApi.getBalanceAndCashBack().getCashBack();

            user_balance = BigDecimal.valueOf(user_balance.longValue() * 120);

        }
        catch(SMSActivateBaseException e) {

        }

        return null;
    }
}
