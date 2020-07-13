package sample.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class FileService {

    @Value("${file.upload.folder}")
    private String folder;

    private final RestTemplate restTemplate;

    public FileService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String decideUploadDirectoryCreateIfNotExist() throws IOException {
        createDirectoryIfNotExist(folder);

        return folder;
    }

    public void createDirectoryIfNotExist(String directory) throws IOException {
        try {
            Path path = Path.of(directory);
            Files.createDirectory(path);
        } catch (FileAlreadyExistsException e) {
            // Normaldir. Demek ki hemin directory artiq daha once yaradilib.
        }
    }

    public void downloadFile(String photoUrl) {
        byte[] imageBytes = restTemplate.getForObject(photoUrl, byte[].class);
        try {
            String directoryCreateIfNotExist = decideUploadDirectoryCreateIfNotExist();
            Path path = Paths.get(directoryCreateIfNotExist + File.separator + Thread.currentThread().getId() + new Date().getTime() + ".jpeg");
            Files.write(path, imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
