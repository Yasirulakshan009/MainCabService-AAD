package lk.ijse.MainCabService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TermsAndConditionDTO {

    private Long id;

    @NotBlank(message = "Heading cannot be blank!")
    private String heading;

    @NotBlank(message = "Content cannot be blank!")
    private String content;
}