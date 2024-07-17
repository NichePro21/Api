package ManagementSystem.fpt.Services;

import ManagementSystem.fpt.Models.Address;
import ManagementSystem.fpt.Models.Role;
import ManagementSystem.fpt.Models.User;
import ManagementSystem.fpt.Repositories.AccountRepository;
import ManagementSystem.fpt.Repositories.AddressRepository;
import ManagementSystem.fpt.Repositories.RoleRepository;
import ManagementSystem.fpt.Requests.CreateAccountRequest;
import ManagementSystem.fpt.Util.VerificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private VerificationUtil verificationUtil;

    public List<User> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<User> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public User createAccount(CreateAccountRequest createAccountRequest) {
        if (accountRepository.existsByEmail(createAccountRequest.getEmail())) {
            throw new RuntimeException("Email đã được đăng ký trước đó. Vui lòng sử dụng email khác.");
        }
        if (accountRepository.existsByPhone(createAccountRequest.getPhone())) {
            throw new RuntimeException("Số điện thoại đã được đăng ký trước đó");
        }
        // Create user entity
        User newUser = new User();
        newUser.setFirstname(createAccountRequest.getFirstname());
        newUser.setLastname(createAccountRequest.getLastname());
        newUser.setEmail(createAccountRequest.getEmail());
        newUser.setPhone(createAccountRequest.getPhone());
        newUser.setUsername(generateUsername(createAccountRequest.getEmail()));
        newUser.setPassword(BCrypt.hashpw(createAccountRequest.getPassword(), BCrypt.gensalt()));
        newUser.setIsVerified(false); // Optionally set user as inactive until verified
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_EMPLOYEE"); // Assuming you have a role named "ROLE_USER"
        roles.add(userRole);
        newUser.setRoles(roles);
        // Generate and send verification code
        String verificationCode = verificationUtil.generateVerificationCode();
        newUser.setVerificationCode(verificationCode);

        // Save user to database
        User savedUser = accountRepository.save(newUser);
        sendLinkVerified(savedUser.getEmail(), verificationCode);
        // Create address entity with default null values
//        Address newAddress = new Address();
//        newAddress.setUser(savedUser);
//        addressRepository.save(newAddress);


        return savedUser;
    }

    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    public boolean existsByPhone(String phone) {
        return accountRepository.existsByPhone(phone);
    }


    //generate
    private String generateUsername(String email) {
        // Example: if email is email@gmail.com, generate something like email27
        Random random = new Random();
        int suffix = random.nextInt(50) + 1; // Random number between 1 and 50
        return email.split("@")[0] + suffix;
    }

    private void sendVerificationCode(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Xác nhận đăng ký");
        message.setText("Mã xác nhận của bạn là: " + code);
        emailSender.send(message);
    }

    //send link verify
    public void sendLinkVerified(String toEmail, String verificationCode) {
        String resetLink = "http://localhost:4200/verify?email=" + toEmail + "&code=" + verificationCode;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Verify Email");
        mailMessage.setText("Click Here to Verify Email: " + resetLink);
        emailSender.send(mailMessage);
    }

    @Transactional
    public User updateAccountAndRoles(Long id, User updatedUser) {
        User existingUser = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Cập nhật thông tin tài khoản, giữ nguyên giá trị không thay đổi
        if (updatedUser.getFirstname() != null) {
            existingUser.setFirstname(updatedUser.getFirstname());
        }
        if (updatedUser.getLastname() != null) {
            existingUser.setLastname(updatedUser.getLastname());
        }
        if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(BCrypt.hashpw(updatedUser.getPassword(), BCrypt.gensalt()));
        }

        return accountRepository.save(existingUser);
    }


    public User updateRoles(Long id, Set<Role> roles) {
        User user = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRoles(roles);

        return accountRepository.save(user);
    }

    @Transactional
    public void deleteAccount(Long id) {
        User user = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Xóa tất cả các địa chỉ liên quan đến người dùng
        for (Address address : user.getAddresses()) {
            addressRepository.delete(address);
        }

        // Sau đó xóa người dùng
        accountRepository.delete(user);
    }


}
