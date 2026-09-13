package lk.ijse.MainCabService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactMessageDTO {

    @NotBlank(message = "Name cannot be blank!")
    private String name;

    @NotBlank(message = "Email cannot be blank!")
    @Email(message = "Invalid email address!")
    private String email;

    private String phone;

    @NotBlank(message = "Subject cannot be blank!")
    private String subject;

    @NotBlank(message = "Message cannot be blank!")
    private String message;
}