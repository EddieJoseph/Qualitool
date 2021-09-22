package ch.eddjos.qualitool.checkboxes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonalCheckboxRepo extends JpaRepository<PersonalCheckbox, PersonalCheckbox.PersonalCheckboxId> {

    List<PersonalCheckbox> findAllByIdPersonIdEquals(int personId);

}
