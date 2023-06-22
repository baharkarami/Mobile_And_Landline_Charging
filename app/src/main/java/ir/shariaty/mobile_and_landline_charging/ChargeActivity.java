package ir.shariaty.mobile_and_landline_charging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
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
    String operatorType;
    String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityChargeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.radioGroupOperator.getCheckedRadioButtonId() == R.id.rdoHamrahAval)
                    operatorType = "1";
                else if(binding.radioGroupOperator.getCheckedRadioButtonId() == R.id.rdoIrancell)
                    operatorType = "2";
                else
                    operatorType = "3";

                if(binding.radioGroupAmount.getCheckedRadioButtonId() == R.id.rdo20000)
                    amount = "20000";
                else if (binding.radioGroupAmount.getCheckedRadioButtonId() == R.id.rdo50000)
                    amount = "50000";
                else if (binding.radioGroupAmount.getCheckedRadioButtonId() == R.id.rdo100000)
                    amount = "100000";
                else if (binding.radioGroupAmount.getCheckedRadioButtonId() == R.id.rdo200000)
                    amount = "200000";
                else if (binding.radioGroupAmount.getCheckedRadioButtonId() == R.id.rdo500000)
                    amount = "500000";

                    mobileNo = binding.txtMobileNo.getText().toString();

                callAPI(mobileNo,operatorType,amount);
            }
        });
    }

    private void callAPI(String mobileNo, String operatorType, String amount) {
        JSONObject object = new JSONObject();

        try {
            object.put("MobileNo", mobileNo);
            object.put("OperatorType",operatorType);
            object.put("AmountPure",amount);
            object.put("mid","0");
        } catch (Exception e) {
            throw new RuntimeException(e);
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
                    assert response.body() != null;
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    url = jsonObject.get("url").toString();
                    load(url);

                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void load(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}