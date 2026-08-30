package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.TermsAndConditionDTO;
import lk.ijse.MainCabService.entity.TermsAndCondition;
import lk.ijse.MainCabService.repository.TermsAndConditionRepository;
import lk.ijse.MainCabService.service.TermsAndConditionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TermsAndConditionServiceIMPL implements TermsAndConditionService {

    private final TermsAndConditionRepository termsAndConditionRepository;

    @Override
    public List<TermsAndConditionDTO> getAllTermsAndConditions() {
        log.info("Executing getAllTermsAndConditions()");
        try {
            List<TermsAndCondition> termsList = termsAndConditionRepository.findAll();
            List<TermsAndConditionDTO> dtoList = new ArrayList<>();

            for (TermsAndCondition terms : termsList) {
                TermsAndConditionDTO dto = new TermsAndConditionDTO();
                dto.setId(terms.getId());
                dto.setHeading(terms.getHeading());
                dto.setContent(terms.getContent());

                dtoList.add(dto);
            }

            log.info("Fetched " + dtoList.size() + " terms and conditions successfully.");
            return dtoList;
        } catch (Exception e) {
            log.error("Error in getAllTermsAndConditions(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public TermsAndConditionDTO saveTermsAndCondition(TermsAndConditionDTO termsDTO) {
        log.info("Executing saveTermsAndCondition()");
        try {
            TermsAndCondition terms = new TermsAndCondition();
            terms.setHeading(termsDTO.getHeading().trim());
            terms.setContent(termsDTO.getContent().trim());

            TermsAndCondition saved = termsAndConditionRepository.save(terms);

            TermsAndConditionDTO dto = new TermsAndConditionDTO();
            dto.setId(saved.getId());
            dto.setHeading(saved.getHeading());
            dto.setContent(saved.getContent());

            log.info("Terms and Condition saved successfully!");
            return dto;
        } catch (Exception e) {
            log.error("Error in saveTermsAndCondition(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public TermsAndConditionDTO updateTermsAndCondition(Long id, TermsAndConditionDTO termsDTO) {
        log.info("Executing updateTermsAndCondition() for ID: " + id);
        try {
            Optional<TermsAndCondition> optionalTerms = termsAndConditionRepository.findById(id);
            if (!optionalTerms.isPresent()) {
                throw new RuntimeException("Terms and Condition not found with ID: " + id);
            }

            TermsAndCondition terms = optionalTerms.get();
            terms.setHeading(termsDTO.getHeading().trim());
            terms.setContent(termsDTO.getContent().trim());

            TermsAndCondition updated = termsAndConditionRepository.save(terms);

            TermsAndConditionDTO dto = new TermsAndConditionDTO();
            dto.setId(updated.getId());
            dto.setHeading(updated.getHeading());
            dto.setContent(updated.getContent());

            log.info("Terms and Condition updated successfully!");
            return dto;
        } catch (Exception e) {
            log.error("Error in updateTermsAndCondition(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTermsAndCondition(Long id) {

        log.info("Executing deleteTermsAndCondition() for ID: " + id);
        try {
            Optional<TermsAndCondition> optionalTerms = termsAndConditionRepository.findById(id);
            if (!optionalTerms.isPresent()) {
                throw new RuntimeException("Terms and Condition not found with ID: " + id);
            }

            termsAndConditionRepository.deleteById(id);
            log.info("Terms and Condition deleted successfully for ID: " + id);
        } catch (Exception e) {
            log.error("Error in deleteTermsAndCondition(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
