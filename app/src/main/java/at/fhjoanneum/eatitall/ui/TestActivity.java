package at.fhjoanneum.eatitall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.ArrayList;
import java.util.List;

import at.fhjoanneum.eatitall.R;
import at.fhjoanneum.eatitall.model.Meal;
import at.fhjoanneum.eatitall.model.MealContainer;
import at.fhjoanneum.eatitall.ui.adapter.MealAdapter;

public class TestActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    RecyclerView recyclerView;
    MealAdapter mealAdapter;
    List<Meal> myMeals;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        AndroidNetworking.initialize(getApplicationContext());

        textView = findViewById(R.id.test_textView);
        editText = findViewById(R.id.test_editText);
        recyclerView = findViewById(R.id.test_recycler);

        myMeals = new ArrayList<>();
        myMeals.add(new Meal("Hendl Klug", "www.hendl.at", "123"));
        myMeals.add(new Meal("Hendl Klug", "www.hendl.at", "123"));
        myMeals.add(new Meal("Hendl Klug", "www.hendl.at", "123"));
        mealAdapter = new MealAdapter(this, myMeals);
        recyclerView.setAdapter(mealAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void search(View view) {
        String ingredient = editText.getText().toString();

        if (ingredient.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter an ingredient to search for", Toast.LENGTH_SHORT).show();
            return;
        }

        AndroidNetworking.get("https://www.themealdb.com/api/json/v1/1/filter.php")
                .addQueryParameter("i", ingredient)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(MealContainer.class, new ParsedRequestListener<MealContainer>() {
                    @Override
                    public void onResponse(MealContainer mealContainer) {
                        myMeals = mealContainer.getMeals();
                        if (myMeals != null) {
                            textView.setText(myMeals.get(0).toString());
                            Toast.makeText(getApplicationContext(), "recipes found: " + myMeals.size(), Toast.LENGTH_SHORT).show();
                            mealAdapter = new MealAdapter(getApplicationContext(), myMeals);
                            recyclerView.setAdapter(mealAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        } else {
                            Toast.makeText(getApplicationContext(), "no recipes found", Toast.LENGTH_SHORT).show();
                            textView.setText("");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "ERROR: " + anError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
