package testSaveMp.testSaveMp.server.service.download;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import testSaveMp.testSaveMp.model.Item;
import testSaveMp.testSaveMp.server.repository.ItemRepository;

import java.io.File;
import java.io.IOException;

@Service
@Transactional
public class DownloadServiceImpl implements DownloadService{
    @Value("${item.absolute-path}")
    private String absolutePath;
    private final ItemRepository repository;

    public DownloadServiceImpl(ItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createItem(MultipartFile multipartFile) throws IOException {
        String path = saveItemLocal(multipartFile);
        Item item = new Item();

        item.setName(multipartFile.getName());
        item.setAudioLink(path);
        repository.save(item);
    }

    private String saveItemLocal(MultipartFile multipartFile) throws IOException {
        String fileName = System.currentTimeMillis() + "-" + multipartFile.getOriginalFilename();
        File destinationFile = new File(absolutePath, fileName);
        File parentDir = destinationFile.getParentFile();
        if (!parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                throw new IOException("Не удалось создать директорию: " + parentDir.getAbsolutePath());
            }
        }
        multipartFile.transferTo(destinationFile);

        return absolutePath + "/" + fileName;
    }
}
