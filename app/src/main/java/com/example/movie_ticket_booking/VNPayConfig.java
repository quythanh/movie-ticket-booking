package com.example.movie_ticket_booking;

import android.content.Context;
import android.content.Intent;

import lombok.Setter;

public class VNPayConfig {
    public static VNPayConfig _instance;
    
    public static VNPayConfig getInstance(){
        if (_instance == null)
            _instance = new VNPayConfig();
        return _instance;
    }
    
    private  VNPayConfig(){
        url = "https://sandbox.vnpayment.vn/testsdk/";
        tmn_code = "U6BDZMV1";
        scheme = "payactivity";
        isSandbox = true;
    }
    
    private String url;
    private String tmn_code;
    private String scheme;
    private boolean isSandbox;
    @Setter
    private Context context;

//    public void openSdk() {
//        Intent intent = new Intent(context, VNP_AuthenticationActivity.class);
//        intent.putExtra("url", "https://sandbox.vnpayment.vn/testsdk/"); //bắt buộc, VNPAY cung cấp
//        intent.putExtra("tmn_code", "FAHASA03"); //bắt buộc, VNPAY cung cấp
//        intent.putExtra("scheme", "resultactivity"); //bắt buộc, scheme để mở lại app khi có kết quả thanh toán từ mobile banking
//        intent.putExtra("is_sandbox", false); //bắt buộc, true <=> môi trường test, true <=> môi trường live
//        VNP_AuthenticationActivity.setSdkCompletedCallback(new VNP_SdkCompletedCallback() {
//            @Override
//            public void sdkAction(String action) {
//                Log.wtf("SplashActivity", "action: " + action);
//                //action == AppBackAction
//                //Người dùng nhấn back từ sdk để quay lại
//
//                //action == CallMobileBankingApp
//                //Người dùng nhấn chọn thanh toán qua app thanh toán (Mobile Banking, Ví...)
//                //lúc này app tích hợp sẽ cần lưu lại cái PNR, khi nào người dùng mở lại app tích hợp thì sẽ gọi kiểm tra trạng thái thanh toán của PNR Đó xem đã thanh toán hay chưa.
//
//                //action == WebBackAction
//                //Người dùng nhấn back từ trang thanh toán thành công khi thanh toán qua thẻ khi url có chứa: cancel.sdk.merchantbackapp
//
//                //action == FaildBackAction
//                //giao dịch thanh toán bị failed
//
//                //action == SuccessBackAction
//                //thanh toán thành công trên webview
//            }
//        });
//        startActivity(intent);
//    }

}
