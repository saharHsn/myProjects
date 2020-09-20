package tapsel.test.rewardsimulator.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import tapsel.test.rewardsimulator.models.Contract;
import tapsel.test.rewardsimulator.models.enums.Quarter;

import java.util.List;

public interface ContractRepository extends CrudRepository<Contract, Long> {

    @Query("SELECT c FROM Contract c " +
            "WHERE " +
            "c.owner.id = :ownerId AND " +
            "(:quarter IS NULL OR c.timePeriod.quarter = :quarter) AND " +
            "(:year IS NULL OR c.timePeriod.year = :year) AND " +
            "c.isExpired = false ")
    List<Contract> filterContractsByTimePeriod(
            @Param("ownerId") Long ownerId,
            @Param("quarter") Quarter quarter,
            @Param("year") Integer year);


}
