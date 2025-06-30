package testSaveMp.testSaveMp.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testSaveMp.testSaveMp.model.AudioModel;

public interface AudioModelRepository extends JpaRepository<AudioModel, Long>{
}
