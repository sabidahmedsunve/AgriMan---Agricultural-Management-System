package com.ppmdev.agriman.fragment;

import static com.ppmdev.agriman.utils.AndroidUtil.setImageFromFirebase;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ppmdev.agriman.R;
import com.ppmdev.agriman.activity.MarketPlacePage;
import com.ppmdev.agriman.utils.AndroidUtil;
import com.ppmdev.agriman.utils.FirebaseUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private TextView btnEnterMarket;
    private TextView tvLocation;
    private TextView tvTemperature;
    private View view;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;

    private Uri weatherImageUri;
    private ImageView weatherBackgroundImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        init();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        requestLocationPermission();

        btnEnterMarket.setOnClickListener(v -> startActivity(new Intent(getContext(), MarketPlacePage.class)));

        return view;
    }

    private void init() {
        btnEnterMarket = view.findViewById(R.id.tv_enterMarket);
        tvLocation = view.findViewById(R.id.tvLocation);
        tvTemperature = view.findViewById(R.id.tvWeather);
        weatherBackgroundImage=view.findViewById(R.id.iv_weather_background_image);
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        // Permission granted
                        getLastLocation();
                    } else {
                        // Permission denied
                       // Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
            if (location != null) {
                fetchWeatherData(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(requireContext(), "Could not retrieve location", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(requireContext(), "Error getting location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchWeatherData(double latitude, double longitude) {
        String apiKey = "0b20cb8d79f9c39a964f9b48713eaa41";
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey + "&units=metric";

        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String locationName = jsonObject.getString("name");
                JSONObject main = jsonObject.getJSONObject("main");
                double temp = main.getDouble("temp");

                JSONArray weatherArray = jsonObject.getJSONArray("weather");
                if (weatherArray.length() > 0) {
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    String weatherMain = weatherObject.getString("main");

                    // Update the UI with the location, temperature, and weatherMain
                    updateUI(locationName, temp, weatherMain);
                }



            } catch (JSONException e) {
                //Toast.makeText(requireContext(), "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            //Toast.makeText(requireContext(), "Error fetching weather data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });




        queue.add(stringRequest);
    }

    private void updateUI(String locationName, double temp, String imageForWeather) {
        tvLocation.setText(locationName);
        tvTemperature.setText(String.format("%.1f°C", temp));

        if (imageForWeather != null && !imageForWeather.isEmpty()) {
            String imageRef = "";
            switch (imageForWeather) {
                case "Clouds":
                    imageRef = "clouds_image.jpg";
                    //AndroidUtil.showToast(getContext(),imageRef);
                    break;
                case "Rain":
                    imageRef = "rain_image.jpg";
                    //AndroidUtil.showToast(getContext(),imageRef);
                    break;
                default:
                    imageRef = "haze_image.jpg";
                    //AndroidUtil.showToast(getContext(),imageRef);
                    break;
            }

            FirebaseUtil.getWeatherImageStorageRef(imageRef).getDownloadUrl()
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri weatherImageUri = task.getResult();
                                if (weatherImageUri != null) {
                                    AndroidUtil.setImageFromFirebase(getContext(),weatherImageUri,weatherBackgroundImage);
                                }
                            }
                        }
                    });
        }
    }
}
