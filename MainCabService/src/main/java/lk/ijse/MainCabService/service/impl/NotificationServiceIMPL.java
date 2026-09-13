package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.NotificationDTO;
import lk.ijse.MainCabService.entity.Notification;
import lk.ijse.MainCabService.enumeratios.NotificationType;
import lk.ijse.MainCabService.repository.NotificationRepository;
import lk.ijse.MainCabService.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceIMPL implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public void createNotification(NotificationType type, String message, Long referenceId) {
        try {
            Notification notification = new Notification();
            notification.setType(type);
            notification.setMessage(message);
            notification.setReferenceId(referenceId);
            notification.setRead(false);
            notification.setCreatedDate(LocalDateTime.now());

            notificationRepository.save(notification);
            log.info("Notification created: " + message);
        } catch (Exception e) {
            log.error("Error creating notification: " + e.getMessage());
        }
    }

    @Override
    public List<NotificationDTO> getAllNotifications() {
        try {
            List<Notification> list = notificationRepository.findAllByOrderByCreatedDateDesc();
            List<NotificationDTO> dtoList = new ArrayList<>();

            for (Notification n : list) {
                NotificationDTO dto = new NotificationDTO();
                dto.setId(n.getId());
                dto.setMessage(n.getMessage());
                dto.setType(n.getType());
                dto.setReferenceId(n.getReferenceId());
                dto.setRead(n.isRead());
                dto.setCreatedDate(n.getCreatedDate());
                dtoList.add(dto);
            }
            return dtoList;
        } catch (Exception e) {
            log.error("Error in getAllNotifications(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public long getUnreadCount() {
        return notificationRepository.countByIsReadFalse();
    }

    @Override
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + id));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void markAllAsRead() {
        List<Notification> unread = notificationRepository.findAllByOrderByCreatedDateDesc();
        for (Notification n : unread) {
            if (!n.isRead()) {
                n.setRead(true);
            }
        }
        notificationRepository.saveAll(unread);
    }
}