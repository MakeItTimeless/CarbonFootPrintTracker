package com.internship.tool.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KpiDTO {
    private long totalRecords;
    private BigDecimal totalCarbonKg;
    private BigDecimal avgCarbonPerRecord;
    private String topCategory;
}