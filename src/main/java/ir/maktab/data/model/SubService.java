package ir.maktab.data.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "TB_SUB_SERVICE")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubService extends General {
    private String name;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id", updatable = false, nullable = false, foreignKey = @ForeignKey(name = "SUBSERVICE_SERVICE_FK"))
    private Service service;
    private long basePrice;
    private String description;




}
