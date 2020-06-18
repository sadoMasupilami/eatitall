package at.fhjoanneum.eatitall.model;

public class Meal {
    private String strMeal;
    private String strMealThumb;
    private String idMeal;

    @Override
    public String toString() {
        return "Meal{" +
                "strMeal='" + strMeal + '\'' +
                ", strMealThumb='" + strMealThumb + '\'' +
                ", idMeal='" + idMeal + '\'' +
                '}';
    }
}
