        package com.example.productsaleprm.activity;

        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Looper;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;

        import com.bumptech.glide.Glide;
        import com.example.productsaleprm.R;
        import com.example.productsaleprm.model.response.PaymentSuccessResponse;
        import com.example.productsaleprm.retrofit.OrderApi;
        import com.example.productsaleprm.retrofit.RetrofitClient;
        import com.google.zxing.BarcodeFormat;
        import com.google.zxing.WriterException;
        import com.journeyapps.barcodescanner.BarcodeEncoder;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

        public class MomoPaymentActivity extends AppCompatActivity {

            private ImageView imgQrCode;
            private TextView tvMomoLink;
            private ImageView btnBack;
            private Handler handler = new Handler(Looper.getMainLooper());
            private final int POLL_INTERVAL = 3000; // 3s
            private int orderId = -1;

            private boolean isPaymentSuccess = false;


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
                orderId = getIntent().getIntExtra("ORDER_ID", -1);
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
                startPollingPaymentStatus();
            }

            private void startPollingPaymentStatus() {
                if (orderId == -1) return;

                OrderApi api = RetrofitClient.getClient(MomoPaymentActivity.this).create(OrderApi.class);
                api.getPaymentStatus(orderId).enqueue(new Callback<PaymentSuccessResponse>() {
                    @Override
                    public void onResponse(Call<PaymentSuccessResponse> call, Response<PaymentSuccessResponse> response) {
                        if (response.isSuccessful()) {
                            isPaymentSuccess = true;
                            PaymentSuccessResponse data = response.body();

                            // ✅ Chuyển sang màn hình thanh toán thành công
                            Intent intent = new Intent(MomoPaymentActivity.this, PaymentSuccessActivity.class);
                            intent.putExtra("PAYMENT_METHOD", "MOMO");
                            intent.putExtra("ORDER_ID", orderId);
                            intent.putExtra("TOTAL_AMOUNT", data.getTotalAmount().toPlainString());
                            intent.putExtra("PAYMENT_DATE", data.getPaymentDate());
                            intent.putExtra("PAYMENT_STATUS", data.getPaymentStatus());
                            startActivity(intent);
                            finish();
                        } else {
                            // ❌ chưa thanh toán → tiếp tục kiểm tra sau 3s
                            retryPolling();
                        }
                    }

                    @Override
                    public void onFailure(Call<PaymentSuccessResponse> call, Throwable t) {
                        retryPolling(); // retry khi lỗi mạng
                    }
                });
            }

            private void retryPolling() {
                if (!isPaymentSuccess) {
                    handler.postDelayed(this::startPollingPaymentStatus, POLL_INTERVAL);
                }
            }

            private void showBackConfirm() {
                new AlertDialog.Builder(this)
                        .setTitle("Thoát thanh toán?")
                        .setMessage("Đơn hàng đã được tạo và sẽ hết hạn nếu bạn không hoàn tất thanh toán MOMO.")
                        .setPositiveButton("Thoát", (dialogInterface, i) -> {
                            Intent intent = new Intent(MomoPaymentActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish(); // đóng MomoPaymentActivity
                        })
                        .setNegativeButton("Tiếp tục", null)
                        .show();
            }
        }
