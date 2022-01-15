package ir.maktab.data.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "TB_WALLET")
@Getter
@Setter
@Data
public class Wallet extends General {
    private long amount;
    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", updatable = false, nullable = false, foreignKey = @ForeignKey(name = "WALLET_USER_FK"))
    private User user;
    @CreationTimestamp
    private Date createdDate;
    @UpdateTimestamp
    private Date updatedDate;
}
