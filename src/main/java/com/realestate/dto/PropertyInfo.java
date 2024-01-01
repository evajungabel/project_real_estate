package com.realestate.dto;

import com.realestate.domain.PropertyPurpose;
import com.realestate.domain.PropertyType;
import com.realestate.domain.PropertyImageURL;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PropertyInfo {
    private Long id;
    private String name;
    private PropertyType type;
    private PropertyPurpose purpose;
    private Double area;
    private Integer numberOfRooms;
    private Double price;
    private AddressInfoForProperty addressInfoForProperty;
    private List<PropertyImageURL> propertyImageURLS;
}
