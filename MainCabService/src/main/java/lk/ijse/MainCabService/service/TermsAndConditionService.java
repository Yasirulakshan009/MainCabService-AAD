package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.TermsAndConditionDTO;

import java.util.List;

public interface TermsAndConditionService {

    List<TermsAndConditionDTO> getAllTermsAndConditions();
    TermsAndConditionDTO saveTermsAndCondition(TermsAndConditionDTO termsDTO);
    TermsAndConditionDTO updateTermsAndCondition(Long id, TermsAndConditionDTO termsDTO);
    void deleteTermsAndCondition(Long id);
}
