package ch.eddjos.qualitool.comments;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    public List<Comment> findAllByPerson_IdAndCheckbox_IdOrderById(int persinId, int checkboxId);
    public List<Comment> findAllByPerson_Id(int personid);
    public List<Comment> findAllByUser_Id(int userId);

    //public List<Comment> findCommentsBy
}
