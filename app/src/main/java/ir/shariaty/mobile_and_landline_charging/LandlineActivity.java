package ir.shariaty.mobile_and_landline_charging;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class LandlineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landline);

        getSupportActionBar().hide();

    }
}