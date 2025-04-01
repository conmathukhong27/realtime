package org.example.process.dictionary;
import org.example.model.EventMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EventTrackingObjectNameDestinationDict {
    private static final Map<String, List<EventMap>> objectNameSet = new HashMap<>();
    static {
        List<EventMap> TOPUP_VIEW_TRANSACTIONRESULT_APP_VIEW_INFO = new ArrayList<>();
        TOPUP_VIEW_TRANSACTIONRESULT_APP_VIEW_INFO.add(EventProductEnum.TOPUP_USER_CONTINUE);
        TOPUP_VIEW_TRANSACTIONRESULT_APP_VIEW_INFO.add(EventProductEnum.TOPUP_USER_COMFIRM);
        objectNameSet.put("Telecom_topup_view_transactionresult_app_view_info", TOPUP_VIEW_TRANSACTIONRESULT_APP_VIEW_INFO);

        List<EventMap> TRANSFER_PHONENUMBER_VIEW_TRANSACTIONRESULT_APP_VIEW_INFO = new ArrayList<>();
        TRANSFER_PHONENUMBER_VIEW_TRANSACTIONRESULT_APP_VIEW_INFO.add(EventProductEnum.PHONENUMBER_USER_COMFIRM);
        objectNameSet.put("Transfer_phonenumber_view_transactionresult_app_view_info", TRANSFER_PHONENUMBER_VIEW_TRANSACTIONRESULT_APP_VIEW_INFO);

        List<EventMap> CASHLOAN_VIEW_ACCOUNT_APP_VIEW_ACCOUNT = new ArrayList<>();
        CASHLOAN_VIEW_ACCOUNT_APP_VIEW_ACCOUNT.add(EventProductEnum.CASHLOAN_APP_AMOUNT);
        objectNameSet.put("Loan_cashloan_view_account_app_view_account", CASHLOAN_VIEW_ACCOUNT_APP_VIEW_ACCOUNT);

        List<EventMap> EKYCIDCARD_VIEW_PHOTOOFIDCARD_APP_VIEW_INFO = new ArrayList<>();
        EKYCIDCARD_VIEW_PHOTOOFIDCARD_APP_VIEW_INFO.add(EventProductEnum.CASHLOAN_APP_ACCOUNT);
        objectNameSet.put("Onboarding_eKYCidcard_view_photoofidcard_app_view_info", EKYCIDCARD_VIEW_PHOTOOFIDCARD_APP_VIEW_INFO);

        List<EventMap> CASHLOAN_VIEW_ADDRESS_INFO_APP_VIEW_INFO = new ArrayList<>();
        CASHLOAN_VIEW_ADDRESS_INFO_APP_VIEW_INFO.add(EventProductEnum.CASHLOAN_PHOTOOFIDCARD_INFO);
        objectNameSet.put("Loan_cashloan_view_address_info_app_view_info", CASHLOAN_VIEW_ADDRESS_INFO_APP_VIEW_INFO);

        List<EventMap> CASHLOAN_VIEW_PROFILE_APP_VIEW_INFO = new ArrayList<>();
        CASHLOAN_VIEW_PROFILE_APP_VIEW_INFO.add(EventProductEnum.CASHLOAN_ADDRESS_INFO);
        objectNameSet.put("Loan_cashloan_view_profile_app_view_info", CASHLOAN_VIEW_PROFILE_APP_VIEW_INFO);

        List<EventMap> CASHLOAN_VIEW_REOFFER_APP_VIEW_AMOUNT = new ArrayList<>();
        CASHLOAN_VIEW_REOFFER_APP_VIEW_AMOUNT.add(EventProductEnum.CASHLOAN_PROFILE_INFO);
        objectNameSet.put("Loan_cashloan_view_reoffer_app_view_amount", CASHLOAN_VIEW_REOFFER_APP_VIEW_AMOUNT);

        List<EventMap> CASHLOAN_VIEW_VERIFY_APP_VIEW_INFO = new ArrayList<>();
        CASHLOAN_VIEW_VERIFY_APP_VIEW_INFO.add(EventProductEnum.CASHLOAN_REOFFER_AMOUNT);
        objectNameSet.put("Loan_cashloan_view_verify_app_view_info", CASHLOAN_VIEW_VERIFY_APP_VIEW_INFO);

        List<EventMap> CASHLOAN_VIEW_CONTRACT_APP_VIEW_VERIFYINFO = new ArrayList<>();
        CASHLOAN_VIEW_CONTRACT_APP_VIEW_VERIFYINFO.add(EventProductEnum.CASHLOAN_INFO_VERIFYINFO);
        objectNameSet.put("Loan_cashloan_view_contract_app_view_verifyinfo", CASHLOAN_VIEW_CONTRACT_APP_VIEW_VERIFYINFO);

        List<EventMap> CASHLOAN_VIEW_OTP_USER_BUTTON_CONFIRM = new ArrayList<>();
        CASHLOAN_VIEW_OTP_USER_BUTTON_CONFIRM.add(EventProductEnum.CASHLOAN_CONTRACT_VERIFYINFO);
        objectNameSet.put("Loan_cashloan_view_otp_user_button_confirm", CASHLOAN_VIEW_OTP_USER_BUTTON_CONFIRM);

        List<EventMap> CASHLOAN_VIEW_THANKYOU_APP_VIEW_INFO = new ArrayList<>();
        CASHLOAN_VIEW_THANKYOU_APP_VIEW_INFO.add(EventProductEnum.CASHLOAN_OTP_CONFIRM);
        objectNameSet.put("Loan_cashloan_view_thankyou_app_view_info", CASHLOAN_VIEW_THANKYOU_APP_VIEW_INFO);

        List<EventMap> EKYCSELFILE_VIEW_SELFILE_USER_BUTTON_TAKEPHOTO = new ArrayList<>();
        EKYCSELFILE_VIEW_SELFILE_USER_BUTTON_TAKEPHOTO.add(EventProductEnum.CREDITCARD_PERSONALINFO_CONTINUE);
        objectNameSet.put("Onboarding_eKYCselfile_view_selfile_user_button_takephoto", EKYCSELFILE_VIEW_SELFILE_USER_BUTTON_TAKEPHOTO);

        List<EventMap> EKYCIDCARD_VIEW_PHOTOOFIDCARD_USER_BUTTON_TAKEPHOTO = new ArrayList<>();
        EKYCIDCARD_VIEW_PHOTOOFIDCARD_USER_BUTTON_TAKEPHOTO.add(EventProductEnum.CREDITCARD_SELFILE_TAKEPHOTO);
        objectNameSet.put("Onboarding_eKYCidcard_view_photoofidcard_user_button_takephoto", EKYCIDCARD_VIEW_PHOTOOFIDCARD_USER_BUTTON_TAKEPHOTO);

        List<EventMap> CREDITCARD_VIEW_ACCOUNT_USER_BUTTON_CONTINUE = new ArrayList<>();
        CREDITCARD_VIEW_ACCOUNT_USER_BUTTON_CONTINUE.add(EventProductEnum.CREDITCARD_PHOTOOFIDCARD_TAKEPHOTO);
        objectNameSet.put("Loan_creditcard_view_account_user_button_continue", CREDITCARD_VIEW_ACCOUNT_USER_BUTTON_CONTINUE);

        List<EventMap> CREDITCARD_VIEW_OTHERINFO_USER_BUTTON_CONTINUE = new ArrayList<>();
        CREDITCARD_VIEW_OTHERINFO_USER_BUTTON_CONTINUE.add(EventProductEnum.CREDITCARD_ACCOUNT_CONTINUE);
        objectNameSet.put("Loan_creditcard_view_otherinfo_user_button_continue", CREDITCARD_VIEW_OTHERINFO_USER_BUTTON_CONTINUE);

        List<EventMap> CREDITCARD_VIEW_REFERRAL_USER_BUTTON_CONTINUE = new ArrayList<>();
        CREDITCARD_VIEW_REFERRAL_USER_BUTTON_CONTINUE.add(EventProductEnum.CREDITCARD_OTHERINFO_CONTINUE);
        objectNameSet.put("Loan_creditcard_view_referral_user_button_continue", CREDITCARD_VIEW_REFERRAL_USER_BUTTON_CONTINUE);

        List<EventMap> CREDITCARD_VIEW_CONFIRMINFO_APP_VIEW_INFO_USER_BUTTON_CONTINUE = new ArrayList<>();
        CREDITCARD_VIEW_CONFIRMINFO_APP_VIEW_INFO_USER_BUTTON_CONTINUE.add(EventProductEnum.CREDITCARD_REFERRAL_CONTINUE);
        objectNameSet.put("Loan_creditcard_view_confirminfo_app_view_info_user_button_continue", CREDITCARD_VIEW_CONFIRMINFO_APP_VIEW_INFO_USER_BUTTON_CONTINUE);

        List<EventMap> CREDITCARD_POPUP_OTP_USER_BUTTON_SKIP = new ArrayList<>();
        CREDITCARD_POPUP_OTP_USER_BUTTON_SKIP.add(EventProductEnum.CREDITCARD_CONFIRMINFO_CONTINUE);
        objectNameSet.put("Loan_creditcard_popup_otp_user_button_skip", CREDITCARD_POPUP_OTP_USER_BUTTON_SKIP);

        List<EventMap> CREDITCARD_VIEW_TRANSACTIONRESULT_USER_BUTTON_BACKTOHOME = new ArrayList<>();
        CREDITCARD_VIEW_TRANSACTIONRESULT_USER_BUTTON_BACKTOHOME.add(EventProductEnum.CREDITCARD_POPUP_SKIP);
        objectNameSet.put("Loan_creditcard_view_transactionresult_user_button_backtohome", CREDITCARD_VIEW_TRANSACTIONRESULT_USER_BUTTON_BACKTOHOME);

        List<EventMap> CREDITCARD_VIEW_WORKINGINFO_USER_BUTTON_CONTINUE = new ArrayList<>();
        CREDITCARD_VIEW_WORKINGINFO_USER_BUTTON_CONTINUE.add(EventProductEnum.CREDITCARD_WAITINGFORSIGN_CONTINUE);
        objectNameSet.put("Loan_creditcard_view_workinginfo_user_button_continue", CREDITCARD_VIEW_WORKINGINFO_USER_BUTTON_CONTINUE);

        List<EventMap> CREDITCARD_VIEW_CARDADRESS_USER_BUTTON_CONTINUE = new ArrayList<>();
        CREDITCARD_VIEW_CARDADRESS_USER_BUTTON_CONTINUE.add(EventProductEnum.CREDITCARD_WORKINGINFO_CONTINUE);
        objectNameSet.put("Loan_creditcard_view_cardadress_user_button_continue", CREDITCARD_VIEW_CARDADRESS_USER_BUTTON_CONTINUE);

        List<EventMap> CREDITCARD_VIEW_CONTRACT_USER_BUTTON_SUBMIT = new ArrayList<>();
        CREDITCARD_VIEW_CONTRACT_USER_BUTTON_SUBMIT.add(EventProductEnum.CREDITCARD_CARDADRESS_CONTINUE);
        objectNameSet.put("Loan_creditcard_view_contract_user_button_submit", CREDITCARD_VIEW_CONTRACT_USER_BUTTON_SUBMIT);

        List<EventMap> CREDITCARD_VIEW_OTP_USER_BUTTON_SKIP = new ArrayList<>();
        CREDITCARD_VIEW_OTP_USER_BUTTON_SKIP.add(EventProductEnum.CREDITCARD_CONTRACT_SUBMIT);
        objectNameSet.put("Loan_creditcard_view_otp_user_button_skip", CREDITCARD_VIEW_OTP_USER_BUTTON_SKIP);

        List<EventMap> CREDITCARD_VIEW_CORRECTRESULT_USER_BUTTON_BACKTOHOME = new ArrayList<>();
        CREDITCARD_VIEW_CORRECTRESULT_USER_BUTTON_BACKTOHOME.add(EventProductEnum.CREDITCARD_OTP_SKIP);
        objectNameSet.put("Loan_creditcard_view_correctresult_user_button_backtohome", CREDITCARD_VIEW_CORRECTRESULT_USER_BUTTON_BACKTOHOME);

    }

    public static List<String> search(String value){
        if (value == null) return null;
//        if (status!=null && status.equals("false") && objectNameSet.containsKey(value)){
//            return objectNameSet.get(value)
//                    .stream()
//                    .map(event -> event.getDestination())
//                    .collect(Collectors.toList());
//        }
        if (objectNameSet.containsKey(value)){
            return objectNameSet.get(value)
                    .stream()
                    .map(event -> event.getSource())
                    .collect(Collectors.toList());
        }
        return null;
    }
}
