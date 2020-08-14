package covid.info.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IndiaCorona extends AppCompatActivity {

    TextView tv_state, tv_confirmed, tv_active, tv_deceased, tv_recovered, tv_lastUpdated;
    TextView tvTotalConfirmed , tvTotalActive ,tvTotalRecovered , tvTotalDeceased;

    IndiaStatesData indiaStatesData;
    IndiaStatesDataAdapter indiaStatesDataAdapter;
    ListView list_view;

    public static List<IndiaStatesData> statesList = new ArrayList<>();

    RequestQueue mQueue;

    SwipeRefreshLayout swipeToRefresh;

    public String lastUpdatdeDate = "";

    public static ProgressDialog progressDialog;

    private InterstitialAd interstitialAd;
    public final String TAG = "AlternativeApps";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AudienceNetworkAds.initialize(this);
//        InMobiSdk.init(this, "a05360fad3384d5bbb3badf15d2832b1");
        setContentView(R.layout.activity_india_corona);

        tv_state = findViewById(R.id.tv_state);
        tv_confirmed = findViewById(R.id.tv_confirmed);
        tv_active = findViewById(R.id.tv_active);
        tv_deceased = findViewById(R.id.tv_deceased);
        tv_recovered = findViewById(R.id.tv_recovered);
        tv_lastUpdated = findViewById(R.id.tv_lastUpdated);
        list_view = findViewById(R.id.list_view);

        tvTotalActive = findViewById(R.id.tvTotalActive);
        tvTotalConfirmed = findViewById(R.id.tvTotalConfirmed);
        tvTotalRecovered = findViewById(R.id.tvTotalRecovered);
        tvTotalDeceased = findViewById(R.id.tvTotalDeceased);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);

        if (CheckNetwork.isInternetAvailable(IndiaCorona.this)) //returns true if internet available
        {

            interstitialAd = new InterstitialAd(this, "260276888410992_260285521743462");
            interstitialAd.setAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    // Interstitial ad displayed callback
                    Log.e(TAG, "Interstitial ad displayed.");
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    // Interstitial dismissed callback
                    Log.e(TAG, "Interstitial ad dismissed.");
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    // Ad error callback
                    Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Interstitial ad is loaded and ready to be displayed
                    Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                    // Show the ad
                    interstitialAd.show();
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Ad clicked callback
                    Log.d(TAG, "Interstitial ad clicked!");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Ad impression logged callback
                    Log.d(TAG, "Interstitial ad impression logged!");
                }
            });



            progressDialog = new ProgressDialog(IndiaCorona.this);
            showProgressBar();

            Animation animation = new RotateAnimation(0.0f, 360.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            animation.setRepeatCount(-1);
            animation.setDuration(2000);

            mQueue = Volley.newRequestQueue(this);
            getStatesData();
            IndiaCorona.progressDialog.dismiss();

            swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getStatesData();
                    swipeToRefresh.setRefreshing(false);

                }
            });

        }
        else {
            DynamicToast.makeError(this, "Check Internet Connection!").show();
            swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeToRefresh.setRefreshing(false);
                    startActivity(new Intent(IndiaCorona.this, IndiaCorona.class));
                }
            });
        }

    }

    public void getStatesData()
    {
        lastUpdatedDateRetrive();
        getTimeAgo();

        String url = "https://api.covid19india.org/data.json";

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            JSONArray jsonArray = response.getJSONArray("statewise");

                            JSONObject totalStatewise = jsonArray.getJSONObject(0);

                            String totalActive = totalStatewise.getString("active");
                            String totalConfirmed = totalStatewise.getString("confirmed");
                            String totalDeaths = totalStatewise.getString("deaths");
                            String totalRecovered = totalStatewise.getString("recovered");

                            String totalDeltaconfirmed = totalStatewise.getString("deltaconfirmed");
                            String totalDeltarecovered = totalStatewise.getString("deltarecovered");
                            String totalDeltadeaths = totalStatewise.getString("deltadeaths");
                            String totalDeltaactive = "0";

                            tvTotalConfirmed.setText(totalConfirmed + "\n" + "↑" + totalDeltaconfirmed);
                            tvTotalActive.setText(totalActive + "\n" + "↑" + totalDeltaactive);
                            tvTotalRecovered.setText(totalRecovered + "\n" + "↑" + totalDeltarecovered);
                            tvTotalDeceased.setText(totalDeaths + "\n" + "↑" + totalDeltadeaths);

                            for (int i=1; i<jsonArray.length(); i++)
                            {
                                JSONObject statewise = jsonArray.getJSONObject(i);

                                String active = statewise.getString("active");
                                String confirmed = statewise.getString("confirmed");
                                String deaths = statewise.getString("deaths");
                                String recovered = statewise.getString("recovered");
                                String state = statewise.getString("state");

                                String deltaconfirmed = statewise.getString("deltaconfirmed");
                                String deltarecovered = statewise.getString("deltarecovered");
                                String deltadeaths = statewise.getString("deltadeaths");
                                String deltaactive = "0";

                                active = active + "\n" + "↑" + deltaactive;
                                recovered = recovered + "\n" + "↑" + deltarecovered;
                                deaths = deaths + "\n" + "↑" + deltadeaths;
                                confirmed = confirmed + "\n" + "↑" + deltaconfirmed;

                                indiaStatesData = new IndiaStatesData(recovered, active, confirmed, state, deaths);
                                statesList.add(indiaStatesData);
                            }

                            indiaStatesDataAdapter = new IndiaStatesDataAdapter(IndiaCorona.this, statesList);
                            list_view.setAdapter(indiaStatesDataAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    public void getTimeAgo()
    {
        Date lastUpdatdeDateConverted, currentDateConverted;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        String currentDateString = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        try {
            lastUpdatdeDateConverted = format.parse(lastUpdatdeDate);
            currentDateConverted = format.parse(currentDateString);

            long diff = currentDateConverted.getTime() - lastUpdatdeDateConverted.getTime();

            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (seconds < 60)
                tv_lastUpdated.setText("Last Updated"+"\n"+"Few seconds ago");
            else if (minutes < 60)
                tv_lastUpdated.setText("Last Updated"+"\n"+String.valueOf(minutes)+" minutes ago");
            else if (hours < 24)
                tv_lastUpdated.setText("Last Updated"+"\n"+String.valueOf(hours)+" hours ago");
            else
                tv_lastUpdated.setText("Last Updated"+"\n"+lastUpdatdeDate);


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void lastUpdatedDateRetrive()
    {
        String url = "https://api.covid19india.org/data.json";

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("statewise");

                            for (int i=0; i<1; i++)
                            {
                                JSONObject statewise = jsonArray.getJSONObject(i);

                                lastUpdatdeDate = statewise.getString("lastupdatedtime");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        interstitialAd.loadAd();
        startActivity(new Intent(IndiaCorona.this, MainActivity.class));
        finish();
    }

}
