package org.example.process.dictionary;

import org.example.model.EventMap;

import java.util.HashMap;
import java.util.Map;

public class EventTrackingObjectNameSourceDict {
    private static final Map<String, EventMap> objectNameSet = new HashMap<>();
    static {
        objectNameSet.put("Telecom_topup_view_info_user_button_continue", EventProductEnum.TOPUP_USER_CONTINUE);
        objectNameSet.put("Telecom_topup_view_noti_user_button_confirm", EventProductEnum.TOPUP_USER_COMFIRM);
        objectNameSet.put("Transfer_phonenumber_view_noti_user_button_confirm", EventProductEnum.PHONENUMBER_USER_COMFIRM);
        objectNameSet.put("Loan_cashloan_view_amount_app_view_amount", EventProductEnum.CASHLOAN_APP_AMOUNT);
        objectNameSet.put("Loan_cashloan_view_account_app_view_account", EventProductEnum.CASHLOAN_APP_ACCOUNT);
        objectNameSet.put("Onboarding_eKYCidcard_view_photoofidcard_app_view_info", EventProductEnum.CASHLOAN_PHOTOOFIDCARD_INFO);
        objectNameSet.put("Loan_cashloan_view_address_info_app_view_info", EventProductEnum.CASHLOAN_ADDRESS_INFO);
        objectNameSet.put("Loan_cashloan_view_profile_app_view_info", EventProductEnum.CASHLOAN_PROFILE_INFO);
        objectNameSet.put("Loan_cashloan_view_reoffer_app_view_amount", EventProductEnum.CASHLOAN_REOFFER_AMOUNT);
        objectNameSet.put("Loan_cashloan_view_info_app_view_verifyinfo", EventProductEnum.CASHLOAN_INFO_VERIFYINFO);
        objectNameSet.put("Loan_cashloan_view_contract_app_view_verifyinfo", EventProductEnum.CASHLOAN_CONTRACT_VERIFYINFO);
        objectNameSet.put("Loan_cashloan_view_otp_user_button_confirm", EventProductEnum.CASHLOAN_OTP_CONFIRM);
        objectNameSet.put("Loan_creditcard_view_personalinfo_user_button_continue", EventProductEnum.CREDITCARD_PERSONALINFO_CONTINUE);
        objectNameSet.put("Onboarding_eKYCselfile_view_selfile_user_button_takephoto", EventProductEnum.CREDITCARD_SELFILE_TAKEPHOTO);
        objectNameSet.put("Onboarding_eKYCidcard_view_photoofidcard_user_button_takephoto", EventProductEnum.CREDITCARD_PHOTOOFIDCARD_TAKEPHOTO);
        objectNameSet.put("Loan_creditcard_view_account_user_button_continue", EventProductEnum.CREDITCARD_ACCOUNT_CONTINUE);
        objectNameSet.put("Loan_creditcard_view_otherinfo_user_button_continue", EventProductEnum.CREDITCARD_OTHERINFO_CONTINUE);
        objectNameSet.put("Loan_creditcard_view_referral_user_button_continue", EventProductEnum.CREDITCARD_REFERRAL_CONTINUE);
        objectNameSet.put("Loan_creditcard_view_confirminfo_app_view_info_user_button_continue", EventProductEnum.CREDITCARD_CONFIRMINFO_CONTINUE);
        objectNameSet.put("Loan_creditcard_popup_otp_user_button_skip", EventProductEnum.CREDITCARD_POPUP_SKIP);
        objectNameSet.put("Loan_creditcard_view_waitingforsign_user_button_continue", EventProductEnum.CREDITCARD_WAITINGFORSIGN_CONTINUE);
        objectNameSet.put("Loan_creditcard_view_workinginfo_user_button_continue", EventProductEnum.CREDITCARD_WORKINGINFO_CONTINUE);
        objectNameSet.put("Loan_creditcard_view_cardadress_user_button_continue", EventProductEnum.CREDITCARD_CARDADRESS_CONTINUE);
        objectNameSet.put("Loan_creditcard_view_contract_user_button_submit", EventProductEnum.CREDITCARD_CONTRACT_SUBMIT);
        objectNameSet.put("Loan_creditcard_view_otp_user_button_skip", EventProductEnum.CREDITCARD_OTP_SKIP);

    }

    public static EventMap search(String value){
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
