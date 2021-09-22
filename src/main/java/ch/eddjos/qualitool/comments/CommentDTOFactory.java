package ch.eddjos.qualitool.comments;


import ch.eddjos.qualitool.auth.BenutzerRepository;
import ch.eddjos.qualitool.checkboxes.CheckboxRepository;
import ch.eddjos.qualitool.person.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommentDTOFactory {
    @Autowired
    private BlockRepository blockRepo;

    @Autowired
    private BenutzerRepository userRepo;
    @Autowired
    private PersonRepository personRepo;
    @Autowired
    private CheckboxRepository checkboxRepo;

    public CommentDTO create(Comment comment){
        CommentDTO dto=new CommentDTO();
        dto.commentId=comment.getId();
        dto.authorId=comment.getUser().getId();
        dto.authorName=comment.getUser().getUsername();
        dto.personId=comment.getPerson().getId();
        dto.text=comment.getText();
        dto.blockId=comment.getBlock().getId();
        dto.blockName=comment.getBlock().getName();
        dto.checkboxId=comment.getCheckbox().getId();
        dto.stared=comment.isStared();
        return dto;
    }
    public List<CommentDTO> create(List<Comment> comments){
        ArrayList<CommentDTO> list=new ArrayList<>();
        comments.forEach(c->list.add(create(c)));
        return list;
    }
    public Comment unwrap(CommentDTO dto/*, Benutzer user*/){
        Comment comment = new Comment();
        comment.setText(dto.text);
        comment.setBlock(blockRepo.findById(dto.blockId).get());
        comment.setCheckbox(checkboxRepo.findById(dto.checkboxId).get());
        comment.setPerson(personRepo.getOne(dto.personId));
        comment.setUser(userRepo.findById(dto.authorId).get());
        comment.setStared(false);
        return comment;
    }
    public BlockDTO create(Block block){
        BlockDTO dto=new BlockDTO();
        dto.value=block.getId();
        dto.label=block.getName();
        return dto;
    }
    public Block unwrap(BlockDTO dto){
        Block block=new Block();
        block.setId(dto.value);
        block.setName(dto.label);
        return block;
    }

    public List<BlockDTO> createBL(List<Block> blocks){
        ArrayList<BlockDTO> list=new ArrayList<>();
        blocks.forEach(b->list.add(create(b)));
        return list;
    }
}
