package testSaveMp.testSaveMp.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testSaveMp.testSaveMp.model.Item;


public interface ItemRepository extends JpaRepository<Item, Long>{
}
