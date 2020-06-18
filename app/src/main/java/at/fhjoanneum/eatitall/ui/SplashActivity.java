package at.fhjoanneum.eatitall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import at.fhjoanneum.eatitall.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, TestActivity.class);
            startActivity(intent);
        }, 2000);
    }
}
