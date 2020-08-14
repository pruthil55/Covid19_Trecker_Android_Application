package covid.info.tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CopyrightInfo extends AppCompatActivity {

    RelativeLayout ralative_india_data, ralative_global_data, ralative_privacy_policy, ralative_copyright_agreement;
    TextView tv_back_to_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copyright_info);

        ralative_india_data = findViewById(R.id.ralative_india_data);
        ralative_global_data = findViewById(R.id.ralative_global_data);
        ralative_privacy_policy = findViewById(R.id.ralative_privacy_policy);
        ralative_copyright_agreement = findViewById(R.id.ralative_copyright_agreement);
        tv_back_to_home = findViewById(R.id.tv_back_to_home);

        ralative_india_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.covid19india.org/"));
                startActivity(i);
            }
        });

        ralative_global_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse("https://corona.lmao.ninja/"));
                startActivity(i);
            }
        });

        ralative_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse("https://bit.ly/2XaQ1LS"));
                startActivity(i);
            }
        });

        ralative_copyright_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse("https://bit.ly/3bPCNJU"));
                startActivity(i);
            }
        });

        tv_back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CopyrightInfo.this, MainActivity.class));
                finish();
            }
        });

    }
}
