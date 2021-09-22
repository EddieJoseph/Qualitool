package ch.eddjos.qualitool.checkboxes;

import ch.eddjos.qualitool.person.Person;
import ch.eddjos.qualitool.updatecache.UpdateCache;
import ch.eddjos.qualitool.updatecache.Versionized;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table
public class PersonalCheckbox {
    @EmbeddedId
    PersonalCheckboxId id;
    private boolean positiv;

    @Override
    public String toString() {
        return "PersonalCheckbox{" +
                "id=" + id +
                ", positiv=" + positiv +
                ", negativ=" + negativ +
                ", sighted=" + sighted +
                ", passed=" + passed +
                '}';
    }

    private boolean negativ;
    private boolean sighted;
    private Boolean passed;


    public PersonalCheckbox copy(){
        PersonalCheckbox n = new PersonalCheckbox();
        n.negativ=this.isNegativ();
        n.passed=this.getPassed();
        n.positiv=this.isPositiv();
        n.sighted=this.isSighted();
        n.getId().setCheckboxId(this.getCheckboxId());
        n.getId().setPersonId(this.getPersonId());
        return n;
    }


    public PersonalCheckbox(){
        id=new PersonalCheckboxId();
    };

    public PersonalCheckbox(Checkbox c, Person p){
        id=new PersonalCheckboxId(c,p);
    }

    @JsonIgnore
    public PersonalCheckboxId getId() {
        return id;
    }

    public int getPersonId(){
        return id.getPersonId();
    }

    public int getCheckboxId(){
        return id.checkboxId;
    }

    public void setId(PersonalCheckboxId id) {
        this.id = id;
    }

    public boolean isPositiv() {
        return positiv;
    }

    public void setPositiv(boolean positiv) {
        this.positiv = positiv;
    }

    public boolean isNegativ() {
        return negativ;
    }

    public void setNegativ(boolean negativ) {
        this.negativ = negativ;
    }

    public boolean isSighted() {
        return sighted;
    }

    public void setSighted(boolean sighted) {
        this.sighted = sighted;
    }

    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }
    @Embeddable
    public static class PersonalCheckboxId implements Serializable {
        int checkboxId;

        int personId;

        public PersonalCheckboxId(){}

        public PersonalCheckboxId(int checkboxId, int personId){
            this.checkboxId = checkboxId;
            this.personId = personId;
        }
        public PersonalCheckboxId(Checkbox c, Person p){
            this(c.getId(),p.getId());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PersonalCheckboxId that = (PersonalCheckboxId) o;
            return checkboxId == that.checkboxId &&
                    personId == that.personId;
        }

        @Override
        public String toString() {
            return "PersonalCheckboxId{" +
                    "checkboxId=" + checkboxId +
                    ", personId=" + personId +
                    '}';
        }

        @Override
        public int hashCode() {
            return Objects.hash(checkboxId, personId);
        }

        public int getCheckboxId() {
            return checkboxId;
        }

        public void setCheckboxId(int checkboxId) {
            this.checkboxId = checkboxId;
        }

        public int getPersonId() {
            return personId;
        }

        public void setPersonId(int personId) {
            this.personId = personId;
        }
    }

    //Setting
    @Transactional
    public List<Versionized<PersonalCheckbox>> updateCheckbox(boolean positivN, boolean negativN, boolean sightedN, List<Versionized<PersonalCheckbox>> updatelist, UpdateCache<PersonalCheckbox> updateCach, CheckboxRepository checkboxRepo, PersonalCheckboxRepo personalCheckboxRepo){
        Checkbox cb = checkboxRepo.getOne(getCheckboxId());
        if(sightedN&&!sighted){
            cb.getBoxes().stream().map(b->personalCheckboxRepo.getOne(new PersonalCheckbox.PersonalCheckboxId(b.getId(),getPersonId())))
                    .map(tpcb->tpcb.updateChild(sightedN,updateCach,checkboxRepo,personalCheckboxRepo)).forEach(updatelist::addAll);
        }

        boolean change = sighted!=sightedN||positiv!=positivN||negativ!=negativN;

        sighted=sightedN;
        positiv=positivN;
        negativ=negativN;
        Boolean tpass = passed;

        if(cb.getSeverity()==2){
            passed = null;
        }else if(cb.getBoxes().size()==0){
            if(positiv || (sighted && !positiv && !negativ)){
                passed = true;
            }else if(negativ){
                passed = false;
            } else {
                passed = null;
            }
        } else {
            int numberOfRatesSubboxes = (int)cb.getBoxes().stream().filter(b->b.getSeverity()==1).map(b->personalCheckboxRepo.getOne(new PersonalCheckbox.PersonalCheckboxId(b.getId(),getPersonId()))).count();
            if(cb.getBoxes().stream().filter(b->b.getSeverity()==1).map(b->personalCheckboxRepo.getOne(new PersonalCheckbox.PersonalCheckboxId(b.getId(),getPersonId()))).filter(c->(c.getPassed()!=null&&c.getPassed())).count()>=cb.getMinimumachieved()){
                passed=true;
                positiv=true;
                negativ=false;
            } else if(cb.getBoxes().stream().filter(b->b.getSeverity()==1).map(b->personalCheckboxRepo.getOne(new PersonalCheckbox.PersonalCheckboxId(b.getId(),getPersonId()))).filter(c-> c.getPassed()!=null&&!c.getPassed()).count()>numberOfRatesSubboxes-cb.getMinimumachieved()){
                passed=false;
                positiv=false;
                negativ=true;
            }else{
                passed=null;
                positiv=false;
                negativ=false;
            }
        }

        if(change||tpass!=passed){
            updatelist.add(updateCach.update(getPersonId(),getCheckboxId(),this));
        }

        if(cb.getParent()!=null){
            updatelist.addAll(personalCheckboxRepo.getOne(new PersonalCheckbox.PersonalCheckboxId(cb.getParent().getId(),getPersonId())).updateParent(updateCach,checkboxRepo,personalCheckboxRepo));
        }



        return updatelist;
    }

    @Transactional
    protected List<Versionized<PersonalCheckbox>> updateChild(boolean sightedN, UpdateCache<PersonalCheckbox> updateCach, CheckboxRepository checkboxRepo, PersonalCheckboxRepo personalCheckboxRepo) {

        List<Versionized<PersonalCheckbox>> returnList = new ArrayList<>();
        boolean sig=isSighted();
        boolean neg=isNegativ();
        boolean pos=isPositiv();
        Boolean pas=getPassed();
        Checkbox cb = checkboxRepo.getOne(getCheckboxId());
        if(sightedN&&!sighted){
            cb.getBoxes().stream().map(b->personalCheckboxRepo.getOne(new PersonalCheckbox.PersonalCheckboxId(b.getId(),getPersonId())))
                    .map(tpcb->tpcb.updateChild(sightedN,updateCach,checkboxRepo,personalCheckboxRepo)).forEach(returnList::addAll);
        }

        sighted=sightedN;

        if(cb.getSeverity()==2){
            passed = null;
        }else if(cb.getBoxes().size()==0){
            if(positiv || (sighted && !positiv && !negativ)){
                passed = true;
            }else if(negativ){
                passed = false;
            } else {
                passed = null;
            }
        } else {
            int numberOfRatesSubboxes = (int)cb.getBoxes().stream().filter(b->b.getSeverity()==1).map(b->personalCheckboxRepo.getOne(new PersonalCheckbox.PersonalCheckboxId(b.getId(),getPersonId()))).count();
            if(cb.getBoxes().stream().filter(b->b.getSeverity()==1).map(b->personalCheckboxRepo.getOne(new PersonalCheckbox.PersonalCheckboxId(b.getId(),getPersonId()))).filter(c->(c.getPassed()!=null&&c.getPassed())).count()>=cb.getMinimumachieved()){
                passed=true;
                positiv=true;
                negativ=false;
            } else if(cb.getBoxes().stream().filter(b->b.getSeverity()==1).map(b->personalCheckboxRepo.getOne(new PersonalCheckbox.PersonalCheckboxId(b.getId(),getPersonId()))).filter(c-> c.getPassed()!=null&&!c.getPassed()).count()>numberOfRatesSubboxes-cb.getMinimumachieved()){
                passed=false;
                positiv=false;
                negativ=true;
            }else{
                passed=null;
                positiv=false;
                negativ=false;
            }
        }

        if(sig!=isSighted()||neg!=isNegativ()||pos!=isPositiv()||pas!=getPassed()){
            returnList.add(updateCach.update(this.getPersonId(),this.getCheckboxId(),this));
        }

        return returnList;
    }


    //Updating
    @Transactional
    protected List<Versionized<PersonalCheckbox>> updateParent(UpdateCache<PersonalCheckbox> updateCach, CheckboxRepository checkboxRepo, PersonalCheckboxRepo personalCheckboxRepo){
        List<Versionized<PersonalCheckbox>> updatelist = new ArrayList<>();
        Checkbox cb = checkboxRepo.getOne(getCheckboxId());
        boolean neg=isNegativ();
        boolean pos=isPositiv();
        Boolean pas=getPassed();

        if(cb.getLevel()==0){//Toplevel
            List <Checkbox> tmplist = new ArrayList<>();
            tmplist.add(cb);
            for(int counter = 0;counter<tmplist.size();counter++){
                tmplist.addAll(tmplist.get(counter).getBoxes());
            }
            List<PersonalCheckbox> critical = tmplist.subList(1, tmplist.size()).stream().filter(c -> c.getSeverity() == 0).map(c -> personalCheckboxRepo.getOne(new PersonalCheckboxId(c.getId(), getPersonId()))).collect(Collectors.toList());
            if(critical.stream().filter(c->c.getPassed()!=null&&c.getPassed()).count()==critical.size()){ //All critical done
                if(cb.getSeverity()<2){
                    List<PersonalCheckbox> important = cb.getBoxes().stream().filter(b->b.getSeverity()==1).map(c -> personalCheckboxRepo.getOne(new PersonalCheckboxId(c.getId(), getPersonId()))).collect(Collectors.toList());
                    int nrPassed = (int)important.stream().filter(c->c.getPassed()!=null&&c.getPassed()).count();
                    int failed = (int)important.stream().filter(c->c.getPassed()!=null&&!c.getPassed()).count();
                    if(nrPassed>=cb.getMinimumachieved()){
                        negativ=false;
                        positiv=true;
                        passed=true;
                    }else if(important.size()-failed<cb.getMinimumachieved()){
                        negativ=true;
                        positiv=false;
                        passed=false;
                    }else{
                        negativ=false;
                        positiv=false;
                        passed=null;
                    }
                } else {
                    negativ=false;
                    positiv=true;
                    passed=true;
                }

            }else if(critical.stream().filter(c->c.getPassed()!=null&&!c.getPassed()).count()>0){//failed
                negativ=true;
                positiv=false;
                passed=false;
            } else{

                if(cb.getSeverity()<2){
                    List<PersonalCheckbox> important = cb.getBoxes().stream().filter(b->b.getSeverity()==1).map(c -> personalCheckboxRepo.getOne(new PersonalCheckboxId(c.getId(), getPersonId()))).collect(Collectors.toList());
                    int nrPassed = (int)important.stream().filter(c->c.getPassed()!=null&&c.getPassed()).count();
                    int failed = (int)important.stream().filter(c->c.getPassed()!=null&&!c.getPassed()).count();
                    if(nrPassed>=cb.getMinimumachieved()){
                        negativ=false;
                        positiv=false;
                        passed=null;
                    }else if(important.size()-failed<cb.getMinimumachieved()){
                        negativ=true;
                        positiv=false;
                        passed=false;
                    }else{
                        negativ=false;
                        positiv=false;
                        passed=null;
                    }
                } else {
                    negativ=false;
                    positiv=true;
                    passed=true;
                }
            }
        }else if(cb.getSeverity()<2){
            List<PersonalCheckbox> important = cb.getBoxes().stream().filter(b->b.getSeverity()==1).map(c -> personalCheckboxRepo.getOne(new PersonalCheckboxId(c.getId(), getPersonId()))).collect(Collectors.toList());
            int nrPassed = (int)important.stream().filter(c->c.getPassed()!=null&&c.getPassed()).count();
            int failed = (int)important.stream().filter(c->c.getPassed()!=null&&!c.getPassed()).count();
            if(nrPassed>=cb.getMinimumachieved()){
                negativ=false;
                positiv=true;
                passed=true;
            }else if(important.size()-failed<cb.getMinimumachieved()){
                negativ=true;
                positiv=false;
                passed=false;
            }else{
                negativ=false;
                positiv=false;
                passed=null;
            }
        }

        if(neg!=negativ||pos!=positiv||pas!=passed){
            updatelist.add(updateCach.update(this.getPersonId(),this.getCheckboxId(),this));
        }

        if(cb.getParent()!=null){
            updatelist.addAll(personalCheckboxRepo.getOne(new PersonalCheckbox.PersonalCheckboxId(cb.getParent().getId(),getPersonId())).updateParent(updateCach,checkboxRepo,personalCheckboxRepo));
        }

        return updatelist;
    }



}
