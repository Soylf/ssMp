package testSaveMp.testSaveMp.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testSaveMp.testSaveMp.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    boolean existsByUserId(Long userId);
}
