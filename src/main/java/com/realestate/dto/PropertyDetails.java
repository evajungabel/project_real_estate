package com.realestate.dto;

import com.realestate.domain.PropertyPurpose;
import com.realestate.domain.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PropertyDetails {
    private String name;
    private Integer numberOfRooms;
    private Double price;
    private PropertyType type;
    private PropertyPurpose purpose;
    private Double area;
    private String description;
    private AddressInfoForProperty addressInfoForProperty;
    private List<PropertyImageURLInfo> propertyImageURLInfos;
    private PropertyDataInfo propertyDataInfo;
}
