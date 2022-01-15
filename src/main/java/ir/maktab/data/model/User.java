package ir.maktab.data.model;

import ir.maktab.data.model.enums.UserStatus;
import ir.maktab.data.model.enums.UserType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity(name = "TB_USER")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends General {
    private String name;
    private String family;
    private String email;
    private String password;
    private String imageAddress;
    private float score;
    @Enumerated(EnumType.STRING)
    private UserType type;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @CreationTimestamp
    private Date createdDate;
    @Transient
    private Set<Service> services;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", family='" + family + '\'' +
                ", email='" + email + '\'' +
                ", imageAddress='" + imageAddress + '\'' +
                ", score=" + score +
                ", type=" + type +
                ", status=" + status +
                ", createdDate=" + createdDate +
                ", services=" + services +
                "} " + super.toString();
    }
}
