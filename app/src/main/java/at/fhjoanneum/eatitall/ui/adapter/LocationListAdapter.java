package at.fhjoanneum.eatitall.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.fhjoanneum.eatitall.R;
import at.fhjoanneum.eatitall.model.Location;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.LocationViewHolder> {

    private List<Location> locations;
    private LocationItemClickListener itemClickListener;

    public LocationListAdapter(List<Location> locations, LocationItemClickListener listener) {
        this.locations = locations;
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list_item, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        holder.bindItem(locations.get(position));
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLocation;
        private TextView tvCity;
        private TextView tvCountry;

        private View rootView;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            rootView = itemView.findViewById(R.id.cvLocation);

        }

        public void bindItem(Location location) {
            tvLocation.setText(location.getLocation());
            tvCountry.setText(location.getCountry());
            tvCity.setText(location.getCity());

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onLocationItemClicked(location);
                }
            });
        }
    }

    public interface LocationItemClickListener {
        void onLocationItemClicked(Location location);
    }
}
