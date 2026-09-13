package lk.ijse.MainCabService.config;

import lk.ijse.MainCabService.entity.PaymentMethod;
import lk.ijse.MainCabService.entity.User;
import lk.ijse.MainCabService.entity.VehicleCategory;
import lk.ijse.MainCabService.entity.UserRole;
import lk.ijse.MainCabService.enumeratios.Category;
import lk.ijse.MainCabService.enumeratios.Method;
import lk.ijse.MainCabService.enumeratios.Role;
import lk.ijse.MainCabService.enumeratios.UserStatus;
import lk.ijse.MainCabService.repository.PaymentMethodRepository;
import lk.ijse.MainCabService.repository.UserRepository;
import lk.ijse.MainCabService.repository.UserRoleRepository;
import lk.ijse.MainCabService.repository.VehicleCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private VehicleCategoryRepository vehicleCategoryRepository;

    @Autowired
    private UserRoleRepository roleRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        for (Category cat : Category.values()) {
            boolean exists = vehicleCategoryRepository.existsByVehicleCategory(cat);
            if (!exists) {
                VehicleCategory vehicleCategory = new VehicleCategory();
                vehicleCategory.setVehicleCategory(cat);
                vehicleCategoryRepository.save(vehicleCategory);
            }
        }
        System.out.println("✅ Vehicle Categories initialized successfully!");

        for (Role r : Role.values()) {
            boolean exists = roleRepository.existsByRole(r);
            if (!exists) {
                UserRole userRole = new UserRole();
                userRole.setRole(r);
                roleRepository.save(userRole);
            }
        }
        System.out.println("✅ User Roles initialized successfully!");

        for (Method m : Method.values()) {
            PaymentMethod existing = paymentMethodRepository.findByPaymentMethod(m);
            if (existing == null) {
                PaymentMethod paymentMethod = new PaymentMethod();
                paymentMethod.setPaymentMethod(m);
                paymentMethodRepository.save(paymentMethod);
            }
        }
        System.out.println("✅ Payment Methods initialized successfully!");


        boolean adminExists = userRepository.existsByUserRole_Role(Role.ADMIN);
        if (!adminExists) {
            UserRole adminRole = roleRepository.findByRole(Role.ADMIN)
                    .orElseThrow(() -> new RuntimeException("ADMIN role not found!"));

            User admin = new User();
            admin.setUserName("Admin");
            admin.setUserEmail("admin@gmail.com");
            admin.setPhone("0770000000");
            admin.setUserPassword(passwordEncoder.encode("Admin123"));
            admin.setStatus(UserStatus.ACTIVE);
            admin.setUserRole(adminRole);
            admin.setPermissions(new ArrayList<>());

            userRepository.save(admin);

            System.out.println("✅ Default ADMIN account created!");
            System.out.println("   Email    : admin@gmail.com");
            System.out.println("   Password : Admin123");
            System.out.println("Please log in and change this password immediately!");

        }
    }
}