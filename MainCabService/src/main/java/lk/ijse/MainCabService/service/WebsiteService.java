package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.WebsiteSettingDTO;

public interface WebsiteService {
    WebsiteSettingDTO getWebsiteSettings();
    void updateWebsiteSettings(WebsiteSettingDTO websiteSettingDTO);
}