package example.end_course.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

public class UploadImage {
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
    public static String upload(MultipartFile image, String folder) throws IOException {
        String randomString = UUID.randomUUID().toString();
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get(folder);

        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
        }
        int hashCode = Objects.requireNonNull(image.getOriginalFilename()).hashCode();
        String fileName = hashCode + "." + randomString + image.getOriginalFilename();

        Path file = CURRENT_FOLDER.resolve(staticPath)
                .resolve(imagePath).resolve(fileName);
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(image.getBytes());
        }
        System.out.println(fileName);
        System.out.println(file);
        return imagePath.resolve(fileName).toString();
    }
}
