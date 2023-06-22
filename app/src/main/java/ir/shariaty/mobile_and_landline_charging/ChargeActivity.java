package ir.shariaty.mobile_and_landline_charging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import ir.shariaty.mobile_and_landline_charging.databinding.ActivityChargeBinding;
import ir.shariaty.mobile_and_landline_charging.databinding.ActivityMainBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChargeActivity extends AppCompatActivity {

    ActivityChargeBinding binding;

    public static final MediaType JSON
            = MediaType.get("application/json;charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    String url;
    String mobileNo;
    Integer operatorType;
    Integer amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityChargeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.rdoHamrahAval.isChecked())
                    operatorType = 1;
                else if (binding.rdoIrancell.isChecked())
                    operatorType = 2;
                else if (binding.rdoRightel.isChecked())
                    operatorType = 3;

                if (binding.rdo20000.isChecked())
                    amount = 20000;
                else if(binding.rdo50000.isChecked())
                    amount = 50000;
                else if(binding.rdo100000.isChecked())
                    amount = 100000;
                else if(binding.rdo200000.isChecked())
                    amount = 200000;
                else if(binding.rdo500000.isChecked())
                    amount = 500000;

                mobileNo = binding.txtMobileNo.getText().toString();

                callAPI(mobileNo,operatorType,amount);
            }
        });
    }

    private void callAPI(String mobileNo, Integer operatorType, Integer amount) {
        JSONObject object = new JSONObject();

        try {
            object.put("MobileNo", "09024471819");
            object.put("OperatorType",2);
            object.put("AmountPure",20000);
            object.put("mid","0");
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        RequestBody requestBody = RequestBody.create(object.toString(), JSON);
        Request request = new Request.Builder().url("https://topup.pec.ir/")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    url = jsonObject.getString("url");

                    load(url);

                } catch (Exception e) {
                    String error = e.getMessage();
                }
            }
        });
    }

    private void load(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

}