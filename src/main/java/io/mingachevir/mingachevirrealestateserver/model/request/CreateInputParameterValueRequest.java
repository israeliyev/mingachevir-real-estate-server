package io.mingachevir.mingachevirrealestateserver.model.request;

import lombok.Getter;

@Getter
public class CreateInputParameterValueRequest {
    Long parameterId;
    Integer value;
}
