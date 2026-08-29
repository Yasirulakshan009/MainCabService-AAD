package lk.ijse.MainCabService.repository;

import lk.ijse.MainCabService.entity.CustomerReview;
import lk.ijse.MainCabService.enumeratios.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerReviewsRepository extends JpaRepository<CustomerReview,Long> {

    List<CustomerReview> findByStatus(ReviewStatus status);
}
