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
public class FaqDTO {

    private Long id;

    @NotBlank(message = "Question cannot be blank!")
    private String question;

    @NotBlank(message = "Answer cannot be blank!")
    private String answer;
}