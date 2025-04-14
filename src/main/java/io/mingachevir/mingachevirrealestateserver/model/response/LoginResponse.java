package io.mingachevir.mingachevirrealestateserver.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
}
