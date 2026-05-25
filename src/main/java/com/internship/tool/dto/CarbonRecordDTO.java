package com.internship.tool.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarbonRecordDTO {
    private Long id;
    private String activityName;
    private String category;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal emissionFactor;
    private BigDecimal carbonKg;
    private LocalDate activityDate;
    private String status;
    private String aiDescription;
}