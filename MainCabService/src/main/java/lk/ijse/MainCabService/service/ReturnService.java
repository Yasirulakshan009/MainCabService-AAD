package lk.ijse.MainCabService.service;

import lk.ijse.MainCabService.dto.ReturnDTO;
import lk.ijse.MainCabService.enumeratios.ReturnStatus;

import java.util.List;

public interface ReturnService {

    void saveReturn(ReturnDTO returnDTO);
    void updateReturn(ReturnDTO returnDTO);
    void deleteReturn(Long id);
    ReturnDTO getReturnById(Long id);
    List<ReturnDTO> getAllReturns();
    List<ReturnDTO> searchReturns(String keyword);
    List<ReturnDTO> getReturnsByStatus(ReturnStatus returnStatus);
    long getReturnCountByStatus(ReturnStatus returnStatus);
    long getTotalReturnCount();

}
