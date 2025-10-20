package com.prsanmartin.appmartin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntelligenceResultsDTO {
    
    private Integer totalTests;
    private Integer totalStudents;
    private LocalDateTime lastUpdate;
    private Map<String, Long> intelligenceDistribution;
    private Map<String, Double> averageScores;
    private List<TestResultDTO> recentResults;
    private Map<String, Object> statistics;
    
    // Additional fields for comprehensive results
    private Map<String, Integer> predominantIntelligenceCount;
    private List<String> topIntelligences;
    private Map<String, Object> trends;
    private String systemStatus;
}
