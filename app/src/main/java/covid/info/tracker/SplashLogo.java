package covid.info.tracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashLogo extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2100;
    //Hooks
    View first,second,third,fourth,fifth,sixth;
    TextView tv_application_name;
    ImageView iv_logo;
    //Animations
    Animation topAnimantion,bottomAnimation,middleAnimation,bounce;

    public static String TAG = "SplashLogo";

//    public static JSONObject consentObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_logo);

        // Hooks
        first = findViewById(R.id.first_line);
        second = findViewById(R.id.second_line);
        third = findViewById(R.id.third_line);
        fourth = findViewById(R.id.fourth_line);
        fifth = findViewById(R.id.fifth_line);
        sixth = findViewById(R.id.sixth_line);
        iv_logo = findViewById(R.id.iv_logo);
        tv_application_name = findViewById(R.id.tv_application_name);




//        consentObject = new JSONObject();
//        try {
//            // Provide correct consent value to sdk which is obtained by User
//            consentObject.put(InMobiSdk.IM_GDPR_CONSENT_AVAILABLE, true);
//            // Provide 0 if GDPR is not applicable and 1 if applicable
//            consentObject.put("gdpr", "0");
//            // Provide user consent in IAB format
//            consentObject.put(InMobiSdk.IM_GDPR_CONSENT_IAB, false);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        InMobiSdk.init(this, "a05360fad3384d5bbb3badf15d2832b1", consentObject, new SdkInitializationListener() {
//            @Override
//            public void onInitializationComplete(@Nullable Error error) {
//                if (null != error) {
//                    Log.e(TAG, "InMobi Init failed -" + error.getMessage());
//                } else {
//                    Log.d(TAG, "InMobi Init Successful");
//                }
//            }
//        });
//        InMobiSdk.updateGDPRConsent(consentObject);








        //Animation Calls
        topAnimantion = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_animation);
        bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);

        //-----------Setting Animations to the elements of SplashScreen--------

        first.setAnimation(topAnimantion);
        second.setAnimation(topAnimantion);
        third.setAnimation(topAnimantion);
        fourth.setAnimation(topAnimantion);
        fifth.setAnimation(topAnimantion);
        sixth.setAnimation(topAnimantion);
        iv_logo.setAnimation(bounce);
        tv_application_name.setAnimation(bottomAnimation);

        //Splash Screen Code to call new Activity after some time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashLogo.this, LoadActivity.class);
                startActivity(intent);
                finish();

            }
        }, SPLASH_TIME_OUT);

    }

}
