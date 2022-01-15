package ir.maktab.data.model;

import ir.maktab.data.model.enums.TransactionType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "TB_TRANSACTION")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends General {
    private long transactionAmount;
    private long walletAmount;
    private TransactionType transactionType;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_id", nullable = false, foreignKey = @ForeignKey(name = "TRANSACTION_WALLET_FK"))
    private Wallet wallet;
    @CreationTimestamp
    private Date createdDate;
}
