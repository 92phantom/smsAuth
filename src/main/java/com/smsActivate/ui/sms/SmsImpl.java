package com.smsActivate.ui.sms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import com.smsActivate.ui.model.Country;
import com.smsActivate.ui.model.ServiceCode;
import com.smsActivate.ui.user.UserImpl;

import lombok.Getter;
import ru.sms_activate.client_enums.SMSActivateClientStatus;
import ru.sms_activate.error.base.SMSActivateBaseException;
import ru.sms_activate.response.api_activation.SMSActivateActivation;
import ru.sms_activate.response.api_activation.SMSActivateGetCountriesResponse;
import ru.sms_activate.response.api_activation.SMSActivateGetFullSmsResponse;
import ru.sms_activate.response.api_activation.SMSActivateGetNumbersStatusResponse;
import ru.sms_activate.response.api_activation.SMSActivateGetPricesResponse;
import ru.sms_activate.response.api_activation.SMSActivateGetStatusResponse;
import ru.sms_activate.response.api_activation.extra.SMSActivateCountryInfo;
import ru.sms_activate.response.api_activation.extra.SMSActivatePriceInfo;
import ru.sms_activate.response.api_activation.extra.SMSActivateServiceInfo;

@Component
@Getter
public class SmsImpl {

    @Autowired
    UserImpl user;

    private ConcurrentHashMap<String, ArrayList<Country>> map = new ConcurrentHashMap<>();
    private SMSActivateActivation activation;
    private SMSActivateGetFullSmsResponse smsActivateGetFullSmsResponse;
    private String code;
    private SMSActivateGetStatusResponse getResponse;
    
    
    public SMSActivateGetStatusResponse getGetResponse() {
		return getResponse;
	}

	public void setGetResponse(SMSActivateGetStatusResponse getResponse) {
		this.getResponse = getResponse;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ArrayList<Country> getCountryServiceInfo(String countryName) {

        return map.get(countryName);

    }

    public Long getPhoneNumber(int countryCode, String serviceName) {

        try {

            activation
                    = user.getSmsActivateApi().getNumber(countryCode,serviceName);

            setGetResponse(user.getSmsActivateApi().getStatus(activation));
            System.out.println(getGetResponse().getSMSActivateGetStatus());
            
            return activation.getNumber();

        }
        catch (Exception e){
            return -1L;
        }
    }

    public UserImpl getUser() {
		return user;
	}

	public void setUser(UserImpl user) {
		this.user = user;
	}

	public ConcurrentHashMap<String, ArrayList<Country>> getMap() {
		return map;
	}

	public void setMap(ConcurrentHashMap<String, ArrayList<Country>> map) {
		this.map = map;
	}

	public SMSActivateActivation getActivation() {
		return activation;
	}

	public void setActivation(SMSActivateActivation activation) {
		this.activation = activation;
	}

	public SMSActivateGetFullSmsResponse getSmsActivateGetFullSmsResponse() {
		return smsActivateGetFullSmsResponse;
	}

	public void setSmsActivateGetFullSmsResponse(SMSActivateGetFullSmsResponse smsActivateGetFullSmsResponse) {
		this.smsActivateGetFullSmsResponse = smsActivateGetFullSmsResponse;
	}

	
//	@Async("threadPoolTaskExecutor")
//	public Future<String> getSmsMessage() {
//
//    	System.out.println("AAAAAAAAAAAAAAAAAA");
//    	
//        try {
//            
//        	user.getSmsActivateApi().setStatus(activation, SMSActivateClientStatus.MESSAGE_WAS_SENT);
//            
//            setGetResponse(user.getSmsActivateApi().getStatus(activation));
//            System.out.println("###"+ getGetResponse().getSMSActivateGetStatus());
//            
//            
//            String code= user.getSmsActivateApi().waitSms(activation,20);
//
//            if(code == null) {
//
//                user.getSmsActivateApi().setStatus(activation, SMSActivateClientStatus.CANCEL);//叫�곎궿겆싻압꽥분� �곎궿겉귂꺫곍� CANCEL, 筠�곍뿅� sms 戟筠 極�龜�댭뿅�
//            }
//
//            else {
//                smsActivateGetFullSmsResponse = user.getSmsActivateApi().getFullSms(activation);//�읩압뿌꺫눺둔싻먁� �궿둔붇곎궿� sms
//                System.out.println("Full SMS: " + smsActivateGetFullSmsResponse.getText());//�믍뗊꽥압� �궿둔붇곎궿� SMS
//
//                return new AsyncResult<>(smsActivateGetFullSmsResponse.getText());
//            }
//
//            user.getSmsActivateApi().setStatus(activation, SMSActivateClientStatus.FINISH);//叫�곎궿겆싻압꽥분� �곎궿겉귂꺫곍� FINISH 極棘�곍뿅� 極棘剋�꺫눺둔싻먈� �곍솅� 畇剋�� 戟棘劇筠�逵
//
//            setGetResponse(user.getSmsActivateApi().getStatus(activation));
//            System.out.println("###"+ getGetResponse().getSMSActivateGetStatus());
//            
//            
//        } catch (SMSActivateBaseException e) {
//            e.printStackTrace();
//        } 
//        
//        
//        
//        System.out.println("#######"+ getGetResponse().getSMSActivateGetStatus());
//        
//        
//        return null;
//
//    }
	
	@Async("threadPoolTaskExecutor")
	public ListenableFuture<String> getSmsMessage() {

    	System.out.println("AAAAAAAAAAAAAAAAAA");
    	
        try {
            
        	user.getSmsActivateApi().setStatus(activation, SMSActivateClientStatus.MESSAGE_WAS_SENT);
            
            setGetResponse(user.getSmsActivateApi().getStatus(activation));
            System.out.println("###"+ getGetResponse().getSMSActivateGetStatus());
            
            
            String code= user.getSmsActivateApi().waitSms(activation,20);

            if(code == null) {

                user.getSmsActivateApi().setStatus(activation, SMSActivateClientStatus.CANCEL);//叫�곎궿겆싻압꽥분� �곎궿겉귂꺫곍� CANCEL, 筠�곍뿅� sms 戟筠 極�龜�댭뿅�
            }

            else {
                smsActivateGetFullSmsResponse = user.getSmsActivateApi().getFullSms(activation);//�읩압뿌꺫눺둔싻먁� �궿둔붇곎궿� sms
                System.out.println("Full SMS: " + smsActivateGetFullSmsResponse.getText());//�믍뗊꽥압� �궿둔붇곎궿� SMS

                return new AsyncResult<>(smsActivateGetFullSmsResponse.getText());
            }

            user.getSmsActivateApi().setStatus(activation, SMSActivateClientStatus.FINISH);//叫�곎궿겆싻압꽥분� �곎궿겉귂꺫곍� FINISH 極棘�곍뿅� 極棘剋�꺫눺둔싻먈� �곍솅� 畇剋�� 戟棘劇筠�逵

            setGetResponse(user.getSmsActivateApi().getStatus(activation));
            System.out.println("###"+ getGetResponse().getSMSActivateGetStatus());
            
            
        } catch (SMSActivateBaseException e) {
            e.printStackTrace();
        } 
        
        
        
        System.out.println("#######"+ getGetResponse().getSMSActivateGetStatus());
        
        
        return null;

    }
    
    public void cancelSMS() {
    	
    	
    
    	try {
    	
    		user.getSmsActivateApi().setStatus(activation, SMSActivateClientStatus.CANCEL);//叫�곎궿겆싻압꽥분� �곎궿겉귂꺫곍� CANCEL, 筠�곍뿅� sms 戟筠 極�龜�댭뿅�
//    		user.getSmsActivateApi().setStatus(activation, SMSActivateClientStatus.FINISH);//叫�곎궿겆싻압꽥분� �곎궿겉귂꺫곍� CANCEL, 筠�곍뿅� sms 戟筠 極�龜�댭뿅�

            SMSActivateGetStatusResponse tmpGetResponse = user.getSmsActivateApi().getStatus(activation);//Getting a status for activation
            setGetResponse(tmpGetResponse);
            
//            System.out.println("@@@@@@@@@@@"+ getGetResponse().getSMSActivateGetStatus());
            
    		
    	}
    	catch (SMSActivateBaseException e) {
    		e.printStackTrace();
    	}
    	
    }


    @Async
    public void loadData() {

        // 援�媛� 肄붾뱶, 硫��떚 �꽌鍮꾩뒪 �뿬遺� �솗�씤
        SMSActivateGetCountriesResponse smsActivateGetCountriesResponse = null;//�읩압뿌꺫눺둔싻먁� �곍왈먈곍분� �곎귂�逵戟 �� 龜戟�꾉앗�劇逵�녡먁둔�
        try {
            smsActivateGetCountriesResponse = user.getSmsActivateApi().getCountries();
        } catch (SMSActivateBaseException e) {
            e.printStackTrace();
        }

        List<SMSActivateCountryInfo> arrayList = smsActivateGetCountriesResponse.getSMSActivateGetCountryInfoList();

        for(SMSActivateCountryInfo smsActivateCountryInfo: arrayList) {


            try {

                // �궗�슜 媛��뒫�븳 �꽌鍮꾩뒪 �젙蹂� 諛쏆븘�삤湲�
            SMSActivateGetNumbersStatusResponse smsActivateGetNumbersStatusResponse
                 = user.getSmsActivateApi().getNumbersStatus(smsActivateCountryInfo.getId(),null);

            // �듅�젙 �꽌鍮꾩뒪瑜� 蹂댁쑀�븳 援�媛�, 媛��슜 �옄�썝 �솗�씤

            List<SMSActivateServiceInfo> smsActivateServiceInfoList = smsActivateGetNumbersStatusResponse.getAllServiceInfoList();

            for(SMSActivateServiceInfo smsActivateServiceInfo : smsActivateServiceInfoList) {

                String serviceShortName = smsActivateServiceInfo.getShortName();


                // �썝�븯�뒗 �꽌鍮꾩뒪媛� 議댁옱�븯�뒗吏� 泥댄겕
                for(ServiceCode serviceCode : ServiceCode.values()) {

                    // Google - go
                    //telegram - tg
                    //kakao talk - kt
                    //any other - ot �꽌鍮꾩뒪�뿉 �빐�떦 �릺�뒗 寃쎌슦
                    if(serviceShortName.equals(serviceCode.getServiceCode())
                            && smsActivateServiceInfo.getCountPhoneNumber() > 0) {

                        String _serviceName = serviceCode.toString();
                        String _serviceCode = serviceCode.getServiceCode();
                        int _countryCd = smsActivateCountryInfo.getId();
                        String _countryNm = smsActivateCountryInfo.getEnglishName();
                        int _available_svc_cnt = smsActivateServiceInfo.getCountPhoneNumber();

                        SMSActivateGetPricesResponse smsActivateGetPricesResponse=
                                user.getSmsActivateApi().getPricesByCountryIdAndServiceShortName(_countryCd, _serviceCode);//�읩압뿌꺫눺둔싻먁� �곍왈먈곍분� �녡둔� 畇剋�� �졤앗곎곍먁� 極棘 �곍둘�勻龜�곎� �믌분압싼궿겆붇궿�. �붠뿌� 極棘剋�꺫눺둔싻먈� 極棘剋戟棘均棘 �곍왈먈곍분� �녡둔� 龜�곍왈압뿌뚍롤꺪밂궿� 劇筠�궿압� getAllPrices();

                        SMSActivatePriceInfo smsActivatePriceInfo = smsActivateGetPricesResponse.getPriceInfo(_countryCd, _serviceCode);

                        if(map.containsKey(_serviceName)) {

                            Country country = new Country();
                            country.setCountry_code(_countryCd);
                            country.setCountry_name(_countryNm);
                            country.setAvailable_count(_available_svc_cnt);
                            country.setPrice(BigDecimal.valueOf(smsActivatePriceInfo.getCost().longValue() * 120));
                            map.get(_serviceName).add(country);

                        } else {

                            Country country = new Country();
                            country.setCountry_code(_countryCd);
                            country.setCountry_name(_countryNm);
                            country.setAvailable_count(_available_svc_cnt);
                            country.setPrice(BigDecimal.valueOf(smsActivatePriceInfo.getCost().longValue() * 120));

                            ArrayList<Country> countryArrayList = new ArrayList<>();
                            countryArrayList.add(country);

                            map.put(serviceCode.toString(), countryArrayList);

                        }


                    }


                }


            }

            }
            catch (SMSActivateBaseException e) {
//                e.printStackTrace();
            }

        }

    }



    public SmsImpl() throws SMSActivateBaseException {



    }

}
