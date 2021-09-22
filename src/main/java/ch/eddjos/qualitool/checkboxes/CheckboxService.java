package ch.eddjos.qualitool.checkboxes;

import ch.eddjos.qualitool.updatecache.UpdateCache;
import ch.eddjos.qualitool.updatecache.Versionized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CheckboxService {
    @Autowired
    private CheckboxRepository repo;
    @Autowired
    private PersonalCheckboxRepo personalCheckboxRepo;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    UpdateCache<PersonalCheckbox> updateCach = new UpdateCache<>(1000);

    public Versionized<PersonalCheckbox> getUpdates(int personId, int cachNr){
        return updateCach.getUpdates(cachNr,personId);
    }

    public List<Checkbox> getStructure(){
        return repo.findAllByLevelEqualsOrderById(0);
    }

    public Versionized<PersonalCheckbox> getPersonalData(int person_id){
        return updateCach.versionize(personalCheckboxRepo.findAllByIdPersonIdEquals(person_id));
    }

    @Transactional
    public Versionized<PersonalCheckbox> putCheckbox(int person_id, int checkbox_id, PersonalCheckboxDTO dto){
        if(dto.positiv&&dto.negativ){
            throw new IllegalArgumentException("Checkbox can not be positiv and negativ at the same time.");
        }
        PersonalCheckbox pc=personalCheckboxRepo.getOne(new PersonalCheckbox.PersonalCheckboxId(checkbox_id,person_id));
        if(pc==null) {
            throw new IllegalArgumentException("Unvalid checkbox id.");
        }
        if (dto.sighted==pc.isSighted()&&dto.negativ==pc.isNegativ()&&dto.positiv==pc.isPositiv()){
            return updateCach.versionize(pc);
        }
        if(pc.isSighted()&&dto.sighted){
            throw new IllegalArgumentException("Only unsighted checkboxes can be changed.");
        }
        if(pc.isSighted()&&!dto.sighted&&personalCheckboxRepo.getOne(new PersonalCheckbox.PersonalCheckboxId(repo.getOne(pc.getCheckboxId()).getParent().getId(),pc.getPersonId())).isSighted()){
            throw new IllegalArgumentException("Prent checkboxed must be unsighted first.");
        }
        Checkbox checkbox = repo.getOne(pc.getCheckboxId());
        if((dto.negativ!=pc.isNegativ()||dto.positiv!=pc.isPositiv())&&(checkbox.getSeverity()<2&&checkbox.getBoxes().size()!=0)){
            throw new IllegalArgumentException("Checkbox values of checkboxes with subcheckboxes can not be changed unless they are optional.");
            //TODO Reject if checkbox has subcheckboxes and severity of checkbox is smaller than 2
        }

        List<Versionized<PersonalCheckbox>> vList = new ArrayList<>();
        pc.updateCheckbox(dto.positiv, dto.negativ, dto.sighted,vList,updateCach,repo,personalCheckboxRepo);

        return vList.subList(1,vList.size()).stream().reduce(vList.get(0),(acc,c)->acc.combine(c));
    }

}
