package ir.maktab.model;

import ir.maktab.model.enums.UserStatus;
import ir.maktab.model.enums.UserType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
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
//    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "experts")
    @Transient
    private Set<Service> services;
}
