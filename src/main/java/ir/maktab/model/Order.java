package ir.maktab.model;

import ir.maktab.model.enums.OrderStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity(name = "TB_ORDER")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends General {
    private long offeredPrice;
    private String description;
    private String address;
    private Date requestedDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(updatable = false, nullable = false, foreignKey = @ForeignKey(name = "ORDER_SUBSERVICE_FK"))
    private SubService subService;
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(updatable = false, nullable = false, foreignKey = @ForeignKey(name = "ORDER_CUSTOMER_FK"))
    private User customer;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Offer> offers;
    @CreationTimestamp
    private Date createdDate;
}
