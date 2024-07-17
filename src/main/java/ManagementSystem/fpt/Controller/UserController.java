package ManagementSystem.fpt.Controller;

import ManagementSystem.fpt.Models.User;
import ManagementSystem.fpt.Services.AccountService;
import ManagementSystem.fpt.Services.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v3/")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/user-info")
    public ResponseEntity<User> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();

        // Gọi service để lấy thông tin người dùng từ username (hoặc email)
        Optional<User> optionalUser = userService.findUserByEmail(email);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        return ResponseEntity.ok(user);
    }

}

@Data
class RoleToUserForm {
    private String username;
    private String roleName;
}