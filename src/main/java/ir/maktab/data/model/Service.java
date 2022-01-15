package ir.maktab.data.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "TB_SERVICE")
@Getter
@Setter
@Data
public class Service extends General {
    private String name;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "TB_SERVICE_USER"
            , inverseJoinColumns = @JoinColumn(updatable = false)
    )
    private Set<User> experts;
}
