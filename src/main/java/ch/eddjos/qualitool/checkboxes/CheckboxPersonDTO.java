package ch.eddjos.qualitool.checkboxes;

import java.util.ArrayList;
import java.util.List;

public class CheckboxPersonDTO {

    public CheckboxPersonDTO(){
        boxes=new ArrayList<>();
    }

    public int id;
    public String name;

    public String description;

    public int level;
    public int severity;

    public Integer minimumachieved;

    public boolean positiv;
    public boolean negativ;
    public boolean sighted;
    public Boolean passed;

    public List<CheckboxPersonDTO> boxes;

}
