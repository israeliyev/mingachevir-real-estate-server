package io.mingachevir.mingachevirrealestateserver.service;

import io.mingachevir.mingachevirrealestateserver.model.dto.BaseHouseRequestDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.BrokerDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.HouseCardDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.HouseDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.HouseImageDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.HouseRequestDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.SectionDto;
import io.mingachevir.mingachevirrealestateserver.model.request.CreateHouseRequest;
import io.mingachevir.mingachevirrealestateserver.model.request.CreateSectionRequest;
import io.mingachevir.mingachevirrealestateserver.model.request.GetFilterHousesRequest;
import io.mingachevir.mingachevirrealestateserver.util.GenericResponse;
import org.springframework.data.domain.Pageable;

import java.net.http.HttpResponse;
import java.util.List;

public interface HouseService {
    GenericResponse<List<SectionDto>> getHousesbySectionPage(String page);

    GenericResponse<List<HouseCardDto>> getHousesByFilter(GetFilterHousesRequest request);

    GenericResponse<?> createHouseRequest(HouseRequestDto createHouseRequest);

    GenericResponse<?> makePassiveFile(Long fileId);

    GenericResponse<?> deleteFile(Long fileId);

    GenericResponse<SectionDto> createSection(CreateSectionRequest createSectionRequest);

    GenericResponse<SectionDto> updateSection(CreateSectionRequest createSectionRequest);

    GenericResponse<?> deleteSection(Long sectionId);

    GenericResponse<List<HouseRequestDto>> getAllHouseRequests();

    GenericResponse<?> deleteHouseRequest(Long houseRequestId, Boolean forSave);

    GenericResponse<List<HouseImageDto>> getAllActiveFiles(String filter);

    GenericResponse<List<HouseImageDto>> getAllPassiveFiles(String filter);

    GenericResponse<BrokerDto> getBrokerInformation();

    GenericResponse<HouseDto> getHouseDetails(Long houseId);

    GenericResponse<?> saveBrokerInformation(BrokerDto broker);

    GenericResponse<HouseDto> createHouse(HouseDto houseDto);

    GenericResponse<HouseDto> updateHouse(HouseDto houseDto);

    GenericResponse<HouseDto> getHouseById(Long id);

    GenericResponse<?> deleteHouse(Long houseId);
}
