package testSaveMp.testSaveMp.server.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import testSaveMp.testSaveMp.model.Item;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long>{
    @Query("""
        SELECT i.itemLink FROM Item i
        WHERE i.category.name = :category
        AND (LOWER(i.name) LIKE LOWER(CONCAT('%', :message, '%'))
        OR LOWER(i.description) LIKE LOWER(CONCAT('%', :message, '%')))
        """)
    List<String> findItemLinksByMessageAndCategory(
            @Param("message") String message,
            @Param("category") String category,
            Pageable pageable
    );

    @Query("""
        SELECT i FROM Item i
        WHERE i.category.name = :category
        AND (LOWER(i.name) LIKE LOWER(CONCAT('%', :message, '%'))
        OR LOWER(i.description) LIKE LOWER(CONCAT('%', :message, '%')))
        """)
    List<Item> findItemByMessageAndCategory(
            @Param("message") String message,
            @Param("category") String category,
            Pageable pageable
    );

    @Query("""
        SELECT i FROM Item i
        WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :message, '%'))
        OR LOWER(i.description) LIKE LOWER(CONCAT('%', :message, '%'))
        """)
    List<Item> findItemByMessage(@Param("message") String msg, Pageable limit);
}
