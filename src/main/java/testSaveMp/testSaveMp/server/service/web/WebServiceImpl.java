package testSaveMp.testSaveMp.server.service.web;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;
import testSaveMp.testSaveMp.model.Item;
import testSaveMp.testSaveMp.model.UserModel;
import testSaveMp.testSaveMp.model.dto.ItemDto;
import testSaveMp.testSaveMp.server.repository.CategoryRepository;
import testSaveMp.testSaveMp.server.repository.ItemRepository;
import testSaveMp.testSaveMp.server.repository.UserRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class WebServiceImpl implements WebService {
    private final int PAGE_SIZE = 5;
    private final int PAGE_SIZE_WEB = 6;
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
        Pageable limit = PageRequest.of(0, PAGE_SIZE);
        List<String> items = itemRep.findItemLinksByMessageAndCategory(message, category, limit);
        return items.stream()
                .map(File::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemFilesWeb(String msg, String category) {
        List<ItemDto> itemDos = new ArrayList<>();
        List<Item> items = List.of();
        Pageable limit = PageRequest.of(0, PAGE_SIZE_WEB);
        if(!category.isEmpty()) {
            items = itemRep.findItemByMessageAndCategory(msg,category,limit);
        }else {
            items = itemRep.findItemByMessage(msg,limit);
        }
        for(Item i: items){
            ItemDto itemDto = new ItemDto();
            itemDto.setName(i.getName());
            itemDto.setDescription(i.getDescription());
            itemDto.setFileLink(i.getItemLink());
            itemDto.setData(i.getData());
            itemDto.setCategory(i.getCategory().getName());
            itemDos.add(itemDto);
        }
        return itemDos;
    }
}
