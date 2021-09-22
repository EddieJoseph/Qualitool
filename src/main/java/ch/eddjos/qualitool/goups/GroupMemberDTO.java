package ch.eddjos.qualitool.goups;

import ch.eddjos.qualitool.auth.Benutzer;
import ch.eddjos.qualitool.person.Person;

public class GroupMemberDTO {
    public int id;
    public String name;

    public GroupMemberDTO(){}

    public GroupMemberDTO(Benutzer user){
        id=user.getId();
        name=user.getUsername();
    }
    public GroupMemberDTO(Person pers){
        id=pers.getId();
        name=pers.getNickname();
    }
}
