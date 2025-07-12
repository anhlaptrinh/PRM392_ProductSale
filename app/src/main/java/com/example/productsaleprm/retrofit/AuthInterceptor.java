package com.example.productsaleprm.retrofit;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements okhttp3.Interceptor {

        private final Context context;

        public AuthInterceptor(Context context) {
            this.context = context;
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws java.io.IOException {
            String token = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                    .getString("jwt_token", null);

            okhttp3.Request original = chain.request();
            okhttp3.Request.Builder builder = original.newBuilder();

            if (token != null) {
                builder.header("Authorization", "Bearer " + token);
            }

            okhttp3.Request request = builder.build();
            return chain.proceed(request);
        }
    }

