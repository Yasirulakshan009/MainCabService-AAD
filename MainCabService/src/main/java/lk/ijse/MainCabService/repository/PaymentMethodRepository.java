package lk.ijse.MainCabService.repository;

import lk.ijse.MainCabService.entity.PaymentMethod;
import lk.ijse.MainCabService.enumeratios.Method;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod,Long> {

    PaymentMethod findByPaymentMethod(Method method);
}
