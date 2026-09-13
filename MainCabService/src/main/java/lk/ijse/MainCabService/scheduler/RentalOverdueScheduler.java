package lk.ijse.MainCabService.scheduler;

import lk.ijse.MainCabService.entity.Rental;
import lk.ijse.MainCabService.enumeratios.NotificationType;
import lk.ijse.MainCabService.enumeratios.RentalStatus;
import lk.ijse.MainCabService.repository.NotificationRepository;
import lk.ijse.MainCabService.repository.RentalRepository;
import lk.ijse.MainCabService.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class RentalOverdueScheduler {

    private final RentalRepository rentalRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 * * * *")
    public void checkOverdueRentals() {
        log.info("Running scheduled check for overdue rentals...");

        List<Rental> activeRentals = rentalRepository.findByRentalStatus(RentalStatus.ACTIVE);

        for (Rental rental : activeRentals) {
            if (rental.getEndDate().isBefore(LocalDate.now())) {

                boolean alreadyNotified = notificationRepository
                        .existsByTypeAndReferenceId(NotificationType.RENTAL_OVERDUE, rental.getRentalID());

                if (!alreadyNotified) {
                    String vehicleName = rental.getVehicles() != null ? rental.getVehicles().getVehicleModel() : "Vehicle";
                    notificationService.createNotification(
                            NotificationType.RENTAL_OVERDUE,
                            "Rental #" + rental.getRentalID() + " (" + vehicleName + ") is overdue",
                            rental.getRentalID()
                    );
                }
            }
        }
    }
}