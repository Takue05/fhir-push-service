package com.innovatecore.fhirpushservice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/facilities")
@RequiredArgsConstructor
@Tag(name = "Facility Upload", description = "APIs for uploading facilities to FHIR server")
public class FacilityController {

    private final FhirUploadService fhirUploadService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload facilities from CSV",
            description = "Upload a CSV file containing facility information and post to FHIR server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload completed",
                    content = @Content(schema = @Schema(implementation = UploadResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid file format"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UploadResponse> uploadFacilities(
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            UploadResponse errorResponse = new UploadResponse();
            errorResponse.setMessage("Please select a file to upload");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        if (!file.getOriginalFilename().endsWith(".csv")) {
            UploadResponse errorResponse = new UploadResponse();
            errorResponse.setMessage("Only CSV files are allowed");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        UploadResponse response = fhirUploadService.uploadFacilities(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the service is running")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is running");
    }
}