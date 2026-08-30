package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.PrivacyPolicyDTO;
import lk.ijse.MainCabService.repository.PrivacyPolicyRepository;
import lk.ijse.MainCabService.service.PrivacyPolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivacyPolicyServiceIMPL implements PrivacyPolicyService {

    private final PrivacyPolicyRepository privacyPolicyRepository;

    @Override
    public List<PrivacyPolicyDTO> getAllPrivacyPolicies() {
        return List.of();
    }

    @Override
    public PrivacyPolicyDTO savePrivacyPolicy(PrivacyPolicyDTO privacyPolicyDTO) {
        return null;
    }

    @Override
    public PrivacyPolicyDTO updatePrivacyPolicy(Long id, PrivacyPolicyDTO privacyPolicyDTO) {
        return null;
    }

    @Override
    public void deletePrivacyPolicy(Long id) {

    }
}
