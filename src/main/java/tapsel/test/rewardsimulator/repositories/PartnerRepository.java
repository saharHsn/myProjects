package tapsel.test.rewardsimulator.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import tapsel.test.rewardsimulator.models.Contract;
import tapsel.test.rewardsimulator.models.Partner;
import tapsel.test.rewardsimulator.models.enums.Quarter;

import java.util.List;

public interface PartnerRepository extends CrudRepository<Partner, Long> {
    List<Partner> findPartnerByParent(Partner parent);

    @Query("SELECT c FROM Partner p " +
            "JOIN p.contracts c WHERE " +
            "(p.id = :partnerId) AND " +
            "(:quarter IS NULL OR c.timePeriod.quarter = :quarter) AND " +
            "(:year IS NULL OR c.timePeriod.year = :year)")
    List<Contract> filterContractsByTimePeriod(
            @Param("partnerId") Long partnerId,
            @Param("quarter") Quarter quarter,
            @Param("year") Integer year);


    @Query("SELECT p.contracts FROM Partner p " +
            "JOIN p.contracts c WHERE " +
            "(p.id = :partnerId) AND " +
            "c.isExpired = false ")
    List<Contract> findAllActiveContracts(@Param("partnerId") Long partnerId);

    @Query("SELECT p.id FROM Partner p " +
            "WHERE " +
            "(p.parent.id = :partnerId) ")
    List<Long> getChildren(@Param("partnerId") Long partnerId);

    @Query("SELECT p FROM Partner p " +
            "WHERE " +
            "(p.id = :partnerId) ")
    Partner getPartner(@Param("partnerId") Long partnerId);
}
