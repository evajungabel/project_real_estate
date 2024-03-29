package com.realestate.dto;

import com.realestate.domain.PropertyPurpose;
import com.realestate.domain.PropertyType;
import com.realestate.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PropertyFilterRequestForm {
    private PropertyType type;
    private PropertyPurpose purpose;
    @PositiveOrZero
    private Double minArea;
    @PositiveOrZero
    private Double maxArea;
    @PositiveOrZero
    private Integer minNumberOfRooms;
    @PositiveOrZero
    private Integer maxNumberOfRooms;
    @PositiveOrZero
    private Double minPrice;
    @PositiveOrZero
    private Double maxPrice;

    private AddressInfoForProperty addressInfoForProperty;

    private PropertyDataForm propertyDataForm;

}
