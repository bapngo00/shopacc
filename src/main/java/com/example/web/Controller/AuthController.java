package com.example.web.Controller;

import com.example.web.Entity.User;
import com.example.web.Repository.UserRepository;
import com.example.web.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> req) {

        String username = req.get("username");
        String password = req.get("password");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tài khoản"));

        // Kiểm tra nếu mật khẩu chưa được mã hóa (plain text) - để tương thích với dữ liệu cũ
        String storedPassword = user.getPassword();
        boolean passwordMatches = false;

        // Nếu mật khẩu bắt đầu bằng $2a$ hoặc $2b$ thì là BCrypt hash
        if (storedPassword != null && (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$"))) {
            passwordMatches = encoder.matches(password, storedPassword);
        } else {
            // Nếu là plain text, so sánh trực tiếp (chỉ để tương thích)
            passwordMatches = password.equals(storedPassword);
            // Nếu đúng, cập nhật lại mật khẩu đã mã hóa
            if (passwordMatches) {
                user.setPassword(encoder.encode(password));
                userRepository.save(user);
            }
        }

        if (!passwordMatches) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai mật khẩu");
        }

        String token = JwtUtil.generateToken(user.getUsername(), user.getRole());

        return Map.of(
                "token", token,
                "role", user.getRole()
        );
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> req) {
        String username = req.get("username");
        String password = req.get("password");

        // Validate dữ liệu đầu vào
        if (username == null || username.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên đăng nhập không được để trống");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu không được để trống");
        }

        if (password.length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu phải có ít nhất 6 ký tự");
        }

        // Kiểm tra username đã tồn tại chưa
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tên đăng nhập đã tồn tại");
        }

        // Tạo user mới với role mặc định là ROLE_USER (khách)
        User newUser = new User();
        newUser.setUsername(username.trim());
        newUser.setPassword(encoder.encode(password)); // Mã hóa mật khẩu
        newUser.setRole("ROLE_USER"); // Luôn set role là USER, không cho phép tạo admin

        userRepository.save(newUser);

        return Map.of(
                "message", "Đăng ký thành công",
                "username", username
        );
    }

    @PostMapping("/reset-admin-password")
    public Map<String, String> resetAdminPassword(@RequestBody Map<String, String> req) {
        String newPassword = req.get("password");
        if (newPassword == null || newPassword.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu không được để trống");
        }

        User admin = userRepository.findByUsername("admin")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy tài khoản admin"));

        admin.setPassword(encoder.encode(newPassword));
        userRepository.save(admin);

        return Map.of("message", "Đã reset mật khẩu admin thành công");
    }
}
