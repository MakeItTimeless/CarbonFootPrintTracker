package com.internship.tool.service;

import com.internship.tool.dto.CarbonRecordDTO;
import com.internship.tool.dto.KpiDTO;
import com.internship.tool.entity.CarbonRecord;
import com.internship.tool.entity.User;
import com.internship.tool.repository.CarbonRecordRepository;
import com.internship.tool.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarbonService {

    private final CarbonRecordRepository repository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.url}")
    private String groqApiUrl;

    public List<CarbonRecord> getAllRecords(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return repository.findByUser(user);
    }

    public CarbonRecord createRecord(CarbonRecordDTO dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String aiDescription = callGroqApi(dto.getCategory(),
                dto.getQuantity().toString(), dto.getUnit());

        // Fix: If user didn't provide a value, assign an intelligent estimate based on quantity
        BigDecimal finalCarbonKg = dto.getCarbonKg();
        if (finalCarbonKg == null) {
            BigDecimal factor = dto.getEmissionFactor() != null ? dto.getEmissionFactor() : BigDecimal.valueOf(0.25);
            finalCarbonKg = dto.getQuantity().multiply(factor);
        }

        CarbonRecord record = CarbonRecord.builder()
                .activityName(dto.getActivityName())
                .category(dto.getCategory())
                .quantity(dto.getQuantity())
                .unit(dto.getUnit())
                .emissionFactor(dto.getEmissionFactor() != null ? dto.getEmissionFactor() : BigDecimal.valueOf(0.25))
                .carbonKg(finalCarbonKg)
                .activityDate(dto.getActivityDate())
                .status(dto.getStatus() != null ? dto.getStatus() : "ACTIVE")
                .aiDescription(aiDescription)
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return repository.save(record);
    }

    public KpiDTO getKpiSummary(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CarbonRecord> records = repository.findByUser(user);

        if (records.isEmpty()) {
            return KpiDTO.builder()
                    .totalRecords(0)
                    .totalCarbonKg(BigDecimal.ZERO)
                    .avgCarbonPerRecord(BigDecimal.ZERO)
                    .topCategory("N/A")
                    .build();
        }

        long totalRecords = records.size();

        BigDecimal totalCarbonKg = records.stream()
                .map(r -> r.getCarbonKg() != null ? r.getCarbonKg() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal avgCarbon = totalCarbonKg.divide(
                BigDecimal.valueOf(totalRecords), 2,
                java.math.RoundingMode.HALF_UP);

        String topCategory = records.stream()
                .collect(Collectors.groupingBy(
                        CarbonRecord::getCategory, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        return KpiDTO.builder()
                .totalRecords(totalRecords)
                .totalCarbonKg(totalCarbonKg)
                .avgCarbonPerRecord(avgCarbon)
                .topCategory(topCategory)
                .build();
    }

    private String callGroqApi(String category, String quantity, String unit) {
        try {
            String prompt = "Analyze this carbon footprint log: Category: " + category +
                    ", Value: " + quantity + " " + unit +
                    ". Give a 2-sentence breakdown and one quick tip.";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(groqApiKey);

            Map<String, Object> requestBody = Map.of(
                    "model", "llama-3.3-70b-versatile",
                    "messages", List.of(Map.of("role", "user", "content", prompt)),
                    "max_tokens", 200
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    groqApiUrl, request, Map.class);

            Map<String, Object> body = response.getBody();
            List<Map<String, Object>> choices =
                    (List<Map<String, Object>>) body.get("choices");
            Map<String, Object> message =
                    (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");

        } catch (Exception e) {
            log.error("Groq API call failed: {}", e.getMessage());
            return "AI feedback temporarily unavailable. Consider reducing consumption in this category.";
        }
    }
    public java.util.Map<String, Object> getPeerComparison(String username) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();

        // 1. Fetch the user entity exactly like your other working methods
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Query using the correct repository field 'repository.findByUser(user)'
        List<CarbonRecord> records = repository.findByUser(user);

        // 3. Calculate total carbon using clean primitive double values
        double userTotal = records.stream()
                .filter(r -> r.getCarbonKg() != null)
                .mapToDouble(r -> r.getCarbonKg().doubleValue())
                .sum();

        // 4. Baseline system target baseline for tracking
        double systemAvg = 120.0;
        double variancePercentage = 0.0;
        String statusTier = "Eco-Observer";
        String tierColor = "text-slate-600";

        if (systemAvg > 0) {
            if (userTotal < systemAvg) {
                variancePercentage = ((systemAvg - userTotal) / systemAvg) * 100;
                statusTier = "Eco-Champion";
                tierColor = "text-emerald-600";
            } else {
                variancePercentage = ((userTotal - systemAvg) / systemAvg) * 100;
                statusTier = "High Consumer";
                tierColor = "text-rose-600";
            }
        }

        response.put("userTotal", userTotal);
        response.put("systemAvg", systemAvg);
        response.put("variance", Math.round(variancePercentage));
        response.put("isLower", userTotal <= systemAvg);
        response.put("tier", statusTier);
        response.put("tierColor", tierColor);

        return response;
    }
}