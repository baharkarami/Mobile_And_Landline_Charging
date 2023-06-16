package ir.shariaty.mobile_and_landline_charging;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ir.shariaty.mobile_and_landline_charging.databinding.ActivityLandlineBinding;
import ir.shariaty.mobile_and_landline_charging.databinding.ActivityMainBinding;

public class LandlineActivity extends AppCompatActivity {

    ActivityLandlineBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityLandlineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}