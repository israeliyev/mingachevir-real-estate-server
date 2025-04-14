package io.mingachevir.mingachevirrealestateserver.model.dto.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BaseActiveDto extends BaseDto {
    @Serial
    private static final long serialVersionUID = 7387470308400029460L;
    private Long id;
    private String name;
    private Boolean enabled;
}
