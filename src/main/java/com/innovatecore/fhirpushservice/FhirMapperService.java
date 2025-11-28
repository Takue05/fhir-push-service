package com.innovatecore.fhirpushservice;

import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Service
public class FhirMapperService {

    private static final String MCSD_FACILITY_PROFILE = "http://ihe.net/fhir/StructureDefinition/IHE.mCSD.FacilityLocation";

    public Location mapToFhirLocation(Facility facility) {
        Location location = new Location();

        // Meta
        Meta meta = new Meta();
        meta.addProfile(MCSD_FACILITY_PROFILE);
        meta.setLastUpdated(new Date());
        meta.setVersionId("1");
        location.setMeta(meta);

        // Identifier
        Identifier identifier = new Identifier();
        identifier.setSystem("Impilo");
        identifier.setValue(facility.getFacilityCode());

        Coding idCoding = new Coding();
        idCoding.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203");
        idCoding.setCode("ACSN");
        idCoding.setDisplay("Accession ID");

        CodeableConcept idType = new CodeableConcept();
        idType.addCoding(idCoding);
        idType.setText("Accession ID");
        identifier.setType(idType);

        location.addIdentifier(identifier);

        // Status
        if ("Open".equalsIgnoreCase(facility.getStatus())) {
            location.setStatus(Location.LocationStatus.ACTIVE);
        } else if ("Closed".equalsIgnoreCase(facility.getStatus())) {
            location.setStatus(Location.LocationStatus.INACTIVE);
        } else {
            location.setStatus(Location.LocationStatus.ACTIVE);
        }

        // Name
        location.setName(facility.getFacility());

        // Type
        if (facility.getType() != null) {
            CodeableConcept type = new CodeableConcept();
            Coding typeCoding = new Coding();
            typeCoding.setSystem("urn:ietf:rfc:398");
            typeCoding.setCode("urn:ihe:iti:mcsd:2019:facility");
            typeCoding.setDisplay(facility.getType());
            type.addCoding(typeCoding);
            type.setText(facility.getType());
            location.setType(Collections.singletonList(type));
        }

        // Telecom
        if (facility.getContact() != null) {
            ContactPoint contactPoint = new ContactPoint();
            contactPoint.setSystem(ContactPoint.ContactPointSystem.PHONE);
            contactPoint.setValue(facility.getContact());
            contactPoint.setUse(ContactPoint.ContactPointUse.WORK);
            location.addTelecom(contactPoint);
        }

        // Address
        Address address = new Address();
        address.setUse(Address.AddressUse.WORK);
        address.setType(Address.AddressType.PHYSICAL);
//        if (facility.getAddressLine() != null) {
//            address.addLine(facility.getAddressLine());
//        }
////        if (facility.getCity() != null) {
////            address.setCity(facility.getCity());
//        }
        if (facility.getDistrict() != null) {
            address.setDistrict(facility.getDistrict());
        }
        if (facility.getProvince() != null) {
            address.setState(facility.getProvince());
        }
        address.setCountry("Zimbabwe");
        location.setAddress(address);

        // Physical Type
//        if (facility.getPhysicalType() != null) {
//            CodeableConcept physicalType = new CodeableConcept();
//            Coding physicalCoding = new Coding();
//            physicalCoding.setSystem("http://terminology.hl7.org/CodeSystem/location-physical-type");
//            physicalCoding.setCode(facility.getPhysicalType());
//            physicalCoding.setDisplay(facility.getPhysicalType());
//            physicalType.addCoding(physicalCoding);
//            physicalType.setText(facility.getPhysicalType());
//            location.setPhysicalType(physicalType);
//        }

        // Position
        if (facility.getLongitude() != null && facility.getLatitude() != null) {
            try {
                Location.LocationPositionComponent position = new Location.LocationPositionComponent();
                position.setLongitude(Double.parseDouble(facility.getLongitude()));
                position.setLatitude(Double.parseDouble(facility.getLatitude()));
                location.setPosition(position);
            } catch (NumberFormatException ignored) {}
        }

        // Jurisdiction Extension (Province + District)
        Extension jurisdictionExtension = new Extension();
        jurisdictionExtension.setUrl("http://gofr.org/fhir/StructureDefinition/gofr-jurisdiction");

        if (facility.getDistrict() != null) {
            Extension district = new Extension();
            district.setUrl("district");
            district.setValue(new StringType(facility.getDistrict()));
            jurisdictionExtension.addExtension(district);
        }

        if (facility.getProvince() != null) {
            Extension province = new Extension();
            province.setUrl("province");
            province.setValue(new StringType(facility.getProvince()));
            jurisdictionExtension.addExtension(province);
        }

        location.addExtension(jurisdictionExtension);


        // Managing Organization
        location.setManagingOrganization(new Reference("Organization/mock-org-2"));


        location.setMode(Location.LocationMode.INSTANCE);


        location.setDescription(facility.getLocation());

        return location;
    }
}
