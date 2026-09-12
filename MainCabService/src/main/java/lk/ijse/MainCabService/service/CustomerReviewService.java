package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.CustomerReviewDTO;
import lk.ijse.MainCabService.enumeratios.ReviewStatus;

import java.util.List;

public interface CustomerReviewService {

    List<CustomerReviewDTO> getAllReviews();
    List<CustomerReviewDTO> getApprovedReviews();
    CustomerReviewDTO saveReview(CustomerReviewDTO reviewDTO);
    CustomerReviewDTO updateReviewStatus(Long id, ReviewStatus status);
    void deleteReview(Long id);
}
