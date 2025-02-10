package com.security.vinclub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VinclubSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(VinclubSpringbootApplication.class, args);
    }

//    @Override
//    public void run(String... args) {
//        UserModel adminAccount = userRepository.findByUserRoleModel(UserRoleModel.ADMIN);
//
//        if (adminAccount == null) {
//            UserModel userModel = new UserModel();
//
//            userModel.setUserId(UUID.randomUUID().toString().replaceAll("-",""));
//            userModel.setEmail("admin@gmail.com");
//            userModel.setFirstName("Le");
//            userModel.setLastName("Nguyen");
//            userModel.setUserRoleModel(UserRoleModel.ADMIN);
//            userModel.setPassword(new BCryptPasswordEncoder().encode("admin"));
//            userRepository.save(userModel);
//        }
//    }
//    public static void main(String[] args) {
//        SecureRandom random = new SecureRandom();
//        byte[] keyBytes = new byte[32]; // 256 bits (32 bytes) is a common choice for HMAC-SHA256
//        random.nextBytes(keyBytes);
//
//        String secretKey = Base64.getEncoder().encodeToString(keyBytes);
//        System.out.println("Generated SECRET_KEY : " + secretKey);
//    }
}
