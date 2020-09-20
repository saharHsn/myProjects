package tapsel.test.rewardsimulator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tapsel.test.rewardsimulator.models.enums.Quarter;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Entity
@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimePeriod extends BaseEntity {
    Integer year;
    Quarter quarter;

}
