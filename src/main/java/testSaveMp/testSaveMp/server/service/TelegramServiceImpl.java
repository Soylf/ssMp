package testSaveMp.testSaveMp.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;
import testSaveMp.testSaveMp.model.AudioModel;
import testSaveMp.testSaveMp.model.UserModel;
import testSaveMp.testSaveMp.server.repository.AudioModelRepository;
import testSaveMp.testSaveMp.server.repository.UserRepository;

@Service
@AllArgsConstructor
@Transactional
public class TelegramServiceImpl implements TelegramService{
    private String absolutePath = "C:/Users/Soyl/Documents/AudionModelP";
    private final AudioModelRepository audioModelRep;
    private final UserRepository userRep;

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
    public void saveAudio(String fileName, String absolutePath) {
        System.out.println(fileName + "" + absolutePath);
        AudioModel audioModel = new AudioModel();
        audioModel.setAudioLink(absolutePath);
        audioModel.setName(fileName);
        audioModelRep.save(audioModel);
    }
}
