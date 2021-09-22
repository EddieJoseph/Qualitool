package ch.eddjos.qualitool.checkboxes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckboxRepository extends JpaRepository<Checkbox,Integer> {
    List<Checkbox> findAllByLevelEqualsOrderById(int level);
    List<Checkbox> findAllByLevelEquals(int level);
}
