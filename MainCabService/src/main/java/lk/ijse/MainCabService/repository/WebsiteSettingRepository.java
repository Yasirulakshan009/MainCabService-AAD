package lk.ijse.MainCabService.repository;

import lk.ijse.MainCabService.entity.WebsiteSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebsiteSettingRepository extends JpaRepository<WebsiteSetting, Long> {
}