package com.smsActivate.ui.user;

import java.math.BigDecimal;

public interface User {

    boolean login(String API_KEY);

    BigDecimal reloadBlanace();

}