package at.fhjoanneum.eatitall.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.widget.ANImageView;

import java.util.List;

import at.fhjoanneum.eatitall.R;
import at.fhjoanneum.eatitall.model.Meal;
import at.fhjoanneum.eatitall.ui.MealDetail;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    Context context;
    List<Meal> meals;

    public MealAdapter(Context context, List<Meal> meals) {
        this.context = context;
        this.meals = meals;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.meal_item, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        holder.name.setText(meals.get(position).getStrMeal());
        holder.imageView.setDefaultImageResId(R.drawable.food);
        holder.imageView.setImageUrl(meals.get(position).getStrMealThumb());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MealDetail.class);
                intent.putExtra("id", meals.get(position).getIdMeal());
                Intent[] intents = {intent};
                context.startActivities(intents);
            }
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ANImageView imageView;
        CardView cardView;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvMealName);
            imageView = itemView.findViewById(R.id.test_iv);
            cardView = itemView.findViewById(R.id.cvMeal);
        }
    }
}
