package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.FaqDTO;

import java.util.List;

public interface FaqService {

    List<FaqDTO> getAllFAQs();
    FaqDTO saveFAQ(FaqDTO faqDTO);
    FaqDTO updateFAQ(Long id, FaqDTO faqDTO);
    void deleteFAQ(Long id);
}
