package lk.ijse.MainCabService.repository;

import lk.ijse.MainCabService.entity.Notification;
import lk.ijse.MainCabService.enumeratios.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    long countByIsReadFalse();

    List<Notification> findAllByOrderByCreatedDateDesc();

    boolean existsByTypeAndReferenceId(NotificationType type, Long referenceId);
}