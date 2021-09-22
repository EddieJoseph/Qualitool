package ch.eddjos.qualitool.comments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BlockService {
    @Autowired
    BlockRepository repo;
    public List<Block> findAll(){
        return repo.findAll();
    }

    public Block findOne(int id){
        return repo.findById(id).orElse(null);
    }

    public Block insert(Block block){
        return repo.save(block);
    }

    public void delete (int id){
        repo.deleteById(id);
    }

}
