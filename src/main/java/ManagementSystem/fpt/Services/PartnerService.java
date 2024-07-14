package ManagementSystem.fpt.Services;

import ManagementSystem.fpt.Models.Partner;
import ManagementSystem.fpt.Repositories.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PartnerService {

    @Autowired
    private PartnerRepository partnerRepository;

    public List<Partner> getAllPartners() {
        return partnerRepository.findAll();
    }

    public Optional<Partner> getPartnerById(Long id) {
        return partnerRepository.findById(id);
    }

    public Partner createPartner(Partner partner) {
        if (partnerRepository.existsByPhone(partner.getPhone())) {
            throw new IllegalArgumentException("Phone number already registered");
        }
        partner.setCreatedDate(new Date());
        return partnerRepository.save(partner);
    }

    public Partner updatePartner(Long id, Partner partnerDetails) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        partner.setName(partnerDetails.getName());
        partner.setPhone(partnerDetails.getPhone());
        partner.setAddress(partnerDetails.getAddress());
        partner.setRegion(partnerDetails.getRegion());
        partner.setWard(partnerDetails.getWard());
        partner.setBranch(partnerDetails.getBranch());
        partner.setEmail(partnerDetails.getEmail());
        partner.setCompany(partnerDetails.getCompany());
        partner.setTaxCode(partnerDetails.getTaxCode());
        partner.setSupplierGroup(partnerDetails.getSupplierGroup());
        partner.setNote(partnerDetails.getNote());

        return partnerRepository.save(partner);
    }

    public void deletePartner(Long id) {
        partnerRepository.deleteById(id);
    }
}
