package ch.eddjos.qualitool.person;

import ch.eddjos.qualitool.goups.GroupDTO;

import java.util.List;

public class PersonDTO {
    public int id;
    public String firstname;
    public String lastname;
    public String nickname;
    public String organisation;
    public String imageUrl;
    public List<GroupDTO> groups;
    public List<Integer> commentnumbers;
    public List<Boolean> checkboxValues;

}
