package io.mingachevir.mingachevirrealestateserver.model.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SiteVisitAnalyticsResponse {
    // General stats
    private long totalUsers; // Total unique users from the first day to today
    private long oldUsers;   // Users who visited before today
    private long newUsers;   // Users who visited for the first time today

    // Daily stats (last 24 hours, 4-hour sections)
    private List<UserStats> dailyStats;

    // Weekly stats (last 7 days)
    private List<UserStats> weeklyStats;

    // Monthly stats (last 12 months)
    private List<UserStats> monthlyStats;

    // Yearly stats (all years)
    private List<UserStats> yearlyStats;

    // Getters, setters, and constructors
    public SiteVisitAnalyticsResponse() {}

    public SiteVisitAnalyticsResponse(long totalUsers, long oldUsers, long newUsers,
                                      List<UserStats> dailyStats, List<UserStats> weeklyStats,
                                      List<UserStats> monthlyStats, List<UserStats> yearlyStats) {
        this.totalUsers = totalUsers;
        this.oldUsers = oldUsers;
        this.newUsers = newUsers;
        this.dailyStats = dailyStats;
        this.weeklyStats = weeklyStats;
        this.monthlyStats = monthlyStats;
        this.yearlyStats = yearlyStats;
    }

    // Getters and setters omitted for brevity
}
