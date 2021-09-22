package ch.eddjos.qualitool.comments;


import ch.eddjos.qualitool.jwt.JwtTokenUtil;
import ch.eddjos.qualitool.person.Person;
import ch.eddjos.qualitool.person.PersonService;
import ch.eddjos.qualitool.updatecache.UpdateCache;
import ch.eddjos.qualitool.updatecache.Versionized;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comment")
@CrossOrigin
public class CommentController {
    @Autowired
    private CommentService service;
    @Autowired
    private CommentDTOFactory dtoFactory;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PersonService personService;

    private UpdateCache<CommentDTO> updateCache = new UpdateCache(1000);

    @GetMapping("/{pid}")
    public ResponseEntity<Versionized<CommentDTO>> getComments(@PathVariable("pid") int personid/*, @PathVariable("cid") int checkboxId*/){
        //List<Comment> list=service.findByPersonAndCheckboxComplete(personid,checkboxId);
        List<Comment> list=service.findByPerson(personid);
        Collections.sort(list);
        return new ResponseEntity(updateCache.versionize(dtoFactory.create(list)),HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/{version}")
    public ResponseEntity<Versionized<CommentDTO>> save(@RequestBody CommentDTO dto, @PathVariable("version") int versionNr){
        dto.authorId=jwtTokenUtil.getUser().getId();
        System.out.println(dto);
        Comment comment=dtoFactory.unwrap(dto);
        CommentDTO createdDTO = dtoFactory.create(service.insert(comment));

        Versionized<CommentDTO> ret = updateCache.getUpdates(versionNr,createdDTO.personId);
        Versionized<CommentDTO> changed = updateCache.update(createdDTO.personId,createdDTO.commentId,createdDTO);
        if(ret==null) {
            return getComments(createdDTO.personId);
        }

        return new ResponseEntity(ret.combine(changed),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") int commentId){//TODO Delets are not in Update Cache
        Optional<Comment> dbComment = service.findById(commentId);
        CommentDTO commentDto = null;
        if(dbComment.isPresent()){
            commentDto = dtoFactory.create(dbComment.get());
        }

        service.delete(commentId);
        CommentDTO com = new CommentDTO();
        com.commentId=commentDto.commentId;
        com.personId=commentDto.personId;
        updateCache.update(com.personId,com.commentId,com);

        return new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping("/star/{id}/{value}/{version}")
    public ResponseEntity<Versionized<CommentDTO>> setStared(@PathVariable("id") int id, @PathVariable("value") boolean value, @PathVariable("version") int versionNr){
        CommentDTO com=dtoFactory.create(service.setStared(id,value));

        Versionized<CommentDTO> ret = updateCache.getUpdates(versionNr,com.personId);
        Versionized<CommentDTO> changed = updateCache.update(com.personId,com.commentId,com);
        if(ret==null){
            return getComments(com.personId);
        }

        return new ResponseEntity(ret.combine(changed), HttpStatus.OK);
    }
    @GetMapping("/{person_id}/{version}")
    public ResponseEntity<Versionized<CommentDTO>> getUpdates(@PathVariable("person_id") int id, @PathVariable("version") int versionNr){
        Versionized<CommentDTO> updates = updateCache.getUpdates(versionNr,id);
        if(updates!=null) {
            return new ResponseEntity(updateCache.getUpdates(versionNr, id), HttpStatus.OK);
        }else{
            return getComments(id);
        }
    }

//    @GetMapping("/download/{id}")
//    public ResponseEntity getDownload(@PathVariable("id")int id){
//        System.out.println("entered download function");
//        Person p = personService.get(id);
//        byte[] data=service.generateCommentFile(id);
//        ByteArrayResource resource = new ByteArrayResource(data);
//        HttpHeaders headers=new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION,"atachment;filename=\"Beobachtungen_"+p.getNickname()+".xlsx\"");
//        headers.add("filename=","test.xlsx");
//        headers.add(HttpHeaders.CONTENT_LENGTH,Integer.toString(data.length));
//
//
//
//        //return new ResponseEntity(resource,HttpStatus.OK);
//        return ResponseEntity.ok()
//                .headers(headers)
//
//                //.contentLength(data.length)
////                .contentLength(resource.contentLength())
//                //.contentLength(0)
//                .contentType(MediaType.parseMediaType("application/xlsx"))
//                //.contentType(MediaType.parseMediaType("application/octet-stream"))
//
//                .cacheControl(CacheControl.noCache())
//
//
//                .body(resource);
//    }

    @GetMapping(value = "/download/{id}.xlsx", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody byte[] getDownload(@PathVariable("id")int id/*, ServerHttpResponse response*/) throws IOException {
        System.out.println("entered download function");
        Person p = personService.get(id);
        byte[] data=service.generateCommentFile(id);
        ByteArrayResource resource = new ByteArrayResource(data,"atachment;filename=\"Beobachtungen_"+p.getNickname()+".xlsx\"");
        return IOUtils.toByteArray(resource.getInputStream());
    }

}
