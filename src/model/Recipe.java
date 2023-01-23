package src.model;



import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import src.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Recipe {
    @NonNull
    private String title;
    private int cookingTime;
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<String> steps = new ArrayList<>();

    public Recipe(@NonNull String title, int cookingTime, List<Ingredient> ingredients, List<String> steps) {
        this.title = title;
        setCookingTime(cookingTime);
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public void setCookingTime(int cookingTime) {
        if (cookingTime < 0) {
            cookingTime = Math.abs(cookingTime);
        }
        this.cookingTime = cookingTime;
    }


}