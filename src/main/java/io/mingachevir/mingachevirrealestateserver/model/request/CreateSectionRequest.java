package io.mingachevir.mingachevirrealestateserver.model.request;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateSectionRequest {
    private Long sectionId;
    private String sectionName;
    private String page;
    private List<Long> houseIds;
}
