package at.fhjoanneum.eatitall.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;

import java.util.List;

import at.fhjoanneum.eatitall.R;
import at.fhjoanneum.eatitall.model.Meal;

public class TestActivity extends AppCompatActivity {

    private List<Meal> myMeals;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        AndroidNetworking.initialize(getApplicationContext());

        TextView textView = findViewById(R.id.test_textView);

//        AndroidNetworking.get("https://www.themealdb.com/api/json/v1/1/filter.php")
        AndroidNetworking.get("https://www.themealdb.com/api/json/v1/1/filter.php")
                .addQueryParameter("i", "pork")
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(Meal.class, new ParsedRequestListener<List<Meal>>() {
                    @Override
                    public void onResponse(List<Meal> meals) {
                        myMeals = meals;
                        textView.setText("success");
                    }

                    @Override
                    public void onError(ANError anError) {
                        textView.setText(anError.getMessage());
                    }
                });
    }
}
