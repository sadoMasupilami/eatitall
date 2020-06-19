package at.fhjoanneum.eatitall.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import at.fhjoanneum.eatitall.R;


public class MealDetail extends AppCompatActivity {

    TextView name;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);
        name = findViewById(R.id.detail_name);
        getData();
        setData();
    }

    private void getData() {
        if (getIntent().hasExtra("id")) {
            id = getIntent().getStringExtra("id");
        } else {
            Toast.makeText(this, "ERROR: no ID given", Toast.LENGTH_LONG).show();
        }
    }

    private void setData() {
        name.setText(id);
    }
}