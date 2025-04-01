package org.example.process.dictionary;

import org.example.model.EventContent;
import org.example.model.EventMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventTrackingContentDict {
    private static final Map<String, EventContent> objectNameSet = new HashMap<>();
    static {
        objectNameSet.put("Telecom_topup_view_info_user_button_continue", EventContentEnum.CONTENT_TOPUP_USER_CONTINUE);
        objectNameSet.put("Telecom_topup_view_noti_user_button_confirm", EventContentEnum.CONTENT_TOPUP_USER_COMFIRM);
        objectNameSet.put("Loan_cashloan_view_amount_app_view_amount", EventContentEnum.CONTENT_CASHLOAN_APP_AMOUNT);
        objectNameSet.put("Loan_cashloan_view_account_app_view_account", EventContentEnum.CONTENT_CASHLOAN_APP_ACCOUNT);
        objectNameSet.put("Onboarding_eKYCidcard_view_photoofidcard_app_view_info", EventContentEnum.CONTENT_CASHLOAN_PHOTOOFIDCARD_INFO);
        objectNameSet.put("Loan_cashloan_view_address_info_app_view_info", EventContentEnum.CONTENT_CASHLOAN_ADDRESS_INFO);
        objectNameSet.put("Loan_cashloan_view_profile_app_view_info", EventContentEnum.CONTENT_CASHLOAN_PROFILE_INFO);
        objectNameSet.put("Loan_cashloan_view_reoffer_app_view_amount", EventContentEnum.CONTENT_CASHLOAN_REOFFER_AMOUNT);
        objectNameSet.put("Loan_cashloan_view_info_app_view_verifyinfo", EventContentEnum.CONTENT_CASHLOAN_INFO_VERIFYINFO);
        objectNameSet.put("Loan_cashloan_view_contract_app_view_verifyinfo", EventContentEnum.CONTENT_CASHLOAN_CONTRACT_VERIFYINFO);
        objectNameSet.put("Loan_cashloan_view_otp_user_button_confirm", EventContentEnum.CASHLOAN_OTP_CONFIRM);
    }

    public static EventContent search(String value){
        if (value == null) return null;
//        if (status!=null && status.equals("false") && objectNameSet.containsKey(value)){
//            return objectNameSet.get(value).getDestination();
//        }
        if (objectNameSet.containsKey(value)){
            return objectNameSet.get(value);
        }
        return null;
    }

}
