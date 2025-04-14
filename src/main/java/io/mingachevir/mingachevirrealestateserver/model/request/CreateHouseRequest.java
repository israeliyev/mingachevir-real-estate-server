package io.mingachevir.mingachevirrealestateserver.model.request;

import io.mingachevir.mingachevirrealestateserver.util.PriceType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Data
public class CreateHouseRequest {
    // for update
    private Long id;
    List<MultipartFile> deletedMultipartFiles;

    //for requester
    private String requester;
    private Integer number;

    private String name;
    private String description;
    private Double price;
    private PriceType priceType;
    private String location;
    private String type;

    private String coverImage;

    private Long mainCategoryId;
    private Long subCategoryId;


    List<String> imagesPaths;

    private List<Long> selectiveParameterValuesIds;

    private List<CreateInputParameterValueRequest> inputParameterValues;
}
