package com.example.movie_ticket_booking.Models;

import com.example.movie_ticket_booking.Common.Constant;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends BaseModel {
    private String lastName;
    private String firstName;
    private Date birthdate;
    private boolean gender;
    private String avatarPath;
    private String email;
    private String phone;

    //authentication
    private String username;

    private UserRole role;

    //Foreign key
    private List<Ticket> tickets;

    //constructor
    public User(){
        super();
        this.role = UserRole.CLIENT;
    }

    public void setInfo(Map<String, String> info) throws ParseException {
        this.avatarPath = "https://res.cloudinary.com/dzm6ikgbo/image/upload/v1714058057/okrooxwdmklbtz1jlqjj.png";
        tickets = new ArrayList<>();
        for(Map.Entry<String, String> i : info.entrySet()){
            switch (i.getKey()){
                case "firstname":
                    this.firstName = i.getValue();
                    break;
                case "lastname":
                    this.lastName = i.getValue();
                    break;
                case "username":
                    this.username = i.getValue();
                    break;
                case "email":
                    this.email = i.getValue();
                    break;
                case "phone":
                    this.phone = i.getValue();
                    break;
                case "birthdate":
                    this.birthdate = Constant.DATE_FORMATTER.parse(i.getValue());
                    break;
                case "gender":
                    this.gender = i.getValue().equalsIgnoreCase("Nam");
                    break;
            }
        }
    }
    @Override
    public String toString() {
        return String.format("%s %s %s", this.role.prefix, this.lastName, this.firstName);
    }

    public String getFullName() {
        return String.format("%s %s", this.lastName, this.firstName);
    }
}
