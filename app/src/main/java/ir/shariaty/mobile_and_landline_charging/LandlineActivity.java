package ir.shariaty.mobile_and_landline_charging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ir.shariaty.mobile_and_landline_charging.databinding.ActivityLandlineBinding;
import ir.shariaty.mobile_and_landline_charging.databinding.ActivityMainBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LandlineActivity extends AppCompatActivity {

    public static final MediaType JSON
            = MediaType.get("application/json;charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    ActivityLandlineBinding binding;

    String EndTermAmount;
    String MidTermAmount;
    String EndTermPaymentId;
    String MidTermPaymentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLandlineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnInquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    callAPI("{\n" +
                            "    \"FixedLineNumber\": \"" + binding.txtLandlineNo.getText() + "\"\n" +
                            "}");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    private void callAPI(String landlineNo) {
        progress(true);

        RequestBody body = RequestBody.create(JSON, landlineNo);
        Request request = new Request.Builder()
                .url("https://charge.sep.ir/Inquiry/FixedLineBillInquiry")
                .post(body)
                .build();;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    assert response.body() != null;

                    String jsonData = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonData);
                    if (jsonObject.getString("code").equals("-16")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LandlineActivity.this);
                        builder.setMessage("هیچ قبض قابل پرداختی برای این تلفن ثابت استعلام شده ثبت نشده است")
                                .setTitle("قبض یافت نشد!")
                                .setCancelable(true);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        EndTermAmount = jsonObject.getJSONObject("data").getJSONObject("FinalTerm").getString("Amount");
                        MidTermAmount = jsonObject.getJSONObject("data").getJSONObject("MidTerm").getString("Amount");
                        EndTermPaymentId = jsonObject.getJSONObject("data").getJSONObject("FinalTerm").getString("PaymentID");
                        MidTermPaymentId = jsonObject.getJSONObject("data").getJSONObject("MidTerm").getString("PaymentID");

                        load(EndTermAmount, MidTermAmount, EndTermPaymentId, MidTermPaymentId);
                        progress(false);
                    }

                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void progress(boolean inProgress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (inProgress){
                    binding.endTermLayout.setVisibility(View.GONE);
                    binding.midTermLayout.setVisibility(View.GONE);
                    binding.loadingAnimationView.setVisibility(View.VISIBLE);
                }
                else {
                    binding.endTermLayout.setVisibility(View.VISIBLE);
                    binding.midTermLayout.setVisibility(View.VISIBLE);
                    binding.loadingAnimationView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void load(String endTermAmount, String midTermAmount, String endTermPaymentId, String midTermPaymentId) {
        binding.lblEndTermAmount.setText(endTermAmount+" ریال");
        binding.lblMidTermAmount.setText(midTermAmount+" ریال");
        binding.lblEndPaymentId.setText(endTermPaymentId);
        binding.lblMidPaymentId.setText(midTermPaymentId);

    }
}