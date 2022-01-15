package ir.maktab.data.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "TB_COMMENT")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends General {
    private int score;
    private String description;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(updatable = false, nullable = false, foreignKey = @ForeignKey(name = "COMMENT_EXPERT_FK"))
    private User expert;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", updatable = false, nullable = false, foreignKey = @ForeignKey(name = "COMMENT_ORDER_FK"))
    private Order order;
    @CreationTimestamp
    private Date createdDate;
}
