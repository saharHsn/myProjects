package tapsel.test.rewardsimulator.models;

import lombok.Getter;
import lombok.Setter;
import tapsel.test.rewardsimulator.models.enums.ContractType;
import tapsel.test.rewardsimulator.utils.DateUtil;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Getter
@Setter
public class Contract extends BaseEntity {
    ContractType type;
    Date startDate;
    Boolean isExpired = false;
    TimePeriod timePeriod;
    @ManyToOne
    Partner owner;

    public boolean isExpired() {
        Date current = new Date();
        return DateUtil.getYear(current) - DateUtil.getYear(startDate) >= 8;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "type=" + type +
                ", startDate=" + startDate +
                ", isExpired=" + isExpired +
                ", timePeriod.year=" + timePeriod.getYear() +
                ", timePeriod.quarter=" + timePeriod.getQuarter()+
                ", owner=" + owner.getName() +
                '}';
    }
}
