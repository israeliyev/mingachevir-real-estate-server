package io.mingachevir.mingachevirrealestateserver.model.response;

import io.mingachevir.mingachevirrealestateserver.model.dto.base.BaseIdNameDto;
import io.mingachevir.mingachevirrealestateserver.model.entity.analytics.dto.TrackFilterDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackSearchHousesResponse {
    private List<BaseIdNameDto> parameters;
    private List<TrackFilterDto> filters;
}
