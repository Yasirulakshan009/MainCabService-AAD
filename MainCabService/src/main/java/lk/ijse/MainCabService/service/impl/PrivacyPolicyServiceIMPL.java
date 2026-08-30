package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.PrivacyPolicyDTO;
import lk.ijse.MainCabService.entity.PrivacyPolicy;
import lk.ijse.MainCabService.repository.PrivacyPolicyRepository;
import lk.ijse.MainCabService.service.PrivacyPolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivacyPolicyServiceIMPL implements PrivacyPolicyService {

    private final PrivacyPolicyRepository privacyPolicyRepository;

    @Override
    public List<PrivacyPolicyDTO> getAllPrivacyPolicies() {
        log.info("Executing getAllPrivacyPolicies()");
        try {
            List<PrivacyPolicy> policyList = privacyPolicyRepository.findAll();
            List<PrivacyPolicyDTO> dtoList = new ArrayList<>();

            for (PrivacyPolicy policy : policyList) {
                PrivacyPolicyDTO dto = new PrivacyPolicyDTO();
                dto.setId(policy.getId());
                dto.setHeading(policy.getHeading());
                dto.setContent(policy.getContent());

                dtoList.add(dto);
            }

            log.info("Fetched " + dtoList.size() + " privacy policies successfully.");
            return dtoList;
        } catch (Exception e) {
            log.error("Error in getAllPrivacyPolicies(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public PrivacyPolicyDTO savePrivacyPolicy(PrivacyPolicyDTO privacyPolicyDTO) {
        log.info("Executing savePrivacyPolicy()");
        try {
            PrivacyPolicy policy = new PrivacyPolicy();
            policy.setHeading(privacyPolicyDTO.getHeading().trim());
            policy.setContent(privacyPolicyDTO.getContent().trim());

            PrivacyPolicy saved = privacyPolicyRepository.save(policy);

            PrivacyPolicyDTO dto = new PrivacyPolicyDTO();
            dto.setId(saved.getId());
            dto.setHeading(saved.getHeading());
            dto.setContent(saved.getContent());

            log.info("Privacy Policy saved successfully!");
            return dto;
        } catch (Exception e) {
            log.error("Error in savePrivacyPolicy(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public PrivacyPolicyDTO updatePrivacyPolicy(Long id, PrivacyPolicyDTO privacyPolicyDTO) {
        log.info("Executing updatePrivacyPolicy() for ID: " + id);
        try {
            Optional<PrivacyPolicy> optionalPolicy = privacyPolicyRepository.findById(id);
            if (!optionalPolicy.isPresent()) {
                throw new RuntimeException("Privacy Policy not found with ID: " + id);
            }

            PrivacyPolicy policy = optionalPolicy.get();
            policy.setHeading(privacyPolicyDTO.getHeading().trim());
            policy.setContent(privacyPolicyDTO.getContent().trim());

            PrivacyPolicy updated = privacyPolicyRepository.save(policy);

            PrivacyPolicyDTO dto = new PrivacyPolicyDTO();
            dto.setId(updated.getId());
            dto.setHeading(updated.getHeading());
            dto.setContent(updated.getContent());

            log.info("Privacy Policy updated successfully!");
            return dto;
        } catch (Exception e) {
            log.error("Error in updatePrivacyPolicy(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePrivacyPolicy(Long id) {

        log.info("Executing deletePrivacyPolicy() for ID: " + id);
        try {
            Optional<PrivacyPolicy> optionalPolicy = privacyPolicyRepository.findById(id);
            if (!optionalPolicy.isPresent()) {
                throw new RuntimeException("Privacy Policy not found with ID: " + id);
            }

            privacyPolicyRepository.deleteById(id);
            log.info("Privacy Policy deleted successfully for ID: " + id);
        } catch (Exception e) {
            log.error("Error in deletePrivacyPolicy(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
