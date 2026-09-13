package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.NotificationDTO;
import lk.ijse.MainCabService.enumeratios.NotificationType;

import java.util.List;

public interface NotificationService {

    void createNotification(NotificationType type, String message, Long referenceId);

    List<NotificationDTO> getAllNotifications();

    long getUnreadCount();

    void markAsRead(Long id);

    void markAllAsRead();
}