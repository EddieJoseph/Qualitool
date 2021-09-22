package ch.eddjos.qualitool.checkboxes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
public class CheckboxPersonDTOFactory {
    @Autowired
    PersonalCheckboxRepo repo;

    public CheckboxPersonDTO createCheckboxPersonDTOFrom(Checkbox checkbox, int person_id){
        CheckboxPersonDTO dto=new CheckboxPersonDTO();
        dto.id=checkbox.getId();
        dto.name=checkbox.getName();
        dto.description=checkbox.getDescription();
        dto.level=checkbox.getLevel();
        dto.severity=checkbox.getSeverity();
        dto.minimumachieved=checkbox.getMinimumachieved();
        PersonalCheckbox personal=repo.getOne(new PersonalCheckbox.PersonalCheckboxId(checkbox.getId(),person_id));
        dto.positiv=personal.isPositiv();
        dto.negativ=personal.isNegativ();
        dto.sighted=personal.isSighted();
        dto.passed=personal.getPassed();
        for (Checkbox subCheckbox:checkbox.getBoxes()){
            dto.boxes.add(createCheckboxPersonDTOFrom(subCheckbox,person_id));
        }
        return dto;
    }
}
