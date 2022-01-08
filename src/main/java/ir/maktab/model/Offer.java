package ir.maktab.model;

import ir.maktab.model.enums.OfferStatus;
import ir.maktab.model.enums.OrderStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "TB_OFFER")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Offer extends General {
    private long price;
    private long hours;
    private Date respondDate;
    @Enumerated(EnumType.STRING)
    private OfferStatus status;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "OFFER_ORDER_FK"))
    private Order order;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(updatable = false, nullable = false, foreignKey = @ForeignKey(name = "OFFER_EXPERT_FK"))
    private User expert;
    @CreationTimestamp
    private Date createdDate;
}
