package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.PrivacyPolicyDTO;

import java.util.List;

public interface PrivacyPolicyService {

    List<PrivacyPolicyDTO> getAllPrivacyPolicies();
    PrivacyPolicyDTO savePrivacyPolicy(PrivacyPolicyDTO privacyPolicyDTO);
    PrivacyPolicyDTO updatePrivacyPolicy(Long id, PrivacyPolicyDTO privacyPolicyDTO);
    void deletePrivacyPolicy(Long id);
}
