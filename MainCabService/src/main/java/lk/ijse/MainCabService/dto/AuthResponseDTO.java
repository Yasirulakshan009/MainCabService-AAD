package lk.ijse.MainCabService.dto;

import lk.ijse.MainCabService.enumeratios.DashboardSection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String role;
    private List<DashboardSection> permissions;
}