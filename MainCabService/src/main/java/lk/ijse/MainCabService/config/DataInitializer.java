package lk.ijse.MainCabService.config;

import lk.ijse.MainCabService.entity.PaymentMethod;
import lk.ijse.MainCabService.entity.VehicleCategory;
import lk.ijse.MainCabService.entity.UserRole;
import lk.ijse.MainCabService.enumeratios.Category;
import lk.ijse.MainCabService.enumeratios.Method;
import lk.ijse.MainCabService.enumeratios.Role;
import lk.ijse.MainCabService.repository.PaymentMethodRepository;
import lk.ijse.MainCabService.repository.UserRoleRepository;
import lk.ijse.MainCabService.repository.VehicleCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private VehicleCategoryRepository vehicleCategoryRepository;

    @Autowired
    private UserRoleRepository roleRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

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

    }
}