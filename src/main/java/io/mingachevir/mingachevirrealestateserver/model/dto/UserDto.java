package io.mingachevir.mingachevirrealestateserver.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime lastSuccessfulLoginDate;
    private LocalDateTime lastUnsuccessfulLoginDate;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer maxTokenCount;
}
