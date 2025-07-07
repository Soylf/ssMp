package testSaveMp.testSaveMp.server.service.download;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import testSaveMp.testSaveMp.model.Category;
import testSaveMp.testSaveMp.model.Item;
import testSaveMp.testSaveMp.server.repository.CategoryRepository;
import testSaveMp.testSaveMp.server.repository.ItemRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class DownloadServiceImpl implements DownloadService{
    @Value("${item.absolute-path}")
    private String absolutePath;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    public DownloadServiceImpl(ItemRepository repository, ItemRepository itemRepository, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void createItem(MultipartFile multipartFile, String description, String categoryName) throws IOException {
        String path = saveItemLocal(multipartFile);
        Item item = new Item();
        Category category = categoryRepository.findByName(categoryName)
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(categoryName);
                    return categoryRepository.save(newCategory);
                });


        item.setName(multipartFile.getName());
        item.setItemLink(path);
        item.setCategory(category);
        item.setDescription(description);
        itemRepository.save(item);
    }

    @Override
    public List<String> searchCategories(String query) {
        Pageable limit = PageRequest.of(0, 5);
        return categoryRepository.searchCategoryNames(query, limit);
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
