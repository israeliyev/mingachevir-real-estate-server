package io.mingachevir.mingachevirrealestateserver.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserTokenDto {
    private Long id;
    private String token;
    private LocalDateTime issuedDate;
    private LocalDateTime expireDate;
    private UserDto user; // Replacing userId with the actual object
}
