package src.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RequestMapping("/recipe")
@RestController
@Tag(name = "Рецепты", description = "API для работы с рецептами")
public class RecipeController {

    private final RecipeService recipeService;
    private final ValidateService validateService;

    public RecipeController(RecipeService recipeService, ValidateService validateService) {
        this.recipeService = recipeService;
        this.validateService = validateService;
    }

    @PostMapping
    @Operation(
            summary = "Добавление рецепта"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Рецепт добавлен"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные параметры рецепта"
                    )
            }
    )
    public ResponseEntity<Integer> addRecipe(@RequestBody Recipe recipe) {
        if (validateService.isNotValid(recipe)) {
            return ResponseEntity.badRequest().build();
        }
        int id = recipeService.addRecipe(recipe);
        return ResponseEntity.ok().body(id);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получение рецепта по id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Рецепт найден"

                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Рецепт с таким id не найден"
                    )
            }
    )
    public ResponseEntity<Recipe> getRecipe(@PathVariable int id) {
        Recipe recipe = recipeService.getRecipe(id);
        if (recipe != null) {
            return ResponseEntity.ok().body(recipe);
        } else return ResponseEntity.notFound().build();
    }

    @GetMapping
    @Operation(
            summary = "Получение списка всех рецептов"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список рецептов получен"

                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "Не одного рецепта ни найдено"
                    )
            }
    )
    public ResponseEntity<ArrayList<Recipe>> getAllRecipe() {
        ArrayList<Recipe> allRecipes = recipeService.getAllRecipe();
        if (allRecipes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allRecipes);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Редактирование рецепта по id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Рецепт отредактирован"

                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Рецепт с таким id не найден"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные параметры рецепта"
                    )
            }
    )
    public ResponseEntity<Recipe> editRecipe(@PathVariable int id, @RequestBody Recipe recipe) {
        if (validateService.isNotValid(recipe)) {
            return ResponseEntity.badRequest().build();
        }
        recipe = recipeService.editRecipe(id, recipe);
        if (recipe != null) {
            return ResponseEntity.ok().body(recipe);
        } else return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление рецепта по id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Рецепт удалён"

                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Рецепт с таким id не найден"
                    )
            }
    )
    public ResponseEntity<Void> deleteRecipe(@PathVariable int id) {
        if (recipeService.deleteRecipe(id)) {
            return ResponseEntity.ok().build();
        } else return ResponseEntity.notFound().build();
    }

    @GetMapping("/ingredient/{id}")
    @Operation(
            summary = "Поиск рецептов по id ингредиента"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Рецепты найдены"

                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Рецепты с таким id ингредиента не найдены"
                    )
            }
    )
    public ResponseEntity<ArrayList<Recipe>> getRecipeByIngredient(@PathVariable int id) {
        ArrayList<Recipe> recipeWithIngredient = recipeService.getRecipeByIngredient(id);
        if (recipeWithIngredient.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else return ResponseEntity.ok().body(recipeWithIngredient);
    }

    @PutMapping("/ingredients")
    @Operation(
            summary = "Поиск рецептов по нескольким названиям ингредиентов"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Рецепты найдены"

                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Рецепты с такими ингредиентами не найдены"
                    )
            }
    )
    public ResponseEntity<ArrayList<Recipe>> getRecipeByIngredientList
            (@RequestBody ArrayList<String> ingredients) {
        ArrayList<Recipe> recipeWithIngredients = recipeService.getRecipeByIngredientList(ingredients);
        if (recipeWithIngredients.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else return ResponseEntity.ok().body(recipeWithIngredients);
    }

}