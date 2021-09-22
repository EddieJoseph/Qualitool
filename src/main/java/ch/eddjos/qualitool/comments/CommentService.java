package ch.eddjos.qualitool.comments;

import ch.eddjos.qualitool.checkboxes.Checkbox;
import ch.eddjos.qualitool.checkboxes.CheckboxRepository;
import ch.eddjos.qualitool.checkboxes.CheckboxService;
import ch.eddjos.qualitool.checkboxes.PersonalCheckbox;
import ch.eddjos.qualitool.goups.ExcelWriter;
import ch.eddjos.qualitool.person.Person;
import ch.eddjos.qualitool.person.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    CommentRepository repo;
    @Autowired
    CheckboxRepository checkboxRepo;
    @Autowired
    CheckboxService checkboxService;
    @Autowired
    PersonRepository personRepo;

    public List<Comment> findByPerson(int personId){
        return repo.findAllByPerson_Id(personId);
    }

    public List<Comment> findByAuthor(int userId){
        return repo.findAllByUser_Id(userId);
    }

    public List<Comment> findByPersonAndCheckbox(int personId, int checkboxId){
        return repo.findAllByPerson_IdAndCheckbox_IdOrderById(personId,checkboxId);
    }

    public Optional<Comment> findById(int commentId){
        return repo.findById(commentId);
    }

//    public List<Comment> findByPerson(int personId){
//
//        return null;
//    }

    public List<Comment> findByPersonAndCheckboxComplete(int personId, int checkboxId){
        List<Comment> list = findByPersonAndCheckbox(personId, checkboxId);
        Checkbox cb = checkboxRepo.getOne(checkboxId);
        //if(cb.getBoxes().size()>0){
         for(Checkbox c:cb.getBoxes()){
             list.addAll(findByPersonAndCheckboxComplete(personId,c.getId()));
         }
        //}
        return list;
    }

    public Comment insert(Comment comment){
        //System.out.println(comment);
        return repo.save(comment);
    }

    public void delete(int id){
        repo.deleteById(id);
    }

    @Transactional
    public Comment setStared(int id, boolean value){
        Comment c=repo.findById(id).get();
        c.setStared(value);
        return c;
    }


    public byte[] generateCommentFile(int personId){
        ExcelWriter e=new ExcelWriter("test");
        Person p=personRepo.getOne(personId);

        List<Checkbox> structure= checkboxService.getStructure();
        for(Checkbox l1:structure){
          findByPersonAndCheckbox(personId,l1.getId()).stream().forEach(com->e.add(l1,null,null,null,null,p,com));
            for(Checkbox l2:l1.getBoxes()){
                findByPersonAndCheckbox(personId,l2.getId()).stream().forEach(com->e.add(l1,l2,null,null,null,p,com));
                for(Checkbox l3:l2.getBoxes()){
                    findByPersonAndCheckbox(personId,l3.getId()).stream().forEach(com->e.add(l1,l2,l3,null,null,p,com));
                    for(Checkbox l4:l3.getBoxes()){
                        findByPersonAndCheckbox(personId,l4.getId()).stream().forEach(com->e.add(l1,l2,l3,l4,null,p,com));
                        for(Checkbox l5:l4.getBoxes()){
                            findByPersonAndCheckbox(personId,l5.getId()).stream().forEach(com->e.add(l1,l2,l3,l4,l5,p,com));

                        }
                    }
                }
            }
        }
        return e.create();
    }

    private static final String separator=";";

    private String commaSeparate(String ... input){
        StringBuilder builder=new StringBuilder();
        boolean first=true;
        for(String i:input){
            if(!first){
             builder.append(separator);
            }else{
                first=false;
            }
            builder.append(i);
        }

        return builder.toString();
    }


    private String getTitle(){
        return commaSeparate("Level 1","Level 2","Level 3","Level 4","Level 5","Markiert","Block","Autor","Beobachtung");
    }

    private String toCsvLine(Checkbox level1,Checkbox level2,Checkbox level3,Checkbox level4,Checkbox level5,Comment comment){
        StringBuilder builder=new StringBuilder();

        return builder.toString();
    }






}
