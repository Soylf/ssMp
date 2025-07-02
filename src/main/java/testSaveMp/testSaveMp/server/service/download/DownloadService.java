package testSaveMp.testSaveMp.server.service.download;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DownloadService {
    void createItem(MultipartFile multipartFile) throws IOException;
}
