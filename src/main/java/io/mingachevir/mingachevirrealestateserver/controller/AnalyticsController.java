package io.mingachevir.mingachevirrealestateserver.controller;

import io.mingachevir.mingachevirrealestateserver.model.dto.DiskSpaceInfo;
import io.mingachevir.mingachevirrealestateserver.model.dto.HouseViewSummaryDto;
import io.mingachevir.mingachevirrealestateserver.model.request.GetFilterHousesRequest;
import io.mingachevir.mingachevirrealestateserver.model.response.SiteVisitAnalyticsResponse;
import io.mingachevir.mingachevirrealestateserver.model.response.TrackSearchHousesResponse;
import io.mingachevir.mingachevirrealestateserver.service.analytics.AnalyticsService;
import io.mingachevir.mingachevirrealestateserver.util.GenericResponse;
import io.mingachevir.mingachevirrealestateserver.util.VisitorUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;


@RestController
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PostMapping("/track/site-visit")
    public GenericResponse<Void> trackSiteVisit(@RequestParam String sessionId) {
        HttpServletRequest request = VisitorUtils.getCurrentRequest();
        HttpServletResponse response = VisitorUtils.getCurrentResponse();
        String visitorId = VisitorUtils.generateVisitorId(request, response);
        return analyticsService.trackSiteVisit(visitorId, sessionId);
    }

    @PostMapping("/track/house-view")
    public GenericResponse<Void> trackHouseView(@RequestParam Long houseId, @RequestParam String sessionId) {
        HttpServletRequest request = VisitorUtils.getCurrentRequest();
        HttpServletResponse response = VisitorUtils.getCurrentResponse();
        String visitorId = VisitorUtils.generateVisitorId(request, response);
        return analyticsService.trackHouseView(houseId, visitorId, sessionId);
    }

    @PostMapping("/track/basket-addition")
    public GenericResponse<Void> trackBasketAddition(@RequestParam Long houseId, @RequestParam String sessionId) {
        HttpServletRequest request = VisitorUtils.getCurrentRequest();
        HttpServletResponse response = VisitorUtils.getCurrentResponse();
        String visitorId = VisitorUtils.generateVisitorId(request, response);
        return analyticsService.trackBasketAddition(houseId, visitorId, sessionId);
    }

    @PostMapping("/track/search-query")
    public GenericResponse<Void> trackSearchQuery(@RequestBody GetFilterHousesRequest request, @RequestParam String sessionId) {
        HttpServletRequest requestHttp = VisitorUtils.getCurrentRequest();
        HttpServletResponse response = VisitorUtils.getCurrentResponse();
        String visitorId = VisitorUtils.generateVisitorId(requestHttp, response);
        return analyticsService.trackSearchQuery(request, visitorId, sessionId);
    }

    @GetMapping("/auth/site-visits")
    public GenericResponse<SiteVisitAnalyticsResponse> getSiteVisitsByPeriod() {
        return analyticsService.getSiteVisitAnalytics();
    }

    @GetMapping("/auth/houses/by-view-count")
    public GenericResponse<Page<HouseViewSummaryDto>> getHousesByViewCount(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return analyticsService.getAllHousesByViewCount(page, size);
    }

    @GetMapping("/auth/houses/by-like-count")
    public GenericResponse<Page<HouseViewSummaryDto>> getHousesByBasketCount(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return analyticsService.getHousesByBasketCount(page, size);
    }

    @GetMapping("/auth/searched-filters")
    public GenericResponse<TrackSearchHousesResponse> getMostSearchedFilters() {
        return analyticsService.getTrackedSearchHouses();
    }

    @GetMapping("/auth/disk-space")
    public GenericResponse<DiskSpaceInfo> getDiskSpace() {
        File root = new File("/"); // Or your mounted volume path
        long totalSpace = root.getTotalSpace();
        long freeSpace = root.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;
        return GenericResponse.ok(new DiskSpaceInfo(totalSpace, freeSpace, usedSpace));
    }
}
