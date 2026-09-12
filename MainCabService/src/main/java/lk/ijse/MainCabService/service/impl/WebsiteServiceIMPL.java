package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.WebsiteSettingDTO;
import lk.ijse.MainCabService.entity.WebsiteSetting;
import lk.ijse.MainCabService.repository.WebsiteSettingRepository;
import lk.ijse.MainCabService.service.WebsiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebsiteServiceIMPL implements WebsiteService {

    private final WebsiteSettingRepository websiteSettingRepository;

    @Override
    public WebsiteSettingDTO getWebsiteSettings() {
        log.info("Executing getWebsiteSettings()");

        try {
            Optional<WebsiteSetting> optionalSetting = websiteSettingRepository.findById(1L);

            if (!optionalSetting.isPresent()) {
                return null;
            }

            WebsiteSetting setting = optionalSetting.get();

            WebsiteSettingDTO dto = new WebsiteSettingDTO();
            dto.setId(setting.getId());
            dto.setCompanyName(setting.getCompanyName());
            dto.setPhoneNumber(setting.getPhoneNumber());
            dto.setWhatsappNumber(setting.getWhatsappNumber());
            dto.setEmail(setting.getEmail());
            dto.setAddress(setting.getAddress());
            dto.setFacebookUrl(setting.getFacebookUrl());
            dto.setInstagramUrl(setting.getInstagramUrl());

            return dto;

        } catch (Exception e) {
            log.error("Error in getWebsiteSettings(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateWebsiteSettings(WebsiteSettingDTO websiteSettingDTO) {
        log.info("Executing updateWebsiteSettings()");

        try {
            Optional<WebsiteSetting> optionalSetting = websiteSettingRepository.findById(1L);

            if (optionalSetting.isPresent()) {
                WebsiteSetting setting = optionalSetting.get();
                setting.setCompanyName(websiteSettingDTO.getCompanyName());
                setting.setPhoneNumber(websiteSettingDTO.getPhoneNumber());
                setting.setWhatsappNumber(websiteSettingDTO.getWhatsappNumber());
                setting.setEmail(websiteSettingDTO.getEmail());
                setting.setAddress(websiteSettingDTO.getAddress());
                setting.setFacebookUrl(websiteSettingDTO.getFacebookUrl());
                setting.setInstagramUrl(websiteSettingDTO.getInstagramUrl());

                websiteSettingRepository.save(setting);
            } else {
                WebsiteSetting setting = new WebsiteSetting();
                setting.setCompanyName(websiteSettingDTO.getCompanyName());
                setting.setPhoneNumber(websiteSettingDTO.getPhoneNumber());
                setting.setWhatsappNumber(websiteSettingDTO.getWhatsappNumber());
                setting.setEmail(websiteSettingDTO.getEmail());
                setting.setAddress(websiteSettingDTO.getAddress());
                setting.setFacebookUrl(websiteSettingDTO.getFacebookUrl());
                setting.setInstagramUrl(websiteSettingDTO.getInstagramUrl());

                websiteSettingRepository.save(setting);
            }

            log.info("Website settings updated successfully!");

        } catch (Exception e) {
            log.error("Error in updateWebsiteSettings(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}