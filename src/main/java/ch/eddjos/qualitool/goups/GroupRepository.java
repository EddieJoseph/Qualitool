package ch.eddjos.qualitool.goups;

import ch.eddjos.qualitool.auth.Benutzer;
import ch.eddjos.qualitool.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group,Integer> {
    public List<Group> findAllByMembersContains(Person p);
    public List<Group> findAllByLeadersContains(Benutzer user);
}
