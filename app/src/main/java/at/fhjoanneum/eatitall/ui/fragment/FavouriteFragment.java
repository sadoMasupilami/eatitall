package at.fhjoanneum.eatitall.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import at.fhjoanneum.eatitall.R;

public class FavouriteFragment extends Fragment  {

    private RecyclerView favView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite_list, container, false);

        favView = view.findViewById(R.id.test_recycler);

        return view;
    }

    //TODO: update und onMealItemCliceked()
}
