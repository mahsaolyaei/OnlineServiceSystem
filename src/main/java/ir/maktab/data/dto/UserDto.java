package ir.maktab.data.dto;

import ir.maktab.data.model.enums.UserStatus;
import ir.maktab.data.model.enums.UserType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Data
public class UserDto {
    private int id;
    private String name;
    private String family;
    private String email;
    private String imageAddress;
    private float score;
    private UserType type;
    private UserStatus status;
    private Date createdDate;
}
