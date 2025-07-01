package com.example.productsaleprm.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.productsaleprm.R;
import com.example.productsaleprm.adapter.StoreAdapter;
import com.example.productsaleprm.databinding.ActivityMapBinding;
import com.example.productsaleprm.model.StoreLocation;
import com.example.productsaleprm.model.response.BaseResponse;
import com.example.productsaleprm.retrofit.RetrofitClient;
import com.example.productsaleprm.retrofit.StoreLocationApiService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private ActivityMapBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap myMap;
    private List<StoreLocation> storeLocationList = new ArrayList<>();
    private LatLng currentLatLng;
    private boolean isStoreListVisible = true;
    private StoreAdapter storeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        // 1. Khởi tạo nền bản đồ (Map Engine)
        // 2. Khi bản đồ sẵn sàng, SDK gọi lại: onMapReady
        if(mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Để đến my location
        binding.btnMyLocation.setOnClickListener(v -> {
            if (currentLatLng != null && myMap != null) {
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));
            } else {
                Toast.makeText(this, "My Location is not ready!", Toast.LENGTH_SHORT).show();
            }
        });

        // Để bật store list
        binding.btnToggleList.setOnClickListener(v -> {
            if (isStoreListVisible) {
                // Animate list
                binding.rvStores.animate()
                        .translationY(binding.rvStores.getHeight())
                        .alpha(0f)
                        .setDuration(300)
                        .start();

                // Animate button
                binding.btnToggleList.animate()
                        .translationY(binding.btnToggleList.getHeight() / 10)
                        .rotation(180f)
                        .setDuration(300)
                        .withEndAction(() -> {
                            binding.rvStores.setVisibility(View.GONE);
                        })
                        .start();
                binding.btnToggleList.setImageResource(R.drawable.ic_arrow_drop_up); // 👆 icon cho expand
            } else {
                binding.rvStores.setTranslationY(binding.rvStores.getHeight());
                binding.rvStores.setAlpha(0f);
                binding.rvStores.setVisibility(View.VISIBLE);

                binding.rvStores.animate()
                        .translationY(0)
                        .alpha(1f)
                        .setDuration(300)
                        .start();

                binding.btnToggleList.animate()
                        .translationY(0)
                        .rotation(0f)
                        .setDuration(300)
                        .start();

                binding.btnToggleList.setImageResource(R.drawable.ic_arrow_drop_down); // 👇 icon cho collapse
            }
            isStoreListVisible = !isStoreListVisible;
        });

        // Hiển thị danh sách stores
        storeAdapter = new StoreAdapter(storeLocationList, store -> {
            LatLng destination = new LatLng(store.getLatitude(), store.getLongitude());
            if (currentLatLng != null) {
                showRoute(currentLatLng, destination);
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination, 15));
            }
        });

        binding.rvStores.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL
                , false));
        binding.rvStores.setAdapter(storeAdapter);

        // Back Button
        binding.btnBackContainer.setOnClickListener(v -> finish());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        fetchStoreLocations();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION
            );
            return;
        }

        myMap.setMyLocationEnabled(true);
        myMap.getUiSettings().setZoomControlsEnabled(true);

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if(location != null) {
                currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                // Thêm marker cho My Location và những cửa hàng
                myMap.addMarker(new MarkerOptions().position(currentLatLng).title("My Location"));
                for (StoreLocation storeLocation : storeLocationList) {
                    LatLng pos = new LatLng(storeLocation.getLatitude(), storeLocation.getLongitude());
                    myMap.addMarker(new MarkerOptions()
                            .position(pos)
                            .title(getString(R.string.app_name))
                            .icon(BitmapDescriptorFactory.defaultMarker(
                                    BitmapDescriptorFactory.HUE_BLUE))
                    );
                }
                // Xử lý sự kiện khi nhấn vào bất ký cửa hàng
                myMap.setOnMarkerClickListener(marker -> {
                    LatLng destination = marker.getPosition();
                    if(!destination.equals(currentLatLng)) {
                        showRoute(currentLatLng, destination);
                    }
                    return false;
                });
            }
            else {
                Toast.makeText(this, "Can not get current location!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Lấy danh sách cửa hàng từ backend
    private void fetchStoreLocations() {
        StoreLocationApiService apiService =
                RetrofitClient.getClient().create(StoreLocationApiService.class);
        apiService.getAllStoreLocations().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    storeLocationList.clear();
                    storeLocationList.addAll(response.body().getData());
                    storeAdapter.notifyDataSetChanged();
                    Log.d("API", "Got " + storeLocationList.size() + " stores");
                    for (StoreLocation storeLocation : storeLocationList) {
                        LatLng pos = new LatLng(storeLocation.getLatitude(), storeLocation.getLongitude());
                        myMap.addMarker(new MarkerOptions()
                                .position(pos)
                                .title("Store")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    }

                    myMap.setOnMarkerClickListener(marker -> {
                        LatLng destination = marker.getPosition();
                        if (!destination.equals(currentLatLng)) {
                            showRoute(currentLatLng, destination);
                        }
                        return false;
                    });
                }
                else {
                    Toast.makeText(MapActivity.this, "Can not get store locations", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(MapActivity.this, "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API", "Network error: " + t.getMessage(), t);
            }
        });
    }

    private void showRoute(LatLng currentLatLng, LatLng destination) {
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));
        myMap.addPolyline(new PolylineOptions()
                .add(currentLatLng, destination)
                .width(15)
                .color(0xFF2196F3)
        );
//        drawRouteWithRetrofit(currentLatLng, storeLatlng);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onMapReady(myMap);
        } else {
            Toast.makeText(this, "Need permission to display the map", Toast.LENGTH_SHORT).show();
        }
    }

//    private void drawRouteWithRetrofit(LatLng origin, LatLng destination) {
//        String originStr = origin.latitude + "," + origin.longitude;
//        String destinationStr = destination.latitude + "," + destination.longitude;
//        String apiKey = getString(R.string.my_map_api_key);
//
//        DirectionsApiService service = RetrofitClient.getClient().create(DirectionsApiService.class);
//        Call<DirectionsResponse> call = service.getDirections(originStr, destinationStr, "driving", apiKey);
//
//        call.enqueue(new Callback<DirectionsResponse>() {
//            @Override
//            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<DirectionsResponse.Route> routes = response.body().routes;
//                    if (!routes.isEmpty()) {
//                        String encodedPolyline = routes.get(0).overviewPolyline.points;
//                        List<LatLng> path = decodePolyline(encodedPolyline);
//                        myMap.addPolyline(new PolylineOptions()
//                                .addAll(path)
//                                .width(10)
//                                .color(0xFF0D47A1));
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
//                Toast.makeText(MapActivity.this, "Failed to get directions", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0) ? ~(result >> 1) : (result >> 1);
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0) ? ~(result >> 1) : (result >> 1);
            lng += dlng;

            poly.add(new LatLng(lat / 1E5, lng / 1E5));
        }

        return poly;
    }
}