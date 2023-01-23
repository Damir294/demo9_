package src.Controllers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@RestController
@RequestMapping("/files")
@Tag(name = "Передача файлов по HTTP", description = "API для работы с файлами")
public class FilesController {
    @Value("${path.to.data.file}")
    private String dataFilepath;
    @Value("${name.of.recipes.data.file}")
    private String recipeDataFileName;

    @Value("${name.of.ingredients.data.file}")
    private String ingredientDataFileName;

    private final FileService fileService;
    private final RecipeService recipeService;

    public FilesController(FileService fileService, RecipeService recipeService) {
        this.fileService = fileService;
        this.recipeService = recipeService;
    }

    @Operation(
            summary = "Скачать все рецепты в виде json-файла"
    )
    @GetMapping(value = "/recipes/downloadjson")
    public ResponseEntity<InputStreamResource> downloadRecipesFileJson() throws IOException {
        File file = new File(dataFilepath + "/" + recipeDataFileName);
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + recipeDataFileName)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Принимает json-файл с рецептами и заменяет сохраненный на жестком диске файл на новый"
    )
    @PostMapping(value = "/recipes/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadRecipesFileJson(@RequestParam MultipartFile file) {
        try {
            fileService.uploadFile(file, recipeDataFileName);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }

    @Operation(
            summary = "Принимает json-файл с ингредиентами и заменяет сохраненный на жестком диске файл на новый"
    )
    @PostMapping(value = "/ingredients/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadIngredientsFileJson(@RequestParam MultipartFile file) {
        try {
            fileService.uploadFile(file, ingredientDataFileName);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }

    @Operation(
            summary = "Скачать все рецепты в виде файла txt"
    )
    @GetMapping(value = "/recipes/downloadTxt")
    public ResponseEntity<Object> downloadRecipesFile() {
        try {
            Path path = recipeService.createRecipeFile();
            if (Files.size(path) == 0) {
                return ResponseEntity.noContent().build();
            }
            InputStreamResource resource = new InputStreamResource(new FileInputStream(path.toFile()));
            path.toFile().deleteOnExit();
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .contentLength(Files.size(path))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ricipesList.txt\"")
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }

}