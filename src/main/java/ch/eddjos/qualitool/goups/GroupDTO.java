package ch.eddjos.qualitool.goups;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class GroupDTO {
    int groupId;
    String groupName;
    String type;
    List<GroupMemberDTO> members;
    List<GroupMemberDTO> leaders;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<GroupMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<GroupMemberDTO> members) {
        this.members = members;
    }

    public List<GroupMemberDTO> getLeaders() {
        return leaders;
    }

    public void setLeaders(List<GroupMemberDTO> leaders) {
        this.leaders = leaders;
    }

    @Override
    public String toString() {
        return "GroupDTO{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", type='" + type + '\'' +
                ", members=" + members +
                ", leaders=" + leaders +
                '}';
    }

    public GroupDTO(){
        members=new ArrayList<>();
        leaders=new ArrayList<>();
    }
    public GroupDTO(Group group){
        groupId=group.getId();
        groupName=group.getName();
        type=group.getType();
        members=group.getMembers().stream().map(g->new GroupMemberDTO(g)).collect(Collectors.toList());
        leaders=group.getLeaders().stream().map(l->new GroupMemberDTO(l)).collect(Collectors.toList());
    }
}
