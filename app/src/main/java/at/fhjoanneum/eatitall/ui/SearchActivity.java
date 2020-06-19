package at.fhjoanneum.eatitall.ui;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import at.fhjoanneum.eatitall.R;
import at.fhjoanneum.eatitall.model.Meal;
import at.fhjoanneum.eatitall.model.MealContainer;
import at.fhjoanneum.eatitall.ui.adapter.MealAdapter;

public class SearchActivity extends AppCompatActivity {

    EditText editText;
    RecyclerView recyclerView;
    MealAdapter mealAdapter;
    List<Meal> myMeals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_list);
        View view = findViewById(R.id.searchView);
        //BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMain);
        recyclerView = findViewById(R.id.test_recycler);
        myMeals = new ArrayList<>();
        mealAdapter = new MealAdapter(view.getContext(), myMeals);
        recyclerView.setAdapter(mealAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String ingredient = "";


        Bundle bundle = getIntent().getExtras();
        if ( bundle != null)
        {
           ingredient =  bundle.getString("ingredient");
        }
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
                            Toast.makeText(getApplicationContext(), "recipes found: " + myMeals.size(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "no recipes found", Toast.LENGTH_SHORT).show();
                            myMeals = new ArrayList<>();
                        }
                        mealAdapter = new MealAdapter(SearchActivity.this, myMeals);
                        recyclerView.setAdapter(mealAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "ERROR: " + anError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


    }
}