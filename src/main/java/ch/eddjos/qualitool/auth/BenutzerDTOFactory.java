package ch.eddjos.qualitool.auth;

import ch.eddjos.qualitool.goups.GroupDTO;
import ch.eddjos.qualitool.goups.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BenutzerDTOFactory {
    @Autowired
    GroupService service;
    public BenutzerDTO ctrate(Benutzer b){
        BenutzerDTO dto=new BenutzerDTO();
        dto.id=b.getId();
        dto.firstName=b.getFirstName();
        dto.lastName=b.getLastName();
        dto.username =b.getUsername();
//        dto.token = b.getToken();
        dto.ip = b.getIp();
        dto.groups=service.findByUser(b).stream().map(g->new GroupDTO(g)).collect(Collectors.toList());
        return dto;
    }

}
