package testSaveMp.testSaveMp.server.service.download;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DownloadService {
    void createItem(MultipartFile multipartFile, String description, String category) throws IOException;
    List<String> searchCategories(String lowerCase);
}
