package ch.eddjos.qualitool.goups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="group")
@CrossOrigin
public class GroupController {
    @Autowired
    GroupService service;

    @GetMapping
    public ResponseEntity<List<Group>> getAll(){
        return new ResponseEntity(service.findAll(),HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Group> create(@RequestBody GroupDTO dto){
        return new ResponseEntity(service.insert(dto),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id")int id){
        service.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }


}
