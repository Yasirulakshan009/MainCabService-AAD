package lk.ijse.MainCabService.controller;

import lk.ijse.MainCabService.dto.ReturnDTO;
import lk.ijse.MainCabService.enumeratios.ReturnStatus;
import lk.ijse.MainCabService.service.ReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/returns")
public class ReturnController {

    private final ReturnService returnService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveReturn(@RequestBody ReturnDTO returnDTO) {
        returnService.saveReturn(returnDTO);
        return "Return saved successfully";
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateReturn(@RequestBody ReturnDTO returnDTO) {
        returnService.updateReturn(returnDTO);
        return "Return updated successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteReturn(@PathVariable Long id) {
        returnService.deleteReturn(id);
        return "Return deleted successfully";
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnDTO getReturnById(@PathVariable Long id) {
        return returnService.getReturnById(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReturnDTO> getAllReturns() {
        return returnService.getAllReturns();
    }

    @GetMapping(value = "/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReturnDTO> getReturnsByStatus(@PathVariable ReturnStatus status) {
        return returnService.getReturnsByStatus(status);
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReturnDTO> searchReturns(@RequestParam("keyword") String keyword) {
        return returnService.searchReturns(keyword);
    }

    @GetMapping(value = "/count/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public long getReturnCountByStatus(@PathVariable ReturnStatus status) {
        return returnService.getReturnCountByStatus(status);
    }

    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public long getTotalReturnCount() {
        return returnService.getTotalReturnCount();
    }
}
