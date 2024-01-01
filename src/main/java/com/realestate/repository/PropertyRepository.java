package com.realestate.repository;

import com.realestate.domain.Property;
import com.realestate.domain.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface PropertyRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {


    @Query("SELECT p FROM Property p WHERE p.dateOfCreation > :thresholdDate")
    List<Property> findPropertiesCreatedAfterThresholdDate(@Param("thresholdDate") Date thresholdDate);
}
