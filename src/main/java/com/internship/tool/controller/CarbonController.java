package com.internship.tool.controller;

import com.internship.tool.dto.CarbonRecordDTO;
import com.internship.tool.dto.KpiDTO;
import com.internship.tool.entity.CarbonRecord;
import com.internship.tool.service.CarbonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342", allowedHeaders = "*")
public class CarbonController {
    private final CarbonService carbonService;

    private String getLoggedInUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping
    public ResponseEntity<List<CarbonRecord>> getAllRecords() {
        return ResponseEntity.ok(carbonService.getAllRecords(getLoggedInUsername()));
    }

    @PostMapping
    public ResponseEntity<CarbonRecord> createRecord(@RequestBody CarbonRecordDTO dto) {
        return ResponseEntity.ok(carbonService.createRecord(dto, getLoggedInUsername()));
    }

    @GetMapping("/kpi")
    public ResponseEntity<KpiDTO> getKpi() {
        return ResponseEntity.ok(carbonService.getKpiSummary(getLoggedInUsername()));
    }
    @GetMapping("/peer-status")
    public ResponseEntity<java.util.Map<String, Object>> getPeerStatus() {
        String username = getLoggedInUsername();

        // Call service layer to get the stats map
        java.util.Map<String, Object> statusMap = carbonService.getPeerComparison(username);
        return ResponseEntity.ok(statusMap);
    }
}