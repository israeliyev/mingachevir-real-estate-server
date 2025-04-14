package io.mingachevir.mingachevirrealestateserver.model.request;

import lombok.Getter;

@Getter
public class InputParameterValueRequest {
    Long parameterId;
    Integer min;
    Integer max;
}
