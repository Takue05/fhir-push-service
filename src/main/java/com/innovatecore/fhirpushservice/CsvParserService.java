package com.innovatecore.fhirpushservice;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvParserService {

    public List<Facility> parseCsv(MultipartFile file) throws Exception {
        List<Facility> facilities = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim())) {

            for (CSVRecord csvRecord : csvParser) {
                Facility facility = new Facility();
                facility.setProvince(csvRecord.get("Province"));
                facility.setDistrict(csvRecord.get("District"));
                facility.setFacility(csvRecord.get("Facility"));
                facility.setLatitude(csvRecord.get("Latitude"));
                facility.setLongitude(csvRecord.get("Longitude"));
                facility.setFacilityCode(csvRecord.get("Facility Code"));
                facility.setType(csvRecord.get("Type"));
                facility.setOwnership(csvRecord.get("Ownership"));
                facility.setLocation(csvRecord.get("Location"));
                facility.setLevel(csvRecord.get("Level"));
                facility.setContact(csvRecord.get("Contact"));
                facility.setStatus(csvRecord.get("Status"));
                facility.setBedCapacity(csvRecord.get("Bed Capacity"));
                facility.setDateOpened(csvRecord.get("Date Opened"));
                facility.setDateClosed(csvRecord.get("Date Closed"));
                facility.setComments(csvRecord.get("Comments"));

                facilities.add(facility);
            }
        }

        return facilities;
    }
}
