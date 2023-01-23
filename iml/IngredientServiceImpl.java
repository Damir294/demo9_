package iml;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class IngredientServiceImpl implements IngredientService {
    public static HashMap<Integer, Ingredient> ingredientMap = new HashMap<>();
    private static int ingredientId = 0;
    private final FileService fileService;

    public IngredientServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @Value("${name.of.ingredients.data.file}")
    private String ingredientDataFileName;

    @PostConstruct
    private void init() {
        String json = fileService.readFromFile(ingredientDataFileName);
        try {
            ingredientMap =
                    new ObjectMapper().readValue(json, new TypeReference<HashMap<Integer, Ingredient>>() {
                    });
            ingredientId=ingredientMap.size();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int addIngredient(Ingredient ingredient) {
        ingredientMap.put(++ingredientId, ingredient);
        fileService.saveToJsonFile(ingredientMap, ingredientDataFileName);
        return ingredientId;
    }

    @Override
    public Ingredient getIngredient(int id) {
        Ingredient ingredient = ingredientMap.get(id);
        return ingredient;
    }

    @Override
    public ArrayList<Ingredient> getAllIngredient() {
        Collection<Ingredient> ingredientCollection = ingredientMap.values();
        ArrayList<Ingredient> ingredientArrayList = new ArrayList<>(ingredientCollection);
        return ingredientArrayList;
    }

    @Override
    public Ingredient editIngredient(int id, Ingredient ingredient) {
        if (ingredientMap.containsKey(id)) {
            ingredientMap.put(id, ingredient);
            fileService.saveToJsonFile(ingredientMap, ingredientDataFileName);
            return ingredient;
        }
        return null;
    }

    @Override
    public boolean deleteIngredient(int id) {
        if (ingredientMap.containsKey(id)) {
            ingredientMap.remove(id);
            fileService.saveToJsonFile(ingredientMap, ingredientDataFileName);
            return true;
        }
        return false;
    }


}