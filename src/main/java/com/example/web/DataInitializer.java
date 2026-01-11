package com.example.web;

import com.example.web.Entity.User;
import com.example.web.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Tạo hoặc cập nhật admin account
        User admin = userRepository.findByUsername("admin").orElse(null);
        
        if (admin == null) {
            // Tạo mới admin account
            admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // Mật khẩu mặc định: admin123
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);
            System.out.println("========================================");
            System.out.println("Admin account created successfully!");
            System.out.println("Username: admin");
            System.out.println("Password: admin123");
            System.out.println("========================================");
        } else {
            // Kiểm tra và cập nhật mật khẩu nếu chưa được mã hóa
            String currentPassword = admin.getPassword();
            if (currentPassword == null || 
                (!currentPassword.startsWith("$2a$") && !currentPassword.startsWith("$2b$"))) {
                // Mật khẩu chưa được mã hóa, cập nhật lại
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ROLE_ADMIN"); // Đảm bảo role đúng
                userRepository.save(admin);
                System.out.println("========================================");
                System.out.println("Admin password has been updated!");
                System.out.println("Username: admin");
                System.out.println("Password: admin123");
                System.out.println("========================================");
            } else {
                System.out.println("Admin account already exists with encrypted password!");
            }
        }
    }
}
