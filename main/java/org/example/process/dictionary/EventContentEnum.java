package org.example.process.dictionary;

import org.example.model.EventContent;

public class EventContentEnum {
    public static EventContent CONTENT_TOPUP_USER_CONTINUE = new EventContent("Telecom_topup_view_info_user_button_continue","Telecom_topup_view_transactionresult_app_view_info","SEG-01JBG6GC8JCM1H3V93WK7RSPE4", "topup");
    public static EventContent CONTENT_TOPUP_USER_COMFIRM = new EventContent("Telecom_topup_view_noti_user_button_confirm","Telecom_topup_view_transactionresult_app_view_info","SEG-01JBG655F7E1P9FNK9X04CD95H", "topup");
    public static EventContent CONTENT_CASHLOAN_APP_AMOUNT = new EventContent("Loan_cashloan_view_amount_app_view_amount","Loan_cashloan_view_account_app_view_account","SEG-01JFYFGTRNND3A3DHH47141R6D", "Loan_cashloan");
    public static EventContent CONTENT_CASHLOAN_APP_ACCOUNT = new EventContent("Loan_cashloan_view_account_app_view_account","Onboarding_eKYCidcard_view_photoofidcard_app_view_info","SEG-01JFYFGTRNND3A3DHH47141R6D", "Loan_cashloan");
    public static EventContent CONTENT_CASHLOAN_PHOTOOFIDCARD_INFO = new EventContent("Onboarding_eKYCidcard_view_photoofidcard_app_view_info","Loan_cashloan_view_address_info_app_view_info","SEG-01JFYFGTRNND3A3DHH47141R6D", "Loan_cashloan");
    public static EventContent CONTENT_CASHLOAN_ADDRESS_INFO = new EventContent("Loan_cashloan_view_address_info_app_view_info","Loan_cashloan_view_profile_app_view_info","SEG-01JFYFGTRNND3A3DHH47141R6D", "Loan_cashloan");
    public static EventContent CONTENT_CASHLOAN_PROFILE_INFO = new EventContent("Loan_cashloan_view_profile_app_view_info","Loan_cashloan_view_reoffer_app_view_amount","SEG-01JFYFGTRNND3A3DHH47141R6D", "Loan_cashloan");
    public static EventContent CONTENT_CASHLOAN_REOFFER_AMOUNT = new EventContent("Loan_cashloan_view_reoffer_app_view_amount","Loan_cashloan_view_verify_app_view_info","SEG-01JFYFGTRNND3A3DHH47141R6D", "Loan_cashloan");
    public static EventContent CONTENT_CASHLOAN_INFO_VERIFYINFO = new EventContent("Loan_cashloan_view_info_app_view_verifyinfo","Loan_cashloan_view_contract_app_view_verifyinfo","SEG-01JG0F81A260S3QVYVPHEEZ96V", "Loan_cashloan");
    public static EventContent CONTENT_CASHLOAN_CONTRACT_VERIFYINFO = new EventContent("Loan_cashloan_view_contract_app_view_verifyinfo","Loan_cashloan_view_otp_user_button_confirm","SEG-01JG0F81A260S3QVYVPHEEZ96V", "Loan_cashloan");
    public static EventContent CASHLOAN_OTP_CONFIRM = new EventContent("Loan_cashloan_view_otp_user_button_confirm","Loan_cashloan_view_thankyou_app_view_info","SEG-01JG0F81A260S3QVYVPHEEZ96V", "Loan_cashloan");

}
