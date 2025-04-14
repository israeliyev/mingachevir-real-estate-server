package io.mingachevir.mingachevirrealestateserver.model.request;

import io.mingachevir.mingachevirrealestateserver.util.OrderEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Setter
@Data
public class GetFilterHousesRequest {
    private OrderEnum sort;
    private String searchKey;
    private Long mainCategoryId;
    private Long subCategoryId;
    private List<Long> selectiveParameterIds;
    private List<InputParameterValueRequest> inputParametersRanges;
    private Integer pageNumber;
 }
