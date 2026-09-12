package lk.ijse.MainCabService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebsiteSettingDTO {

    private Long id;

    @NotBlank(message = "Company name cannot be blank!")
    private String companyName;

    @NotBlank(message = "Phone number cannot be blank!")
    @Pattern(regexp = "^07[0-9]{8}$",
            message = "Invalid Sri Lankan phone number"
    )
    private String phoneNumber;

    @NotBlank(message = "WhatsApp number cannot be blank!")
    private String whatsappNumber;

    @Email(message = "Invalid email format!")
    @NotBlank(message = "Email cannot be blank!")
    private String email;

    @NotBlank(message = "Address cannot be blank!")
    private String address;

    private String facebookUrl;
    private String instagramUrl;
}