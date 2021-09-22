package ch.eddjos.qualitool.auth;

import ch.eddjos.qualitool.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class BenutzerController {
    private BenutzerDTOFactory benutzerDTOFactory;
    private BenutzerRepository benutzerRepository;
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private BenutzerService benutzerService;

    public BenutzerController(BenutzerRepository benutzerRepository, JwtTokenUtil jwtTokenUtil,BenutzerDTOFactory benutzerDTOFactory) {
        this.benutzerRepository = benutzerRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.benutzerDTOFactory = benutzerDTOFactory;
    }

    @GetMapping("")
    public BenutzerDTO getUserData(){
        return benutzerDTOFactory.ctrate(jwtTokenUtil.getUser());
    }

    @GetMapping("/getall")
    public ResponseEntity<List<BenutzerDTO>> getUsers(){
        return ResponseEntity.ok(benutzerService.getAll());
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id)  {
        benutzerService.delete(id);
        return ResponseEntity.ok().build();
    }

}
