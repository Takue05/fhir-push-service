package com.innovatecore.fhirpushservice;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Location;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FhirUploadService {

    private final CsvParserService csvParserService;
    private final FhirMapperService fhirMapperService;
    private final IGenericClient fhirClient;
    private final FhirContext fhirContext;

    public UploadResponse uploadFacilities(MultipartFile file) {
        UploadResponse response = new UploadResponse();

        try {
            // Parse CSV
            List<Facility> facilities = csvParserService.parseCsv(file);
            response.setTotalRecords(facilities.size());

            int successCount = 0;
            int failureCount = 0;

            // Process each facility
            for (Facility facility : facilities) {
                try {
                    // Map to FHIR Location
                    Location location = fhirMapperService.mapToFhirLocation(facility);

                    // WORKAROUND: Serialize and deserialize to ensure clean object
                    IParser parser = fhirContext.newJsonParser();
                    String jsonString = parser.encodeResourceToString(location);

                    // Parse back to Location - this creates a completely fresh object
                    Location cleanLocation = parser.parseResource(Location.class, jsonString);



                    log.debug("Uploading facility: {} with identifier: {}",
                            facility.getFacility(), facility.getFacilityCode());

                    // Post to FHIR server
                    MethodOutcome outcome = fhirClient
                            .create()
                            .resource(cleanLocation)
                            .execute();

                    if (outcome.getCreated() != null && outcome.getCreated()) {
                        successCount++;
                        response.getSuccessfulFacilities().add(facility.getFacility());
                        log.info("Successfully uploaded facility: {} with ID: {}",
                                facility.getFacility(),
                                outcome.getId() != null ? outcome.getId().getValue() : "unknown");
                    } else {
                        failureCount++;
                        response.getErrors().add(new UploadResponse.ErrorDetail(
                                facility.getFacility(),
                                "Failed to create resource - server did not confirm creation"
                        ));
                        log.error("Failed to upload facility: {}", facility.getFacility());
                    }

                } catch (Exception e) {
                    failureCount++;
                    response.getErrors().add(new UploadResponse.ErrorDetail(
                            facility.getFacility(),
                            e.getMessage()
                    ));
                    log.error("Error uploading facility {}: {}", facility.getFacility(), e.getMessage(), e);
                }
            }

            response.setSuccessCount(successCount);
            response.setFailureCount(failureCount);
            response.setMessage(String.format(
                    "Upload completed: %d successful, %d failed out of %d total",
                    successCount, failureCount, facilities.size()
            ));

        } catch (Exception e) {
            response.setMessage("Error processing CSV file: " + e.getMessage());
            log.error("Error processing CSV file", e);
        }

        return response;
    }
}