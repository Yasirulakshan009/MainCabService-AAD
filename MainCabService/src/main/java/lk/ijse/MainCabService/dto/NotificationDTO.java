package lk.ijse.MainCabService.dto;

import lk.ijse.MainCabService.enumeratios.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

    private Long id;
    private String message;
    private NotificationType type;
    private Long referenceId;
    private boolean isRead;
    private LocalDateTime createdDate;
}