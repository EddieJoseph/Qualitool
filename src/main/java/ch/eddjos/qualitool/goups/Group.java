package ch.eddjos.qualitool.goups;

import ch.eddjos.qualitool.auth.Benutzer;
import ch.eddjos.qualitool.person.Person;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name="gruppe")
public class Group {
    @Id
    @GeneratedValue
    int id;
    @ManyToMany
    List<Person> members;
    @ManyToMany
    List<Benutzer> leaders;
    String type;
    String name;

    public Group(){
        members=new ArrayList<>();
        leaders=new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Person> getMembers() {
        return members;
    }

    public void setMembers(List<Person> members) {
        this.members = members;
    }

    public List<Benutzer> getLeaders() {
        return leaders;
    }

    public void setLeaders(List<Benutzer> leaders) {
        this.leaders = leaders;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
