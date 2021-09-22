package ch.eddjos.qualitool.comments;

import ch.eddjos.qualitool.auth.Benutzer;
import ch.eddjos.qualitool.checkboxes.Checkbox;
import ch.eddjos.qualitool.person.Person;

import javax.persistence.*;

@Entity
public class Comment implements Comparable<Comment>{
    @Id
    @GeneratedValue
    int id;

    @Column(columnDefinition = "TEXT")
    String text;
    @ManyToOne
    Benutzer user;
    @ManyToOne
    Block block;
    @ManyToOne
    Person person;

    boolean stared;

    public boolean isStared() {
        return stared;
    }

    public void setStared(boolean stared) {
        this.stared = stared;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", user=" + user +
                ", block=" + block +
                ", person=" + person +
                ", checkbox=" + checkbox +
                '}';
    }

    @ManyToOne
    Checkbox checkbox;

    public Checkbox getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(Checkbox checkbox) {
        this.checkbox = checkbox;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Benutzer getUser() {
        return user;
    }

    public void setUser(Benutzer user) {
        this.user = user;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }


    @Override
    public int compareTo(Comment o) {
        return this.id-o.id;
    }
}
