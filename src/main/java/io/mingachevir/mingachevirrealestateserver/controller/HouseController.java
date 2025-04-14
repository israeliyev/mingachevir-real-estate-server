package io.mingachevir.mingachevirrealestateserver.controller;


import io.mingachevir.mingachevirrealestateserver.model.dto.BaseHouseRequestDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.BrokerDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.HouseCardDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.HouseDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.HouseRequestDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.SectionDto;
import io.mingachevir.mingachevirrealestateserver.model.request.CreateHouseRequest;
import io.mingachevir.mingachevirrealestateserver.model.request.CreateSectionRequest;
import io.mingachevir.mingachevirrealestateserver.model.request.GetFilterHousesRequest;
import io.mingachevir.mingachevirrealestateserver.service.HouseService;
import io.mingachevir.mingachevirrealestateserver.util.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @GetMapping("/houses-section/{page}")
    public GenericResponse<List<SectionDto>> getHousesbySectionPage(@PathVariable("page") String page) {
        return houseService.getHousesbySectionPage(page);
    }

    @PostMapping("/auth/houses-section")
    public GenericResponse<SectionDto> createSection(@RequestBody CreateSectionRequest createSectionRequest) {
        return houseService.createSection(createSectionRequest);
    }


    @PutMapping("/auth/houses-section")
    public GenericResponse<SectionDto> updateSection(@RequestBody CreateSectionRequest createSectionRequest) {
        return houseService.updateSection(createSectionRequest);
    }


    @DeleteMapping("/auth/houses-section")
    public GenericResponse<?> deleteSection(@RequestParam("sectionId") Long sectionId) {
        return houseService.deleteSection(sectionId);
    }


    @PostMapping("/houses")
    public GenericResponse<List<HouseCardDto>> getHousesByFilter(@RequestBody GetFilterHousesRequest request) {
        return houseService.getHousesByFilter(request);
    }

    @GetMapping("/houses/{houseId}")
    public GenericResponse<HouseDto> getHouseDetail(@PathVariable("houseId") Long houseId) {
        return houseService.getHouseDetails(houseId);
    }


    @GetMapping("/auth/house-requests")
    public GenericResponse<List<HouseRequestDto>> getAllHouseRequests() {
        return houseService.getAllHouseRequests();
    }

    @PostMapping("/house-request")
    public GenericResponse<?> createHouseRequest(@RequestBody HouseRequestDto createHouseRequest) {
        return houseService.createHouseRequest(createHouseRequest);
    }

    @DeleteMapping("/auth/house-request/{houseRequestId}")
    public GenericResponse<?> deleteHouseRequest(@PathVariable("houseRequestId") Long houseRequestId,
                                                 @RequestParam("forSave") Boolean forSave) {
        return houseService.deleteHouseRequest(houseRequestId, forSave);
    }


    @GetMapping("/broker-information")
    public GenericResponse<BrokerDto> getBrokerInformation() {
        return houseService.getBrokerInformation();
    }

    @PostMapping("/auth/broker-information")
    public GenericResponse<?> saveBrokerInformation(@RequestBody BrokerDto broker) {
        return houseService.saveBrokerInformation(broker);
    }

    @PostMapping("/auth/house")
    public GenericResponse<HouseDto> createHouse(@RequestBody HouseDto houseDto) {
        return houseService.createHouse(houseDto);
    }

    @PutMapping("/auth/house/{id}")
    public GenericResponse<HouseDto> updateHouse(@PathVariable Long id, @RequestBody HouseDto houseDto) {
        houseDto.setId(id);
        return houseService.updateHouse(houseDto);
    }

    @GetMapping("/house/{id}")
    public GenericResponse<HouseDto> getHouseById(@PathVariable Long id) {
        return houseService.getHouseById(id);
    }

    @DeleteMapping("/auth/house")
    public GenericResponse<?> deleteHouse(@RequestParam("houseId") Long houseId) {
        return this.houseService.deleteHouse(houseId);
    }
}
