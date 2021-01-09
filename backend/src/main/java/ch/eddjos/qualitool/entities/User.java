//package ch.eddjos.qualitool.entities;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//import javax.persistence.*;
//
//@Entity
//@Table
//public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//    private String firstName;
//    private String lastName;
//
//
//
//    @Column(unique = true)
//    private String email;
//    private String username;
//    @JsonIgnore
//    private String password;
//    @JsonIgnore
//    private String salt;
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getSalt() {
//        return salt;
//    }
//
//    public void setSalt(String salt) {
//        this.salt = salt;
//    }
//
//}
