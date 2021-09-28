package com.example.mopay;

import com.example.mopay.notification.MyResponse;
import com.example.mopay.notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAN3B7RHE:APA91bHBQW51DJA6s3YRMg-tvdMe8_u8kA3SY5mZRx81ula1jb5qL9OH4hyB1OqAaQBnWqWY4dP_vfji8xbENNHX2XsaLzgUYm3SCGG524mjJnzm165w77HeuKxMy9LL5gQA0GVqPHY0"
            }
    )



    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}