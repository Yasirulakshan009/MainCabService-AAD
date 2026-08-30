package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.CustomerReviewDTO;
import lk.ijse.MainCabService.entity.CustomerReview;
import lk.ijse.MainCabService.enumeratios.ReviewStatus;
import lk.ijse.MainCabService.repository.CustomerReviewsRepository;
import lk.ijse.MainCabService.service.CustomerReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerReviewServiceIMPL implements CustomerReviewService {

    private final CustomerReviewsRepository customerReviewsRepository;

    @Override
    public List<CustomerReviewDTO> getAllReviews() {

        log.info("Executing getAllReviews()");
        try {
            List<CustomerReview> reviewList = customerReviewsRepository.findAll();
            List<CustomerReviewDTO> dtoList = new ArrayList<>();

            for (CustomerReview review : reviewList) {
                CustomerReviewDTO dto = new CustomerReviewDTO();
                dto.setId(review.getId());
                dto.setCustomerName(review.getCustomerName());
                dto.setReviewerRole(review.getReviewerRole());
                dto.setRating(review.getRating());
                dto.setMessage(review.getMessage());
                dto.setStatus(review.getStatus());

                dtoList.add(dto);
            }

            log.info("Fetched " + dtoList.size() + " reviews successfully.");
            return dtoList;
        } catch (Exception e) {
            log.error("Error in getAllReviews(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomerReviewDTO saveReview(CustomerReviewDTO reviewDTO) {

        log.info("Executing saveReview()");
        try {
            CustomerReview review = new CustomerReview();
            review.setCustomerName(reviewDTO.getCustomerName().trim());
            review.setReviewerRole(reviewDTO.getReviewerRole().trim());
            review.setRating(reviewDTO.getRating());
            review.setMessage(reviewDTO.getMessage().trim());
            review.setStatus(ReviewStatus.PENDING);

            CustomerReview saved = customerReviewsRepository.save(review);

            CustomerReviewDTO dto = new CustomerReviewDTO();
            dto.setId(saved.getId());
            dto.setCustomerName(saved.getCustomerName());
            dto.setReviewerRole(saved.getReviewerRole());
            dto.setRating(saved.getRating());
            dto.setMessage(saved.getMessage());
            dto.setStatus(saved.getStatus());

            log.info("Review saved successfully!");
            return dto;
        } catch (Exception e) {
            log.error("Error in saveReview(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomerReviewDTO updateReviewStatus(Long id, ReviewStatus status) {
        log.info("Executing updateReviewStatus() for ID: " + id);
        try {
            Optional<CustomerReview> optionalReview = customerReviewsRepository.findById(id);
            if (!optionalReview.isPresent()) {
                throw new RuntimeException("Review not found with ID: " + id);
            }

            CustomerReview review = optionalReview.get();
            review.setStatus(status);

            CustomerReview updated = customerReviewsRepository.save(review);

            CustomerReviewDTO dto = new CustomerReviewDTO();
            dto.setId(updated.getId());
            dto.setCustomerName(updated.getCustomerName());
            dto.setReviewerRole(updated.getReviewerRole());
            dto.setRating(updated.getRating());
            dto.setMessage(updated.getMessage());
            dto.setStatus(updated.getStatus());

            log.info("Review status updated successfully!");
            return dto;
        } catch (Exception e) {
            log.error("Error in updateReviewStatus(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteReview(Long id) {

        log.info("Executing deleteReview() for ID: " + id);

        try {
            Optional<CustomerReview> optionalReview = customerReviewsRepository.findById(id);
            if (!optionalReview.isPresent()) {
                throw new RuntimeException("Review not found with ID: " + id);
            }

            customerReviewsRepository.deleteById(id);
            log.info("Review deleted successfully for ID: " + id);

        } catch (Exception e) {
            log.error("Error in deleteReview(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
