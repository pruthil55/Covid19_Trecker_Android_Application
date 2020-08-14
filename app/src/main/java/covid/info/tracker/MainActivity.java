package covid.info.tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView tvCases, tvRecovered, tvCritical, tvActive, tvTodayCases, tvTotalDeaths, tvTodayDeaths, tvAffectedCountries;
    TextView tvPopulation, tvCasesPerMillion, tvDeathsPerMillion, tvActivePerMillion, tvRecoveredPerMillion, tvCriticalPerMillion, tvTotalTests, tvTestsPerMillion;
    PieChart pieChart;
    ImageView iv_copyright_info;

    public static ProgressDialog progressDialog;

    LinearLayout card_world_stats, card_india_stats;

    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AudienceNetworkAds.initialize(this);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(MainActivity.this);
        showProgressBar();

        tvCases = findViewById(R.id.tvCases);
        tvRecovered = findViewById(R.id.tvRecovered);
        tvCritical = findViewById(R.id.tvCritical);
        tvActive = findViewById(R.id.tvActive);
        tvTodayCases = findViewById(R.id.tvTodayCases);
        tvTotalDeaths = findViewById(R.id.tvTotalDeaths);
        tvTodayDeaths = findViewById(R.id.tvTodayDeaths);
        tvAffectedCountries = findViewById(R.id.tvAffectedCountries);

        tvActivePerMillion = findViewById(R.id.tvActivePerMillion);
        tvPopulation = findViewById(R.id.tvPopulation);
        tvDeathsPerMillion = findViewById(R.id.tvDeathsPerMillion);
        tvTotalTests = findViewById(R.id.tvTotalTests);
        tvTestsPerMillion = findViewById(R.id.tvTestsPerMillion);
        tvRecoveredPerMillion = findViewById(R.id.tvRecoveredPerMillion);
        tvCriticalPerMillion = findViewById(R.id.tvCriticalPerMillion);
        tvCasesPerMillion = findViewById(R.id.tvCasesPerMillion);

        card_india_stats = findViewById(R.id.card_india_stats);
        card_world_stats = findViewById(R.id.card_world_stats);
        iv_copyright_info = findViewById(R.id.iv_copyright_info);

        pieChart = findViewById(R.id.piechart);


        adView = new AdView(this, "260276888410992_260279521744062", AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        adContainer.addView(adView);
        adView.loadAd();


        if (CheckNetwork.isInternetAvailable(MainActivity.this)) //returns true if internet available
        {

            fetchData();

            card_india_stats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, IndiaCorona.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                }
            });

            card_world_stats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, AffectedCountries.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                }
            });

        } else {
            DynamicToast.makeError(this, "Check Internet Connection!").show();
        }

        iv_copyright_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CopyrightInfo.class));
            }
        });

    }

    private void fetchData() {

        String url = "https://corona.lmao.ninja/v2/all/";


        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            tvCases.setText(jsonObject.getString("cases"));
                            tvRecovered.setText(jsonObject.getString("recovered"));
                            tvCritical.setText(jsonObject.getString("critical"));
                            tvActive.setText(jsonObject.getString("active"));
                            tvTodayCases.setText(jsonObject.getString("todayCases"));
                            tvTotalDeaths.setText(jsonObject.getString("deaths"));
                            tvTodayDeaths.setText(jsonObject.getString("todayDeaths"));
                            tvAffectedCountries.setText(jsonObject.getString("affectedCountries"));

                            tvPopulation.setText(jsonObject.getString("population"));
                            tvActivePerMillion.setText(jsonObject.getString("activePerOneMillion"));
                            tvTestsPerMillion.setText(jsonObject.getString("testsPerOneMillion"));
                            tvTotalTests.setText(jsonObject.getString("tests"));
                            tvDeathsPerMillion.setText(jsonObject.getString("deathsPerOneMillion"));
                            tvCriticalPerMillion.setText(jsonObject.getString("criticalPerOneMillion"));
                            tvCasesPerMillion.setText(jsonObject.getString("casesPerOneMillion"));
                            tvRecoveredPerMillion.setText(jsonObject.getString("recoveredPerOneMillion"));

                            MainActivity.progressDialog.dismiss();


                            pieChart.addPieSlice(new PieModel("Cases", Integer.parseInt(tvCases.getText().toString()), Color.parseColor("#FFA726")));
                            pieChart.addPieSlice(new PieModel("Recoverd", Integer.parseInt(tvRecovered.getText().toString()), Color.parseColor("#66BB6A")));
                            pieChart.addPieSlice(new PieModel("Deaths", Integer.parseInt(tvTotalDeaths.getText().toString()), Color.parseColor("#EF5350")));
                            pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(tvActive.getText().toString()), Color.parseColor("#29B6F6")));
                            ;
                            pieChart.startAnimation();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);


    }

    public static void showProgressBar() {
        progressDialog.show();

        progressDialog.setContentView(R.layout.progress_dialog);

        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }

    // added for exit app when user click back button 1 time and give choice yes/no
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Confirm exit..!!");
        alertDialogBuilder.setIcon(R.drawable.ic_exit_app);
        alertDialogBuilder.setMessage("Are you sure want to exit?");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}