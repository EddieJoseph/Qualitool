package ch.eddjos.qualitool.goups;

import ch.eddjos.qualitool.auth.Benutzer;
import ch.eddjos.qualitool.auth.BenutzerRepository;
import ch.eddjos.qualitool.person.Person;
import ch.eddjos.qualitool.person.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {
    @Autowired
    GroupRepository repo;
    @Autowired
    BenutzerRepository benutzerRepo;
    @Autowired
    PersonRepository personRepo;

    public List<Group> findAll(){
        return repo.findAll();
    }
    public List<Group> findByPerson(Person p){
     return repo.findAllByMembersContains(p);
    }
    public List<Group> findByUser(Benutzer user){
        return repo.findAllByLeadersContains(user);
    }

    public Group insert(GroupDTO dto){
        Group g=new Group();
        //g.setId(dto.groupId);
        g.setType(dto.type);
        g.setName(dto.groupName);
        g.getMembers().addAll(dto.members.stream().map(dm->personRepo.findById(dm.id).get()).collect(Collectors.toList()));
        g.getLeaders().addAll(dto.leaders.stream().map(dl->benutzerRepo.findById(dl.id).get()).collect(Collectors.toList()));
        //System.out.println(g.getId());
        return repo.save(g);
    }

    public void delete(int id){
        repo.deleteById(id);
    }
}
