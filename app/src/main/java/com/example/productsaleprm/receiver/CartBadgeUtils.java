package com.example.productsaleprm.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.content.Intent;
public class CartBadgeUtils {

    public static void sendCartBadge(Context context, int cartCount) {
        Intent intent = new Intent(context, CartBadgeReceiver.class);
        intent.putExtra(CartBadgeReceiver.EXTRA_CART_COUNT, cartCount);
        context.sendBroadcast(intent);
    }
}

