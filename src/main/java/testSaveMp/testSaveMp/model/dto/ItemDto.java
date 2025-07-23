package testSaveMp.testSaveMp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private String name;
    private String description;
    private String fileLink;
    private LocalDateTime data;
    private String category;
}
