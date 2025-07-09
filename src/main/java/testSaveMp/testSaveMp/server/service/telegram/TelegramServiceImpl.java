package testSaveMp.testSaveMp.server.service.telegram;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;
import testSaveMp.testSaveMp.model.Item;
import testSaveMp.testSaveMp.model.UserModel;
import testSaveMp.testSaveMp.server.repository.CategoryRepository;
import testSaveMp.testSaveMp.server.repository.ItemRepository;
import testSaveMp.testSaveMp.server.repository.UserRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class TelegramServiceImpl implements TelegramService{
    private final UserRepository userRep;
    private final ItemRepository itemRep;
    private final CategoryRepository categoryRep;

    @Override
    public boolean checkUserId(Long userId) {
        return userRep.existsByUserId(userId);
    }

    @Override
    public void saveUser(User user) {
        UserModel userModel = new UserModel();
        userModel.setUserId(user.getId());
        userModel.setName(user.getFirstName());
        userRep.save(userModel);
    }

    @Override
    public List<String> getCategories() {
        return categoryRep.findAllName();
    }

    @Override
    public List<File> getItemFiles(String message, String category) {
        Pageable limit = PageRequest.of(0, 5);
        List<String> items = itemRep.findItemLinksByMessageAndCategory(message, category, limit);
        return items.stream()
                .map(File::new)
                .collect(Collectors.toList());
    }
}
