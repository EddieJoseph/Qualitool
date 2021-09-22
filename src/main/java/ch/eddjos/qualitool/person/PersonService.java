package ch.eddjos.qualitool.person;

import ch.eddjos.qualitool.checkboxes.Checkbox;
import ch.eddjos.qualitool.checkboxes.CheckboxRepository;
import ch.eddjos.qualitool.checkboxes.PersonalCheckbox;
import ch.eddjos.qualitool.checkboxes.PersonalCheckboxRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PersonService {
    @Autowired
    PersonRepository repo;
    @Autowired
    CheckboxRepository checkboxRepo;
    @Autowired
    PersonalCheckboxRepo personalCheckboxRepo;

    public List<Person> getAll(){
        return repo.findAll();
    }

    public Person get(int id){
        return repo.findById(id).orElse(null);
    }

    @Transactional
    public Person put(Person p){
        p=repo.save(p);
        List<Checkbox> checkboxes=checkboxRepo.findAll();
        for(Checkbox c:checkboxes){
            personalCheckboxRepo.save(new PersonalCheckbox(c,p));

            PersonalCheckbox pers=new PersonalCheckbox();
            pers.getId().setCheckboxId(c.getId());
            pers.getId().setPersonId(p.getId());
        }
        return p;
    }

    @Transactional
    public void delete(int id) {
        personalCheckboxRepo.deleteAll(personalCheckboxRepo.findAllByIdPersonIdEquals(id));
        repo.deleteById(id);
    }
}
