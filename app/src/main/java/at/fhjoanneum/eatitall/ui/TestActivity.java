package at.fhjoanneum.eatitall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.List;

import at.fhjoanneum.eatitall.R;
import at.fhjoanneum.eatitall.model.Meal;
import at.fhjoanneum.eatitall.model.MealContainer;

public class TestActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        AndroidNetworking.initialize(getApplicationContext());

        textView = findViewById(R.id.test_textView);
        editText = findViewById(R.id.test_editText);
    }

    public void search(View view) {
        String ingredient = editText.getText().toString();

        AndroidNetworking.get("https://www.themealdb.com/api/json/v1/1/filter.php")
                .addQueryParameter("i", ingredient)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(MealContainer.class, new ParsedRequestListener<MealContainer>() {
                    @Override
                    public void onResponse(MealContainer mealContainer) {
                        List<Meal> myMeals = mealContainer.getMeals();
                        if (myMeals != null) {
                            textView.setText(myMeals.get(0).toString());
                            Toast.makeText(getApplicationContext(), "recipes found: " + myMeals.size(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "no recipes found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "ERROR: " + anError.getMessage(), Toast.LENGTH_LONG).show();
                        textView.setText("0");
                    }
                });
    }
}
