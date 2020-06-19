package at.fhjoanneum.eatitall.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import at.fhjoanneum.eatitall.ui.SearchActivity;
import at.fhjoanneum.eatitall.ui.adapter.MealAdapter;

//TODO: add implements MealItemClickListener
public class SearchFragment extends Fragment {

    private RecyclerView searchView;
    EditText editText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);

        searchView = view.findViewById(R.id.test_recycler);

        Button btnSearch = (Button) searchView.findViewById(R.id.test_button);
        editText = view.findViewById(R.id.test_editText);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("ingredient", editText.getText().toString() );
                startActivity(intent);
            }
        });

        return view;
    }


}
