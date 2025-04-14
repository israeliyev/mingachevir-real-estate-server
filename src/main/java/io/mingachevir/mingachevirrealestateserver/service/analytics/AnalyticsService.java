package io.mingachevir.mingachevirrealestateserver.service.analytics;

import io.mingachevir.mingachevirrealestateserver.model.dto.HouseViewSummaryDto;
import io.mingachevir.mingachevirrealestateserver.model.request.GetFilterHousesRequest;
import io.mingachevir.mingachevirrealestateserver.model.response.SiteVisitAnalyticsResponse;
import io.mingachevir.mingachevirrealestateserver.model.response.TrackSearchHousesResponse;
import io.mingachevir.mingachevirrealestateserver.util.GenericResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface AnalyticsService {
    GenericResponse<Void> trackSiteVisit(String visitorIp, String sessionId);
    GenericResponse<Void> trackHouseView(Long houseId, String visitorIp, String sessionId);
    GenericResponse<Void> trackBasketAddition(Long houseId, String visitorIp, String sessionId);
    GenericResponse<Void> trackSearchQuery(GetFilterHousesRequest request, String visitorIp, String sessionId);
    GenericResponse<Page<HouseViewSummaryDto>> getAllHousesByViewCount(int page, int size);
    GenericResponse<Page<HouseViewSummaryDto>> getHousesByBasketCount(int page, int size);
    GenericResponse<TrackSearchHousesResponse> getTrackedSearchHouses();
    GenericResponse<SiteVisitAnalyticsResponse> getSiteVisitAnalytics();
}
