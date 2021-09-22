package ch.eddjos.qualitool.auth;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BenutzerService implements UserDetailsService {

    private final BenutzerRepository benutzerRepository;

    private final PasswordEncoder passwordEncoder;

    @Inject
    public BenutzerService(BenutzerRepository benutzerRepository, PasswordEncoder passwordEncoder) {
        this.benutzerRepository = benutzerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Benutzer benutzer = benutzerRepository.findBenutzerByUsernameEquals(username);
        if (benutzer == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new User(benutzer.getUsername(), benutzer.getPassword(), new ArrayList<>()); //Add grant loading
    }

    public String loadSaltByUsername(String username) throws UsernameNotFoundException {
        Benutzer benutzer = benutzerRepository.findBenutzerByUsernameEquals(username);
        if (benutzer == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return benutzer.getSalt();
    }

    public List<BenutzerDTO> getAll(){
        return benutzerRepository.findAll().stream().map((b)->{
            BenutzerDTO dto = new BenutzerDTO();
            dto.setFirstName(b.getFirstName());
            dto.setLastName(b.getLastName());
            dto.setUsername(b.getUsername());
            dto.setId(b.getId());
            return dto;
        }).collect(Collectors.toList());
    }

    public void delete(int id){
        benutzerRepository.deleteById(id);
    }

    public Benutzer save(BenutzerDTO benutzerDto) {
        Benutzer benutzer = new Benutzer();
        benutzer.setFirstName(benutzerDto.getFirstName());
        benutzer.setLastName(benutzerDto.getLastName());
        benutzer.setUsername(benutzerDto.getUsername());
        benutzer.setSalt(generateSalt(50));
        benutzer.setPassword(passwordEncoder.encode(benutzer.getSalt() + benutzerDto.getPassword()));
        return benutzerRepository.save(benutzer);
    }

    private String generateSalt(int length){
        return RandomStringUtils.random(length,true,true);
    }

}
