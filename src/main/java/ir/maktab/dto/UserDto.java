package ir.maktab.dto;

import ir.maktab.model.Service;
import ir.maktab.model.enums.UserStatus;
import ir.maktab.model.enums.UserType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.Set;

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
