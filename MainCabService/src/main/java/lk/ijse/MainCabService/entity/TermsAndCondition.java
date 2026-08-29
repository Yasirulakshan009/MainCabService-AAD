package lk.ijse.MainCabService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "terms_and_condition")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TermsAndCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String heading;
    private String content;
}
