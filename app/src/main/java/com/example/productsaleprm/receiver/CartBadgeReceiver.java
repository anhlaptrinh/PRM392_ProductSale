package com.example.productsaleprm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.productsaleprm.R;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.util.Log;


public class CartBadgeReceiver extends BroadcastReceiver {
    public static final String EXTRA_CART_COUNT = "cart_count";

    @Override
    public void onReceive(Context context, Intent intent) {
        int cartCount = intent.getIntExtra(EXTRA_CART_COUNT, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "cart_channel")
                .setSmallIcon(R.drawable.ic_cart)
                .setContentTitle("Giỏ hàng của bạn")
                .setContentText("Bạn có " + cartCount + " sản phẩm trong giỏ.")
                .setNumber(cartCount) // số badge
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1001, builder.build());
        } else {
            // ❌ Nếu không có quyền, có thể log hoặc gửi yêu cầu cấp quyền trong Activity/Fragment chính
            Log.d("CartBadgeReceiver", "Permission not granted to post notifications");
        }
    }
}