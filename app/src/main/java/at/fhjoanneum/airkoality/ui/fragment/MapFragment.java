package at.fhjoanneum.airkoality.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import at.fhjoanneum.airkoality.R;

public class MapFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        System.out.println("Test");

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("LIFECYCLE", "onPause map");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("LIFECYCLE", "onStop map");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("LIFECYCLE", "onDestroy map");
    }
}
