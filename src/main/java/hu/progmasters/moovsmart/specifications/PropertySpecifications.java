package hu.progmasters.moovsmart.specifications;

import hu.progmasters.moovsmart.domain.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.time.Year;
import java.util.Date;

public final class PropertySpecifications {

    public static Specification<Property> hasType(PropertyType type) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("type"), type);
    }

    public static Specification<Property> hasPurpose(PropertyPurpose purpose) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("purpose"), purpose);
    }

    public static Specification<Property> hasAreaGreaterThanOrEqualTo(Double minArea) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("area"), minArea);
    }

    public static Specification<Property> hasAreaLessThanOrEqualTo(Double maxArea) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("area"), maxArea);
    }

    public static Specification<Property> hasNumberOfRoomsGreaterThanOrEqualTo(Integer minNumberOfRooms) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("numberOfRooms"), minNumberOfRooms);
    }

    public static Specification<Property> hasNumberOfRoomsLessThanOrEqualTo(Integer maxNumberOfRooms) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("numberOfRooms"), maxNumberOfRooms);
    }

    public static Specification<Property> hasGreaterThanOrEqualTo(Double minPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Property> hasLessThanOrEqualTo(Double maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Property> hasPropertyCountry(String country) {
        return (root, query, criteriaBuilder) -> {
            Join<Property, Address> addressJoin = root.join("address", JoinType.INNER);
            return criteriaBuilder.equal(addressJoin.get("country"), country);
        };
    }

    public static Specification<Property> hasPropertyCity(String city) {
        return (root, query, criteriaBuilder) -> {
            Join<Property, Address> addressJoin = root.join("address", JoinType.INNER);
            return criteriaBuilder.equal(addressJoin.get("city"), city);
        };
    }

    public static Specification<Property> hasPropertyZipcode(Integer zipcode) {
        return (root, query, criteriaBuilder) -> {
            Join<Property, Address> addressJoin = root.join("address", JoinType.INNER);
            return criteriaBuilder.equal(addressJoin.get("zipcode"), zipcode);
        };
    }

    public static Specification<Property> hasPropertyCondition(PropertyCondition propertyCondition) {
        return (root, query, criteriaBuilder) -> {
            Join<Property, PropertyData> propertyDataJoin = root.join("propertyData", JoinType.INNER);
            return criteriaBuilder.equal(propertyDataJoin.get("propertyCondition"), propertyCondition);
        };
    }

    public static Specification<Property> hasPropertyYearBuilt(Integer yearBuilt) {
        return (root, query, criteriaBuilder) -> {
            Join<Property, PropertyData> propertyDataJoin = root.join("propertyData", JoinType.INNER);
            return criteriaBuilder.equal(propertyDataJoin.get("yearBuilt"), yearBuilt);
        };
    }

    public static Specification<Property> hasPropertyPropertyParking(PropertyParking propertyParking) {
        return (root, query, criteriaBuilder) -> {
            Join<Property, PropertyData> propertyDataJoin = root.join("propertyData", JoinType.INNER);
            return criteriaBuilder.equal(propertyDataJoin.get("propertyParking"), propertyParking);
        };
    }

    public static Specification<Property> hasPropertyPropertyOrientation(PropertyOrientation propertyOrientation) {
        return (root, query, criteriaBuilder) -> {
            Join<Property, PropertyData> propertyDataJoin = root.join("propertyData", JoinType.INNER);
            return criteriaBuilder.equal(propertyDataJoin.get("propertyOrientation"), propertyOrientation);
        };
    }

    public static Specification<Property> hasPropertyPropertyHeatingType(PropertyHeatingType propertyHeatingType) {
        return (root, query, criteriaBuilder) -> {
            Join<Property, PropertyData> propertyDataJoin = root.join("propertyData", JoinType.INNER);
            return criteriaBuilder.equal(propertyDataJoin.get("propertyHeatingType"), propertyHeatingType);
        };
    }

    public static Specification<Property> hasPropertyEnergyCertificate(PropertyEnergyPerformanceCertificate energyCertificate) {
        return (root, query, criteriaBuilder) -> {
            Join<Property, PropertyData> propertyDataJoin = root.join("propertyData", JoinType.INNER);
            return criteriaBuilder.equal(propertyDataJoin.get("energyCertificate"), energyCertificate);
        };
    }

    public static Specification<Property> hasPropertyHasBalcony(Boolean hasBalcony) {
        return (root, query, criteriaBuilder) -> {
            Join<Property, PropertyData> propertyDataJoin = root.join("propertyData", JoinType.INNER);
            return criteriaBuilder.equal(propertyDataJoin.get("hasBalcony"), hasBalcony);
        };
    }

    public static Specification<Property> hasPropertyHasLift(Boolean hasLift) {
        return (root, query, criteriaBuilder) -> {
            Join<Property, PropertyData> propertyDataJoin = root.join("propertyData", JoinType.INNER);
            return criteriaBuilder.equal(propertyDataJoin.get("hasLift"), hasLift);
        };
    }

    public static Specification<Property> hasPropertyIsAccessible(Boolean isAccessible) {
        return (root, query, criteriaBuilder) -> {
            Join<Property, PropertyData> propertyDataJoin = root.join("propertyData", JoinType.INNER);
            return criteriaBuilder.equal(propertyDataJoin.get("isAccessible"), isAccessible);
        };
    }


    public static Specification<Property> hasPropertyHasAirCondition(Boolean hasAirCondition) {
        return (root, query, criteriaBuilder) -> {
            Join<Property, PropertyData> propertyDataJoin = root.join("propertyData", JoinType.INNER);
            return criteriaBuilder.equal(propertyDataJoin.get("hasAirCondition"), hasAirCondition);
        };
    }

    public static Specification<Property> hasPropertyHasGarden(Boolean hasGarden) {
        return (root, query, criteriaBuilder) -> {
            Join<Property, PropertyData> propertyDataJoin = root.join("propertyData", JoinType.INNER);
            return criteriaBuilder.equal(propertyDataJoin.get("hasGarden"), hasGarden);
        };
    }
}

