package com.innovatecore.fhirpushservice;


import lombok.Data;

@Data
public class Facility {
    private String province;
    private String  district;
    private String facility;
    private String latitude;
    private String longitude;
    private String facilityCode;
    private String type;
    private String ownership;
    private String location;
    private String level;
    private String contact;
    private String status;
    private String bedCapacity;
    private String dateOpened;
    private String  dateClosed;
    private String comments;
}
