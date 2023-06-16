package ir.shariaty.mobile_and_landline_charging;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ir.shariaty.mobile_and_landline_charging.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}