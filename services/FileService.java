package services;


import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface FileService {


    void saveToJsonFile(Object object, String fileName);

    String readFromFile(String fileName);


    void uploadFile(MultipartFile file, String filename) throws IOException;

    Path CreateTempFile(String suffix);
}