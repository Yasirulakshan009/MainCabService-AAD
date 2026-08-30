package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.FaqDTO;
import lk.ijse.MainCabService.repository.FaqRepository;
import lk.ijse.MainCabService.service.FaqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FaqServiceIMPL implements FaqService {

    private final FaqRepository faqRepository;

    @Override
    public List<FaqDTO> getAllFAQs() {
        return List.of();
    }

    @Override
    public FaqDTO saveFAQ(FaqDTO faqDTO) {
        return null;
    }

    @Override
    public FaqDTO updateFAQ(Long id, FaqDTO faqDTO) {
        return null;
    }

    @Override
    public void deleteFAQ(Long id) {

    }
}
