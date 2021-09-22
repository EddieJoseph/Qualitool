package ch.eddjos.qualitool.person;

import ch.eddjos.qualitool.checkboxes.Checkbox;
import ch.eddjos.qualitool.checkboxes.CheckboxPersonDTO;
import ch.eddjos.qualitool.checkboxes.CheckboxService;
import ch.eddjos.qualitool.checkboxes.PersonalCheckbox;
import ch.eddjos.qualitool.comments.Comment;
import ch.eddjos.qualitool.comments.CommentService;
import ch.eddjos.qualitool.goups.GroupDTO;
import ch.eddjos.qualitool.goups.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonDTOFactory {
    @Autowired
    GroupService service;
    @Autowired
    CommentService commentService;
    @Autowired
    CheckboxService checkboxService;


    List<Integer> getSubcheckboxes(Checkbox checkbox) {
        List<Integer> checkboxIds = new ArrayList<>();
        checkboxIds.add(checkbox.getId());
        checkbox.getBoxes().forEach(cb->checkboxIds.addAll(getSubcheckboxes(cb)));
        return checkboxIds;
    }


    public PersonDTO create(Person p){
        PersonDTO dto=new PersonDTO();
        dto.id=p.getId();
        dto.firstname= p.getFirstname();
        dto.lastname=p.getLastname();
        dto.nickname=p.getNickname();
        dto.imageUrl=p.getImageUrl();
        dto.organisation=p.getOrganisation();
        dto.groups=service.findByPerson(p).stream().map(g -> new GroupDTO(g)).collect(Collectors.toList());
        List<Checkbox> checkbox=checkboxService.getStructure();
        List<List<Integer>> subcheckboxes = checkbox.parallelStream().map(cb -> getSubcheckboxes(cb)).collect(Collectors.toList());
        List<Integer> commentCheckboxIds = commentService.findByPerson(p.getId()).stream().map(c->c.getCheckbox().getId()).collect(Collectors.toList());
        dto.commentnumbers = subcheckboxes.stream().map(subCelem->subCelem.stream().mapToInt(cbId -> Collections.frequency(commentCheckboxIds,cbId)).sum()).collect(Collectors.toList());
        List<Integer> mainIds = checkbox.stream().map(cb -> cb.getId()).collect(Collectors.toList());
        dto.checkboxValues = checkboxService.getPersonalData(p.getId()).getData().stream().filter(pcb->mainIds.contains(pcb.getId().getCheckboxId())).sorted((a,b)->a.getCheckboxId()-b.getCheckboxId()).map(pcb -> pcb.getPassed()).collect(Collectors.toList());
        return dto;
    }


}
