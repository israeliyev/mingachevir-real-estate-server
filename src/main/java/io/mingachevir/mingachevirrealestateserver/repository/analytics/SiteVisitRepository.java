package io.mingachevir.mingachevirrealestateserver.repository.analytics;

import io.mingachevir.mingachevirrealestateserver.model.entity.analytics.SiteVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SiteVisitRepository extends JpaRepository<SiteVisit, Long> {
    // Find all site visits between two dates
    List<SiteVisit> findByVisitDateTimeBetween(Date start, Date end);

    // Find distinct visitor IPs (unique users) between two dates
    @Query("SELECT DISTINCT sv.visitorIp FROM SiteVisit sv WHERE sv.visitDateTime BETWEEN ?1 AND ?2")
    List<String> findDistinctVisitorIpByVisitDateTimeBetween(Date start, Date end);

    // Find distinct visitor IPs (unique users) up to a certain date
    @Query("SELECT DISTINCT sv.visitorIp FROM SiteVisit sv WHERE sv.visitDateTime <= ?1")
    List<String> findDistinctVisitorIpByVisitDateTimeBefore(Date end);

    // Find the earliest visit date (to determine the first day)
    @Query("SELECT MIN(sv.visitDateTime) FROM SiteVisit sv")
    Date findEarliestVisitDateTime();}
