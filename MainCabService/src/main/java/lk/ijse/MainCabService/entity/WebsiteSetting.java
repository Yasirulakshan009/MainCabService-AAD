package lk.ijse.MainCabService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "website_settings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebsiteSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String phoneNumber;
    private String whatsappNumber;
    private String email;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String facebookUrl;
    private String instagramUrl;
}