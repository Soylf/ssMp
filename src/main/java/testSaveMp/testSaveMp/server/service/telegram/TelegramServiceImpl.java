package testSaveMp.testSaveMp.server.service.telegram;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;
import testSaveMp.testSaveMp.model.UserModel;
import testSaveMp.testSaveMp.server.repository.CategoryRepository;
import testSaveMp.testSaveMp.server.repository.UserRepository;

@Service
@AllArgsConstructor
@Transactional
public class TelegramServiceImpl implements TelegramService{
    private final UserRepository userRep;
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
    public String getCategories() {
        return categoryRep.findAllName().toString();
    }


}
