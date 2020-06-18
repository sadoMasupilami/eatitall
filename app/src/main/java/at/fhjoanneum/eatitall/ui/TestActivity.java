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
import at.fhjoanneum.eatitall.model.MealContainer;

public class TestActivity extends AppCompatActivity {

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
                .getAsObject(MealContainer.class, new ParsedRequestListener<MealContainer>() {
                    @Override
                    public void onResponse(MealContainer mealContainer) {
                        List<Meal> myMeals = mealContainer.getMeals();
                        textView.setText(myMeals.get(1).toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        textView.setText("0");
                    }
                });
    }
}
