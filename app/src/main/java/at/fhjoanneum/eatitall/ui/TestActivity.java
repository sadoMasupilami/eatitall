package at.fhjoanneum.eatitall.ui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import at.fhjoanneum.eatitall.R;
import at.fhjoanneum.eatitall.model.Meal;
import at.fhjoanneum.eatitall.model.MealContainer;
import at.fhjoanneum.eatitall.ui.adapter.MealAdapter;
import at.fhjoanneum.eatitall.ui.fragment.FavouriteFragment;
import at.fhjoanneum.eatitall.ui.fragment.SearchFragment;

public class TestActivity extends AppCompatActivity {

    EditText editText;
    RecyclerView recyclerView;
    MealAdapter mealAdapter;
    List<Meal> myMeals;

    SearchFragment searchFragment;
    FavouriteFragment favouriteFragment;

    private static final String FRAGMENT_SEARCH = "FragmentSearch";
    private static final String FRAGMENT_FAVOURITE = "FragmentFavourite";

    private static final String PREF_KEY_FRAGMENT = "fragment";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMain);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        bottomNavigationView.setOnNavigationItemSelectedListener((menuItem) -> {
            switch (menuItem.getItemId()) {
                case R.id.action_search:
                    prefs.edit().putString(PREF_KEY_FRAGMENT, FRAGMENT_SEARCH).apply();
                    switchToFragment(FRAGMENT_SEARCH);
                    break;
                case R.id.action_favourite:
                    prefs.edit().putString(PREF_KEY_FRAGMENT, FRAGMENT_FAVOURITE).apply();
                    switchToFragment(FRAGMENT_FAVOURITE);
                    break;
                default:
                    break;
            }
            return true;
        });


        AndroidNetworking.initialize(getApplicationContext());

        progressDialog = new ProgressDialog(this);

        searchFragment = new SearchFragment();
        favouriteFragment = new FavouriteFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.flFragmentContainer, searchFragment);
        transaction.add(R.id.flFragmentContainer, favouriteFragment);
        transaction.hide(searchFragment);
        transaction.hide(favouriteFragment);
        transaction.commit();

        String fragmentName = prefs.getString(PREF_KEY_FRAGMENT, FRAGMENT_SEARCH);

        switchToFragment(fragmentName);

//        editText = findViewById(R.id.test_editText);
//        recyclerView = findViewById(R.id.test_recycler);
//
//        myMeals = new ArrayList<>();
//        mealAdapter = new MealAdapter(this, myMeals);
//        recyclerView.setAdapter(mealAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                            Toast.makeText(getApplicationContext(), "recipes found: " + myMeals.size(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "no recipes found", Toast.LENGTH_SHORT).show();
                            myMeals = new ArrayList<>();
                        }
                        mealAdapter = new MealAdapter(view.getContext(), myMeals);
                        recyclerView.setAdapter(mealAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "ERROR: " + anError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void switchToFragment(String fragmentName) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragmentName) {
            case FRAGMENT_SEARCH:
                transaction.hide(favouriteFragment);
                transaction.show(searchFragment);
                break;
            case FRAGMENT_FAVOURITE:
                transaction.hide(searchFragment);
                transaction.show(favouriteFragment);
                break;
            default:
                break;
        }

        transaction.commit();
    }
}
