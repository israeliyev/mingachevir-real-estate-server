package io.mingachevir.mingachevirrealestateserver.service.analytics.impl;

import io.mingachevir.mingachevirrealestateserver.model.dto.HouseViewSummaryDto;
import io.mingachevir.mingachevirrealestateserver.model.dto.base.BaseIdNameDto;
import io.mingachevir.mingachevirrealestateserver.model.entity.House;
import io.mingachevir.mingachevirrealestateserver.model.entity.MainCategory;
import io.mingachevir.mingachevirrealestateserver.model.entity.Parameters;
import io.mingachevir.mingachevirrealestateserver.model.entity.SelectiveParameterValue;
import io.mingachevir.mingachevirrealestateserver.model.entity.SubCategory;
import io.mingachevir.mingachevirrealestateserver.model.entity.analytics.BasketAddition;
import io.mingachevir.mingachevirrealestateserver.model.entity.analytics.HouseView;
import io.mingachevir.mingachevirrealestateserver.model.entity.analytics.InputParameterRange;
import io.mingachevir.mingachevirrealestateserver.model.entity.analytics.SearchQuery;
import io.mingachevir.mingachevirrealestateserver.model.entity.analytics.SiteVisit;
import io.mingachevir.mingachevirrealestateserver.model.entity.analytics.dto.TrackFilterDto;
import io.mingachevir.mingachevirrealestateserver.model.request.GetFilterHousesRequest;
import io.mingachevir.mingachevirrealestateserver.model.response.SiteVisitAnalyticsResponse;
import io.mingachevir.mingachevirrealestateserver.model.response.TrackSearchHousesResponse;
import io.mingachevir.mingachevirrealestateserver.model.response.UserStats;
import io.mingachevir.mingachevirrealestateserver.repository.HouseJpaRepository;
import io.mingachevir.mingachevirrealestateserver.repository.MainCategoryJpaRespository;
import io.mingachevir.mingachevirrealestateserver.repository.ParameterJpaRepository;
import io.mingachevir.mingachevirrealestateserver.repository.SubCategoryJpaRepository;
import io.mingachevir.mingachevirrealestateserver.repository.analytics.BasketAdditionRepository;
import io.mingachevir.mingachevirrealestateserver.repository.analytics.HouseViewRepository;
import io.mingachevir.mingachevirrealestateserver.repository.analytics.InputParameterRangeRepository;
import io.mingachevir.mingachevirrealestateserver.repository.analytics.SearchQueryRepository;
import io.mingachevir.mingachevirrealestateserver.repository.analytics.SiteVisitRepository;
import io.mingachevir.mingachevirrealestateserver.service.analytics.AnalyticsService;
import io.mingachevir.mingachevirrealestateserver.util.CustomServiceException;
import io.mingachevir.mingachevirrealestateserver.util.GenericResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AnalyticsServiceImpl implements AnalyticsService {

    private static final ZoneId AZERBAIJAN_ZONE = ZoneId.of("Asia/Baku");

    private final SiteVisitRepository siteVisitRepository;
    private final HouseViewRepository houseViewRepository;
    private final BasketAdditionRepository basketAdditionRepository;
    private final SearchQueryRepository searchQueryRepository;
    private final HouseJpaRepository houseRepository;

    @Transactional
    @Override
    public GenericResponse<Void> trackSiteVisit(String visitorIp, String sessionId) {
        try {
            SiteVisit siteVisit = new SiteVisit();
            siteVisit.setVisitDateTime(new Date());
            siteVisit.setVisitorIp(visitorIp);
            siteVisit.setSessionId(sessionId);
            siteVisit.setEnabled(true);
            siteVisit.setIsDeleted(false);
            siteVisitRepository.save(siteVisit);
            return GenericResponse.ok();
        } catch (Exception e) {
            throw new CustomServiceException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public GenericResponse<Void> trackHouseView(Long houseId, String visitorIp, String sessionId) {
        try {
            House house = houseRepository.findById(houseId).orElse(null);
            if (house == null) {
                return GenericResponse.resourceNotFound();
            }
            HouseView houseView = new HouseView();
            houseView.setHouse(house);
            houseView.setViewDateTime(new Date());
            houseView.setVisitorIp(visitorIp);
            houseView.setSessionId(sessionId);
            houseView.setEnabled(true);
            houseView.setIsDeleted(false);
            houseViewRepository.save(houseView);
            return GenericResponse.ok();
        } catch (Exception e) {
            throw new CustomServiceException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public GenericResponse<Void> trackBasketAddition(Long houseId, String visitorIp, String sessionId) {
        try {
            House house = houseRepository.findById(houseId).orElse(null);
            if (house == null) {
                return GenericResponse.resourceNotFound();
            }
            BasketAddition basketAddition = new BasketAddition();
            basketAddition.setHouse(house);
            basketAddition.setAdditionDateTime(new Date());
            basketAddition.setVisitorIp(visitorIp);
            basketAddition.setSessionId(sessionId);
            basketAddition.setEnabled(true);
            basketAddition.setIsDeleted(false);
            basketAdditionRepository.save(basketAddition);
            return GenericResponse.ok();
        } catch (Exception e) {
            throw new CustomServiceException("Database error while tracking basket addition", e.getMessage());
        }
    }

    @Transactional
    @Override
    public GenericResponse<Void> trackSearchQuery(GetFilterHousesRequest request, String visitorIp, String sessionId) {
        try {
            SearchQuery searchQuery = new SearchQuery();
            searchQuery.setSearchKey(request.getSearchKey());
            searchQuery.setMainCategory(request.getMainCategoryId() != null ? new MainCategory() {{
                setId(request.getMainCategoryId());
            }} : null);
            searchQuery.setSubCategory(request.getSubCategoryId() != null ? new SubCategory() {{
                setId(request.getSubCategoryId());
            }} : null);
            searchQuery.setSearchDateTime(new Date());
            searchQuery.setVisitorIp(visitorIp);
            searchQuery.setSessionId(sessionId);
            searchQuery.setEnabled(true);
            searchQuery.setIsDeleted(false);

            if (request.getSelectiveParameterIds() != null && !request.getSelectiveParameterIds().isEmpty()) {
                Set<SelectiveParameterValue> selectiveParameterValues = request.getSelectiveParameterIds().stream()
                        .map(id -> new SelectiveParameterValue() {{
                            setId(id);
                        }})
                        .collect(Collectors.toSet());
                searchQuery.setSelectiveParameterValues(selectiveParameterValues);
            }

            if (request.getInputParametersRanges() != null && !request.getInputParametersRanges().isEmpty()) {
                List<InputParameterRange> inputParameterRanges = request.getInputParametersRanges().stream()
                        .map(range -> {
                            InputParameterRange ipr = new InputParameterRange();
                            ipr.setParameter(new Parameters() {{
                                setId(range.getParameterId());
                            }});
                            ipr.setMinValue(range.getMin());
                            ipr.setMaxValue(range.getMax());
                            ipr.setSearchQuery(searchQuery);
                            ipr.setEnabled(true);
                            ipr.setIsDeleted(false);
                            return ipr;
                        })
                        .collect(Collectors.toList());
                searchQuery.setInputParameterRanges(inputParameterRanges);
            }

            searchQueryRepository.save(searchQuery);
            return GenericResponse.ok();
        } catch (Exception e) {
            throw new CustomServiceException("Database error while tracking search query", e.getMessage());
        }
    }

    @Override
    public GenericResponse<Page<HouseViewSummaryDto>> getAllHousesByViewCount(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<HouseViewSummaryDto> houses = houseViewRepository.findAllHousesByViewCount(pageable);
            return GenericResponse.ok(houses);
        } catch (Exception e) {
            throw new CustomServiceException("Database error while fetching houses by view count", e.getMessage());
        }
    }

    @Override
    public GenericResponse<Page<HouseViewSummaryDto>> getHousesByBasketCount(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<HouseViewSummaryDto> houses = basketAdditionRepository.findAllHousesByLikeCount(pageable);
            return GenericResponse.ok(houses);
        } catch (Exception e) {
            throw new CustomServiceException("Database error while fetching houses by basket count", e.getMessage());
        }
    }

    @Override
    public GenericResponse<TrackSearchHousesResponse> getTrackedSearchHouses() {
        try {
            List<SearchQuery> searchQueries = searchQueryRepository.findAll();

            Set<BaseIdNameDto> parameters = new TreeSet<>(Comparator.comparing(BaseIdNameDto::getId));
            searchQueries.forEach(query -> {
                query.getSelectiveParameterValues().forEach(spv ->
                        parameters.add(new BaseIdNameDto(spv.getParameter().getId(), spv.getParameter().getName())));
                query.getInputParameterRanges().forEach(ipr ->
                        parameters.add(new BaseIdNameDto(ipr.getParameter().getId(), ipr.getParameter().getName())));
            });

            Map<String, TrackFilterDto> filterMap = new LinkedHashMap<>();
            searchQueries.forEach(query -> {
                String key = (query.getSearchKey() != null ? query.getSearchKey() : "") + "|" +
                        (query.getMainCategory() != null ? query.getMainCategory().getName() : "") + "|" +
                        (query.getSubCategory() != null ? query.getSubCategory().getName() : "");

                TrackFilterDto dto = filterMap.computeIfAbsent(key, k -> new TrackFilterDto(
                        0L, query.getSearchKey(),
                        query.getMainCategory() != null ? query.getMainCategory().getName() : null,
                        query.getSubCategory() != null ? query.getSubCategory().getName() : null,
                        new ArrayList<>()
                ));

                dto.setCount(dto.getCount() + 1);

                List<BaseIdNameDto> paramValues = new ArrayList<>();
                query.getSelectiveParameterValues().forEach(spv ->
                        paramValues.add(new BaseIdNameDto(spv.getParameter().getId(), spv.getName())));
                query.getInputParameterRanges().forEach(ipr ->
                        paramValues.add(new BaseIdNameDto(ipr.getParameter().getId(),
                                ipr.getMinValue() + "-" + ipr.getMaxValue())));
                paramValues.sort(Comparator.comparing(BaseIdNameDto::getId));
                dto.setParameterValues(paramValues);
            });

            return GenericResponse.ok(new TrackSearchHousesResponse(new ArrayList<>(parameters), new ArrayList<>(filterMap.values())));
        } catch (Exception e) {
            throw new CustomServiceException("Database error while fetching tracked search houses", e.getMessage());
        }
    }

    @Override
    public GenericResponse<SiteVisitAnalyticsResponse> getSiteVisitAnalytics() {
        try {
            LocalDateTime now = LocalDateTime.now(AZERBAIJAN_ZONE);
            Date today = Date.from(now.atZone(AZERBAIJAN_ZONE).toInstant());

            long totalUsers = calculateTotalUsers(today);
            long oldUsers = calculateOldUsers(today);
            long newUsers = calculateNewUsers(today);

            List<UserStats> dailyStats = calculateDailyStats(now);
            List<UserStats> weeklyStats = calculateWeeklyStats(now);
            List<UserStats> monthlyStats = calculateMonthlyStats(now);
            List<UserStats> yearlyStats = calculateYearlyStats();

            return GenericResponse.ok(new SiteVisitAnalyticsResponse(totalUsers, oldUsers, newUsers, dailyStats, weeklyStats, monthlyStats, yearlyStats));
        } catch (Exception e) {
            throw new CustomServiceException("Database error while fetching site visit analytics", e.getMessage());
        }
    }

    private long calculateTotalUsers(Date today) {
        List<String> totalVisitorIps = siteVisitRepository.findDistinctVisitorIpByVisitDateTimeBefore(today);
        return totalVisitorIps.size();
    }

    private long calculateOldUsers(Date today) {
        LocalDateTime startOfToday = LocalDateTime.now(AZERBAIJAN_ZONE).truncatedTo(ChronoUnit.DAYS);
        Date startOfTodayDate = Date.from(startOfToday.atZone(AZERBAIJAN_ZONE).toInstant());
        List<String> oldVisitorIps = siteVisitRepository.findDistinctVisitorIpByVisitDateTimeBefore(startOfTodayDate);
        return oldVisitorIps.size();
    }

    private long calculateNewUsers(Date today) {
        LocalDateTime startOfToday = LocalDateTime.now(AZERBAIJAN_ZONE).truncatedTo(ChronoUnit.DAYS);
        Date startOfTodayDate = Date.from(startOfToday.atZone(AZERBAIJAN_ZONE).toInstant());
        List<String> oldVisitorIps = siteVisitRepository.findDistinctVisitorIpByVisitDateTimeBefore(startOfTodayDate);
        List<String> todayVisitorIps = siteVisitRepository.findDistinctVisitorIpByVisitDateTimeBetween(startOfTodayDate, today);
        return todayVisitorIps.stream().filter(ip -> !oldVisitorIps.contains(ip)).count();
    }

    private List<UserStats> calculateDailyStats(LocalDateTime now) {
        LocalDateTime start = now.minusHours(24);
        DateTimeFormatter clockFormatter = DateTimeFormatter.ofPattern("HH");
        List<UserStats> dailyStats = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            LocalDateTime sectionStart = start.plusHours(i * 4);
            LocalDateTime sectionEnd = sectionStart.plusHours(4);
            Date sectionStartDate = Date.from(sectionStart.atZone(AZERBAIJAN_ZONE).toInstant());
            Date sectionEndDate = Date.from(sectionEnd.atZone(AZERBAIJAN_ZONE).toInstant());

            List<String> visitorIps = siteVisitRepository.findDistinctVisitorIpByVisitDateTimeBetween(sectionStartDate, sectionEndDate);
            long userCount = visitorIps.size();

            String time = sectionStart.format(clockFormatter) + "-" + sectionEnd.format(clockFormatter);

            long userChange = 0;
            double changePercentage = 0.0;
            if (i > 0) {
                UserStats previousSection = dailyStats.get(i - 1);
                userChange = userCount - previousSection.getUserCount();
                changePercentage = previousSection.getUserCount() == 0 ? (userCount > 0 ? 100.0 : 0.0)
                        : (userChange * 100.0 / previousSection.getUserCount());
            }

            int deltaUp = Double.compare(changePercentage, 0);
            dailyStats.add(new UserStats(time, userCount, userChange, changePercentage, deltaUp));
        }
        return dailyStats;
    }

    private List<UserStats> calculateWeeklyStats(LocalDateTime now) {
        LocalDateTime start = now.minusDays(7).truncatedTo(ChronoUnit.DAYS);
        List<UserStats> weeklyStats = new ArrayList<>();
        String[] azerbaijaniDayNames = {"Bazar", "Bazar ertəsi", "Çərşənbə.a", "Çərşənbə", "Cümə.a", "Cümə", "Şənbə"};

        for (int i = 0; i < 7; i++) {
            LocalDateTime dayStart = start.plusDays(i);
            LocalDateTime dayEnd = dayStart.plusDays(1);
            Date dayStartDate = Date.from(dayStart.atZone(AZERBAIJAN_ZONE).toInstant());
            Date dayEndDate = Date.from(dayEnd.atZone(AZERBAIJAN_ZONE).toInstant());

            List<String> visitorIps = siteVisitRepository.findDistinctVisitorIpByVisitDateTimeBetween(dayStartDate, dayEndDate);
            long userCount = visitorIps.size();

            String time = azerbaijaniDayNames[dayStart.getDayOfWeek().getValue() % 7];

            long userChange = 0;
            double changePercentage = 0.0;
            if (i > 0) {
                UserStats previousDay = weeklyStats.get(i - 1);
                userChange = userCount - previousDay.getUserCount();
                changePercentage = previousDay.getUserCount() == 0 ? (userCount > 0 ? 100.0 : 0.0)
                        : (userChange * 100.0 / previousDay.getUserCount());
            }

            int deltaUp = Double.compare(changePercentage, 0);
            weeklyStats.add(new UserStats(time, userCount, userChange, changePercentage, deltaUp));
        }
        return weeklyStats;
    }

    private List<UserStats> calculateMonthlyStats(LocalDateTime now) {
        LocalDateTime start = now.minusMonths(12).withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
        List<UserStats> monthlyStats = new ArrayList<>();
        String[] azerbaijaniMonthNames = {"Yanvar", "Fevral", "Mart", "Aprel", "May", "İyun", "İyul", "Avqust", "Sentyabr", "Oktyabr", "Noyabr", "Dekabr"};

        for (int i = 0; i < 12; i++) {
            LocalDateTime monthStart = start.plusMonths(i);
            LocalDateTime monthEnd = monthStart.plusMonths(1);
            Date monthStartDate = Date.from(monthStart.atZone(AZERBAIJAN_ZONE).toInstant());
            Date monthEndDate = Date.from(monthEnd.atZone(AZERBAIJAN_ZONE).toInstant());

            List<String> visitorIps = siteVisitRepository.findDistinctVisitorIpByVisitDateTimeBetween(monthStartDate, monthEndDate);
            long userCount = visitorIps.size();

            String time = azerbaijaniMonthNames[monthStart.getMonthValue() - 1];

            long userChange = 0;
            double changePercentage = 0.0;
            if (i > 0) {
                UserStats previousMonth = monthlyStats.get(i - 1);
                userChange = userCount - previousMonth.getUserCount();
                changePercentage = previousMonth.getUserCount() == 0 ? (userCount > 0 ? 100.0 : 0.0)
                        : (userChange * 100.0 / previousMonth.getUserCount());
            }

            int deltaUp = Double.compare(changePercentage, 0);
            monthlyStats.add(new UserStats(time, userCount, userChange, changePercentage, deltaUp));
        }
        return monthlyStats;
    }

    private List<UserStats> calculateYearlyStats() {
        Date earliestVisitDate = siteVisitRepository.findEarliestVisitDateTime();
        if (earliestVisitDate == null) return new ArrayList<>();

        LocalDateTime earliest = LocalDateTime.ofInstant(earliestVisitDate.toInstant(), AZERBAIJAN_ZONE);
        LocalDateTime now = LocalDateTime.now(AZERBAIJAN_ZONE);
        int earliestYear = earliest.getYear();
        int currentYear = now.getYear();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        List<UserStats> yearlyStats = new ArrayList<>();

        for (int year = earliestYear; year <= currentYear; year++) {
            LocalDateTime yearStart = LocalDateTime.of(year, 1, 1, 0, 0);
            LocalDateTime yearEnd = yearStart.plusYears(1);
            Date yearStartDate = Date.from(yearStart.atZone(AZERBAIJAN_ZONE).toInstant());
            Date yearEndDate = Date.from(yearEnd.atZone(AZERBAIJAN_ZONE).toInstant());

            List<String> visitorIps = siteVisitRepository.findDistinctVisitorIpByVisitDateTimeBetween(yearStartDate, yearEndDate);
            long userCount = visitorIps.size();

            String time = yearStart.format(formatter);

            long userChange = 0;
            double changePercentage = 0.0;
            if (!yearlyStats.isEmpty()) {
                UserStats previousYear = yearlyStats.get(yearlyStats.size() - 1);
                userChange = userCount - previousYear.getUserCount();
                changePercentage = previousYear.getUserCount() == 0 ? (userCount > 0 ? 100.0 : 0.0)
                        : (userChange * 100.0 / previousYear.getUserCount());
            }

            int deltaUp = Double.compare(changePercentage, 0);
            yearlyStats.add(new UserStats(time, userCount, userChange, changePercentage, deltaUp));
        }
        return yearlyStats;
    }
}
