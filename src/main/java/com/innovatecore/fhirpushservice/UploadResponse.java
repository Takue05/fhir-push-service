package com.innovatecore.fhirpushservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponse {
    private int totalRecords;
    private int successCount;
    private int failureCount;
    private List<String> successfulFacilities = new ArrayList<>();
    private List<ErrorDetail> errors = new ArrayList<>();
    private String message;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorDetail {
        private String facilityName;
        private String error;
    }
}
