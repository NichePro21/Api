package ManagementSystem.fpt.Repositories;

import ManagementSystem.fpt.Models.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    boolean existsByPhone(String phone);
}
