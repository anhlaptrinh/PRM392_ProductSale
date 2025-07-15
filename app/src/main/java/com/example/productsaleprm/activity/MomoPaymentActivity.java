package com.example.productsaleprm.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.productsaleprm.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MomoPaymentActivity extends AppCompatActivity {

    private ImageView imgQrCode;
    private TextView tvMomoLink;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_momo_payment);

        // Ánh xạ View
        imgQrCode = findViewById(R.id.imgQrCode);
        tvMomoLink = findViewById(R.id.tvMomoLink);
        btnBack = findViewById(R.id.btnBack);

        // Nhận dữ liệu từ Intent
        String qrCodeUrl = getIntent().getStringExtra("QR_CODE_URL");
        String payUrl = getIntent().getStringExtra("PAY_URL");

        // Load QR Code
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(qrCodeUrl, BarcodeFormat.QR_CODE, 360, 360);
            imgQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Không thể tạo QR Code!", Toast.LENGTH_SHORT).show();
        }

        // Xử lý click link MOMO
        tvMomoLink.setOnClickListener(v -> {
            if (payUrl != null && !payUrl.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(payUrl));
                startActivity(browserIntent);
            }
        });

        // Xử lý nút Back
        btnBack.setOnClickListener(v -> showBackConfirm());
    }

    private void showBackConfirm() {
        new AlertDialog.Builder(this)
                .setTitle("Thoát thanh toán?")
                .setMessage("Đơn hàng đã được tạo và sẽ hết hạn nếu bạn không hoàn tất thanh toán MOMO.")
                .setPositiveButton("Thoát", (dialogInterface, i) -> finish())
                .setNegativeButton("Tiếp tục", null)
                .show();
    }
}
