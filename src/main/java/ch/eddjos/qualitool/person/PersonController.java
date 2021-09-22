package ch.eddjos.qualitool.person;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/person")
@CrossOrigin
public class PersonController {
    @Autowired
    PersonService personService;

    @Autowired
    PersonDTOFactory factory;

    private ResponseEntity<List<Person>> tmp= null;

    @GetMapping("/")
    public ResponseEntity<List<Person>> getAll(){
        return  new ResponseEntity(personService.getAll().stream().map(p->factory.create(p)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> get(@PathVariable("id") int id){
        return  new ResponseEntity(factory.create(personService.get(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Person> put(@RequestBody Person p){
        return new ResponseEntity(factory.create(personService.put(p)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id){
        personService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/saveimg")
    public ResponseEntity<String> saveImage(@RequestBody ImageDTO imageDto) throws IOException {
        //BufferedImage image = decodeToImage(imageDto.data);
//        String path = "pictures/"+generateName()+".jpg";
//        File outputfile = new File("./static/pictures/"+path);
//        ImageIO.write(image,"jpg",outputfile);


        String[] strings = imageDto.data.split(",");
        String extension;
        switch (strings[0]) {//check image's extension
            case "data:image/jpeg;base64":
                extension = "jpeg";
                break;
            case "data:image/png;base64":
                extension = "png";
                break;
            default://should write cases for more images types
                extension = "jpg";
                break;
        }

        String path = "pictures/"+generateName()+"."+extension;
        File outputfile = new File("static/"+path);
        BufferedImage image = decodeToImage(strings[1]);
        ImageIO.write(image,extension,outputfile);
        return ResponseEntity.ok(path);
    }

    public static String generateName(){
        return RandomStringUtils.randomAlphanumeric(16);
    }

    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            //imageByte = Base64.decodeBase64(imageString);

            imageByte = javax.xml.bind.DatatypeConverter.parseBase64Binary(imageString);


//            BASE64Decoder decoder = new BASE64Decoder();
//            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            System.out.println("decoding error");
            e.printStackTrace();
        }
        return image;
    }
}
