package lk.ijse.MainCabService.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lk.ijse.MainCabService.enumeratios.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerReviewDTO {

    private Long id;

    @NotBlank(message = "Customer name cannot be blank!")
    private String customerName;

    @NotBlank(message = "Reviewer role cannot be blank!")
    private String reviewerRole;

    @NotNull(message = "Rating cannot be null!")
    @Min(value = 1, message = "Rating must be at least 1 star!")
    @Max(value = 5, message = "Rating cannot be greater than 5 stars!")
    private int rating;

    @NotBlank(message = "Review message cannot be blank!")
    private String message;

    private ReviewStatus status;
}