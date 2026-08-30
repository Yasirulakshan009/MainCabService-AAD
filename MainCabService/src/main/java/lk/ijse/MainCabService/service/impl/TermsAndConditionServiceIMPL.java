package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.TermsAndConditionDTO;
import lk.ijse.MainCabService.repository.TermsAndConditionRepository;
import lk.ijse.MainCabService.service.TermsAndConditionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TermsAndConditionServiceIMPL implements TermsAndConditionService {

    private final TermsAndConditionRepository termsAndConditionRepository;

    @Override
    public List<TermsAndConditionDTO> getAllTermsAndConditions() {
        return List.of();
    }

    @Override
    public TermsAndConditionDTO saveTermsAndCondition(TermsAndConditionDTO termsDTO) {
        return null;
    }

    @Override
    public TermsAndConditionDTO updateTermsAndCondition(Long id, TermsAndConditionDTO termsDTO) {
        return null;
    }

    @Override
    public void deleteTermsAndCondition(Long id) {

    }
}
