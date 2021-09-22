package ch.eddjos.qualitool.checkboxes;

import ch.eddjos.qualitool.updatecache.Versionized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/cb")
@CrossOrigin
public class CheckboxController {
    @Autowired
    CheckboxService checkboxService;

    //UpdateCache<CheckboxPersonDTO>

    @GetMapping("/")
    public ResponseEntity<List<Checkbox>> getStructure(){
        return new ResponseEntity(checkboxService.getStructure(),HttpStatus.OK);
    }

    @GetMapping("/{person_id}")
    public ResponseEntity<Versionized<PersonalCheckbox>> getPersonalData(@PathVariable("person_id") int id){
        return new ResponseEntity(checkboxService.getPersonalData(id),HttpStatus.OK);
    }

    //@SendTo("/topic/cb")
    @PutMapping("/{person_id}/{checkbox_id}/{version}")
    public ResponseEntity<Versionized<PersonalCheckbox>> put(@PathVariable("person_id") int person_id, @PathVariable("checkbox_id") int checkbox_id, @RequestBody PersonalCheckboxDTO dto, @PathVariable("version") int versionNr){
        try {
            Versionized<PersonalCheckbox> ret = checkboxService.getUpdates(person_id, versionNr);
            Versionized<PersonalCheckbox> changed = checkboxService.putCheckbox(person_id, checkbox_id, dto);
            if (ret == null) {
                return getPersonalData(person_id);
            }

            return new ResponseEntity(ret.combine(changed), HttpStatus.OK);
        }catch(IllegalArgumentException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.LOCKED);
        }


        /*
        if(checkboxService.putCheckbox(person_id,checkbox_id,dto)){
            return new ResponseEntity(checkboxService.getPersonalData(person_id), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }*/
    }

    @GetMapping("/{person_id}/{version}")
    public ResponseEntity<Versionized<PersonalCheckbox>> getUpdates(@PathVariable("person_id") int id, @PathVariable("version") int versionNr){
        Versionized<PersonalCheckbox> res = checkboxService.getUpdates(id, versionNr);
        if(res==null){
            return getPersonalData(id);
        }
        return new ResponseEntity(res , HttpStatus.OK);
    }


}
