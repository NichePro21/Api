package ManagementSystem.fpt.Controller;

import ManagementSystem.fpt.Models.Partner;
import ManagementSystem.fpt.Responses.ApiResponse;
import ManagementSystem.fpt.Services.PartnerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partners")
public class PartnerController {

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private ApiResponse apiResponse;

    @GetMapping
    public ResponseEntity<?> getAllPartners() {
        try {
            List<Partner> partners = partnerService.getAllPartners();
            return apiResponse.ok("Fetched all partners successfully", partners);
        } catch (Exception e) {
            return apiResponse.failure(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPartnerById(@PathVariable Long id) {
        try {
            Partner partner = partnerService.getPartnerById(id)
                    .orElseThrow(() -> new RuntimeException("Partner not found"));
            return apiResponse.ok("Fetched partner successfully", partner);
        } catch (RuntimeException e) {
            return apiResponse.failure(e.getMessage(), null, HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            return apiResponse.failure(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }


    @PostMapping
    public ResponseEntity<?> createPartner(@Valid @RequestBody Partner partner, BindingResult rs) {
        try {
            if (rs.hasErrors()) {
                return apiResponse.failure("Invalid", rs.getAllErrors(), HttpStatus.UNPROCESSABLE_ENTITY.value());
            } else {
                Partner createdPartner = partnerService.createPartner(partner);
                return apiResponse.ok("Created customer success", createdPartner);
            }
        } catch (Exception e) {
            return apiResponse.failure(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePartner(@PathVariable Long id, @Valid @RequestBody Partner partnerDetails, BindingResult rs) {
        if (rs.hasErrors()) {
            return apiResponse.failure("Invalid", rs.getAllErrors(), HttpStatus.UNPROCESSABLE_ENTITY.value());
        }

        Partner updatedPartner = partnerService.updatePartner(id, partnerDetails);
        return apiResponse.ok("Updated partner successfully", updatedPartner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePartner(@PathVariable Long id) {
        partnerService.deletePartner(id);
        return apiResponse.ok("Deleted partner successfully", null);
    }
}
