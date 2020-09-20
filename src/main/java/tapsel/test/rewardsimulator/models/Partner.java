package tapsel.test.rewardsimulator.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tapsel.test.rewardsimulator.models.enums.LevelType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Partner extends BaseEntity {
    String name;
    @ManyToOne(fetch = FetchType.EAGER)
    Partner parent;
    LevelType levelType;
    @OneToMany(fetch = FetchType.EAGER)
    List<Contract> contracts;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", fetch = FetchType.EAGER)
    private List<Partner> children = new ArrayList<>();

    public Partner(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Partner)) return false;
        Partner partner = (Partner) o;
        return Objects.equals(getId(), partner.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "Partner{" +
                "name='" + name + '\'' +
                ", parent=" + parent +
                ", levelType=" + levelType +
                '}';
    }
}
