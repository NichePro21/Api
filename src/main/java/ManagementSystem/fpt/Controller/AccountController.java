package ManagementSystem.fpt.Controller;

import ManagementSystem.fpt.Models.Role;
import ManagementSystem.fpt.Models.User;
import ManagementSystem.fpt.Repositories.AccountRepository;
import ManagementSystem.fpt.Requests.CreateAccountRequest;
import ManagementSystem.fpt.Responses.ApiResponse;
import ManagementSystem.fpt.Services.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/accounts")
public class AccountController extends ApiController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ApiResponse apiResponse;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping
    public ResponseEntity<?> getAllAccounts() {
        List<User> users = accountService.getAllAccounts();
        return apiResponse.ok("Successfully retrieved all accounts", users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        User user = accountService.getAccountById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return apiResponse.ok("Successfully retrieved account", user);
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody @Valid CreateAccountRequest createAccountRequest, BindingResult rs) {
        try {
            if (rs.hasErrors()) {
                return this.apiResponse.failure("invalid", this.parseFieldErrors(rs), HttpStatus.UNPROCESSABLE_ENTITY.value());
            }

            // Kiểm tra xem email đã được đăng ký hay chưa
            if (accountService.existsByEmail(createAccountRequest.getEmail())) {
                return this.apiResponse.failure("Email đã được đăng ký", HttpStatus.BAD_REQUEST.value());
            }

            if (accountService.existsByPhone(createAccountRequest.getPhone())) {
                return this.apiResponse.failure("Số điện thoại đã được đăng ký", HttpStatus.BAD_REQUEST.value());
            }

            User user = this.accountService.createAccount(createAccountRequest);
            return this.apiResponse.ok("Created User Success", user);
        } catch (Exception var4) {
            var4.printStackTrace();
            return this.apiResponse.failure("Error");
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(
            @PathVariable Long id,
            @RequestBody User updatedUser,
            BindingResult rs) {
        try {
            if (rs.hasErrors()) {
                return apiResponse.failure("Invalid input", rs.getAllErrors(), HttpStatus.UNPROCESSABLE_ENTITY.value());
            }

            User user = accountService.updateAccountAndRoles(id, updatedUser);
            return apiResponse.ok("Successfully updated account", user);
        } catch (RuntimeException e) {
            return apiResponse.failure(e.getMessage(), null, HttpStatus.BAD_REQUEST.value());
        } catch (Exception e) {
            return apiResponse.failure("An unexpected error occurred: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }


    @PutMapping("/{id}/roles")
    public ResponseEntity<?> updateRoles(@PathVariable Long id, @RequestBody Set<Role> roles) {
        try {
            User updatedUser = accountService.updateRoles(id, roles);
            return apiResponse.ok("Successfully updated roles", updatedUser);
        } catch (RuntimeException e) {
            return apiResponse.failure(e.getMessage(), null, HttpStatus.BAD_REQUEST.value());
        } catch (Exception e) {
            return apiResponse.failure("An unexpected error occurred: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        try {
                accountService.deleteAccount(id);
            return apiResponse.ok("Successfully deleted account");
        } catch (RuntimeException e) {
            return apiResponse.failure(e.getMessage(), null, HttpStatus.BAD_REQUEST.value());
        } catch (Exception e) {
            return apiResponse.failure("An unexpected error occurred: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
