package com.example.productsaleprm.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.productsaleprm.databinding.ActivityMapBinding;
import com.example.productsaleprm.R;
import com.example.productsaleprm.adapter.StoreAdapter;
import com.example.productsaleprm.model.StoreLocation;
import com.example.productsaleprm.model.User;
import com.example.productsaleprm.model.response.BaseResponse;
import com.example.productsaleprm.retrofit.RetrofitClient;
import com.example.productsaleprm.retrofit.StoreLocationAPI;
import com.example.productsaleprm.retrofit.UserAPI;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private ActivityMapBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap myMap;
    private final List<StoreLocation> storeLocationList = new ArrayList<>();
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
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        binding.btnMyLocation.setOnClickListener(v -> {
            if (currentLatLng != null && myMap != null) {
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));
            } else {
                Toast.makeText(this, "Can not get my location!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnToggleList.setOnClickListener(v -> {
            if (isStoreListVisible) {
                binding.rvStores.animate()
                        .translationY(binding.rvStores.getHeight())
                        .alpha(0f)
                        .setDuration(300)
                        .start();

                binding.btnToggleList.animate()
                        .translationY(binding.btnToggleList.getHeight() / 10)
                        .rotation(180f)
                        .setDuration(300)
                        .withEndAction(() -> {
                            binding.rvStores.setVisibility(View.GONE);
                        })
                        .start();
                binding.btnToggleList.setImageResource(R.drawable.ic_arrow_drop_up);
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

                binding.btnToggleList.setImageResource(R.drawable.ic_arrow_drop_down);
            }
            isStoreListVisible = !isStoreListVisible;
        });

        storeAdapter = new StoreAdapter(storeLocationList, store -> {
            LatLng destination = new LatLng(store.getLatitude(), store.getLongitude());
            if (currentLatLng != null) {
                showRoute(currentLatLng, destination);
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination, 15));
            }
        });

        binding.rvStores.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvStores.setAdapter(storeAdapter);

        binding.btnBackContainer.setOnClickListener(v -> finish());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        fetchStoreLocations();

        RetrofitClient.getClient(this).create(UserAPI.class)
                .getCurrentUser()
                .enqueue(new Callback<BaseResponse<User>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            User currentUser = response.body().getData();
                            if (currentUser != null && currentUser.getAddress() != null) {
                                resolveAddressToLatLng(currentUser.getAddress());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                        Toast.makeText(MapActivity.this, "Get current user error", Toast.LENGTH_SHORT).show();
                        Log.e("UserAPI", "Error: " + t.getMessage(), t);
                    }
                });

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
    }

    private void fetchStoreLocations() {
        StoreLocationAPI apiService =
                RetrofitClient.getClient(this).create(StoreLocationAPI.class);

        Call<BaseResponse<List<StoreLocation>>> call = apiService.getAllStoreLocations();
        call.enqueue(new Callback<BaseResponse<List<StoreLocation>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<StoreLocation>>> call, Response<BaseResponse<List<StoreLocation>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    storeLocationList.clear();
                    storeLocationList.addAll(response.body().getData());
                    storeAdapter.notifyDataSetChanged();
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
                } else {
                    Toast.makeText(MapActivity.this, "Can not get store locations", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<StoreLocation>>> call, Throwable t) {
                Toast.makeText(MapActivity.this, "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API", "Network error: " + t.getMessage(), t);
            }
        });
    }

    private void resolveAddressToLatLng(String address) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        new Thread(() -> {
            try {
                List<Address> addresses = geocoder.getFromLocationName(address, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address addr = addresses.get(0);
                    LatLng latLng = new LatLng(addr.getLatitude(), addr.getLongitude());
                    runOnUiThread(() -> {
                        currentLatLng = latLng;
                        myMap.addMarker(new MarkerOptions().position(currentLatLng).title("My Location"));
                        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));
                    });
                } else {
                    runOnUiThread(() -> Log.d("MapActivity", "resolveAddressToLatLng: Failed to resolve user address"));
                }
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Log.d("MapActivity", "resolveAddressToLatLng: " + e.getMessage()));
            }
        }).start();
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
