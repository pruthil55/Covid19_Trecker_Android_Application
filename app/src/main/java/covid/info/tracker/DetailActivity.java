package covid.info.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.leo.simplearcloader.SimpleArcLoader;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import java.text.DecimalFormat;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private int positionCountry;
    TextView tvCountry, tvCases, tvRecovered, tvCritical, tvActive, tvTodayCases, tvTotalDeaths, tvTodayDeaths;

    SimpleArcLoader arcLoader;

//    InMobiBanner bannerAd;
    AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AudienceNetworkAds.initialize(this);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        positionCountry = intent.getIntExtra("position", 0);

        getSupportActionBar().setTitle("Details of " + AffectedCountries.countryModelsList.get(positionCountry).getCountry());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        tvCountry = findViewById(R.id.tvCountry);
        tvCases = findViewById(R.id.tvCases);
        tvRecovered = findViewById(R.id.tvRecovered);
        tvCritical = findViewById(R.id.tvCritical);
        tvActive = findViewById(R.id.tvActive);
        tvTodayCases = findViewById(R.id.tvTodayCases);
        tvTotalDeaths = findViewById(R.id.tvTotalDeaths);
        tvTodayDeaths = findViewById(R.id.tvTodayDeaths);
        arcLoader = findViewById(R.id.arcLoader);

        if (CheckNetwork.isInternetAvailable(DetailActivity.this)) //returns true if internet available
        {

            adView = new AdView(this, "260276888410992_260283195077028", AdSize.BANNER_HEIGHT_50);
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
            adContainer.addView(adView);
            adView.loadAd();


            tvCountry.setText(AffectedCountries.countryModelsList.get(positionCountry).getCountry());
            tvCases.setText(AffectedCountries.countryModelsList.get(positionCountry).getCases());
            tvRecovered.setText(AffectedCountries.countryModelsList.get(positionCountry).getRecovered());
            tvCritical.setText(AffectedCountries.countryModelsList.get(positionCountry).getCritical());
            tvActive.setText(AffectedCountries.countryModelsList.get(positionCountry).getActive());
            tvTodayCases.setText(AffectedCountries.countryModelsList.get(positionCountry).getTodayCases());
            tvTotalDeaths.setText(AffectedCountries.countryModelsList.get(positionCountry).getDeaths());
            tvTodayDeaths.setText(AffectedCountries.countryModelsList.get(positionCountry).getTodayDeaths());

            arcLoader.setVisibility(View.GONE);

            AnimatedPieView mAnimatedPieView = findViewById(R.id.animatedPieView);
            AnimatedPieViewConfig config = new AnimatedPieViewConfig();

            String active = tvActive.getText().toString();
            String recovered = tvRecovered.getText().toString();
            String totalDeaths = tvTotalDeaths.getText().toString();
            String cases = tvCases.getText().toString();

            if (active == null || active.equals(null) || active.equals("null") || active.isEmpty())
                active = "0";
            else if (recovered == null || recovered.equals(null) || recovered.equals("null") || recovered.isEmpty())
                recovered = "0";
            else if (totalDeaths == null || totalDeaths.equals(null) || totalDeaths.equals("null") || totalDeaths.isEmpty())
                totalDeaths = "0";
            else if (cases == null || cases.equals(null) || cases.equals("null") || cases.isEmpty())
                cases = "0";

            config.startAngle(-90)
                    .addData(new SimplePieInfo(Integer.parseInt(active), ContextCompat.getColor(this, R.color.countryActive), String.valueOf(Float.valueOf(new DecimalFormat("##.##").format((Float.parseFloat(active) / Float.parseFloat(cases)) * 100)) + "%")))
                    .addData(new SimplePieInfo(Integer.parseInt(recovered), ContextCompat.getColor(this, R.color.countryRecovered), String.valueOf(Float.valueOf(new DecimalFormat("##.##").format((Float.parseFloat(recovered) / Float.parseFloat(cases)) * 100)) + "%")))
                    .addData(new SimplePieInfo(Integer.parseInt(totalDeaths), ContextCompat.getColor(this, R.color.countryDeath), String.valueOf(Float.valueOf(new DecimalFormat("##.##").format((Float.parseFloat(totalDeaths) / Float.parseFloat(cases)) * 100)) + "%")))
                    .drawText(true)
                    .autoSize(true)
                    .textSize(30)
                    .textGravity(AnimatedPieViewConfig.ABOVE)
                    .focusAlpha(150)
                    .focusAlphaType(AnimatedPieViewConfig.FOCUS_WITH_ALPHA_REV)
                    .strokeMode(true)
                    .splitAngle(1.5f)
                    .duration(2000)
                    .canTouch(true)
                    .strokeMode(false);

            mAnimatedPieView.applyConfig(config);
            mAnimatedPieView.start();


        } else {
            DynamicToast.makeError(this, "Check Internet Connection!").show();
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DetailActivity.this, AffectedCountries.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
