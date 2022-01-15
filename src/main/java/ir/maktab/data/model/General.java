package ir.maktab.data.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
public class General {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
}
