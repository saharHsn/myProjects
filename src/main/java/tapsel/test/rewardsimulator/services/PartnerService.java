package tapsel.test.rewardsimulator.services;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tapsel.test.rewardsimulator.models.Contract;
import tapsel.test.rewardsimulator.models.Partner;
import tapsel.test.rewardsimulator.models.enums.LevelType;
import tapsel.test.rewardsimulator.repositories.PartnerRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class PartnerService {
    private final PartnerRepository repository;

    public PartnerService(PartnerRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void save(Partner partner) {
        this.repository.save(partner);
        if (partner.getParent() != null) {
            if (!partner.getParent().getChildren().contains(partner)) {
                addChild(partner.getParent(), partner);
            }
        }
    }

    @Transactional
    public void addChild(Partner partner, Partner child) {
        List<Partner> children = partner.getChildren();
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
        this.repository.save(partner);
    }

    @Transactional
    public void addContract(Partner partner, Contract contract) {
        List<Contract> contracts = partner.getContracts();
        if (contracts == null) {
            partner.setContracts(new ArrayList<>());
        }
        partner.getContracts().add(contract);
        this.repository.save(partner);
        updateLevel(partner);
    }

    public List<Long> getChildren(Long partnerId) {
        return this.repository.getChildren(partnerId);
    }

    @Transactional
    public void updateLevel(Partner partner) {
        List<Contract> allActiveContracts = this.repository.findAllActiveContracts(partner.getId());
        if (!CollectionUtils.isEmpty(allActiveContracts)) {
            int size = allActiveContracts.size();
            partner.setLevelType(getLevelType(size));
        } else {
            partner.setLevelType(LevelType.ANT);
        }
        this.repository.save(partner);
    }

    public LevelType getLevelType(int size) {
        if (size < 10) {
            return LevelType.ANT;
        } else if (size < 50) {
            return LevelType.BEE;
        } else if (size < 200) {
            return LevelType.CAT;
        } else if (size < 1000) {
            return LevelType.DOG;
        } else if (size > 1000) {
            return LevelType.ELEPHANT;
        }
        return LevelType.ANT;
    }

    ///////////////////////////////////////////////PRIVATE METHODS \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public Iterable<Partner> getAll() {
        return this.repository.findAll();
    }

    public Partner get(Long id) {
        return this.repository.getPartner(id);
    }
}
