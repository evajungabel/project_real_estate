package com.realestate.repository;

import com.realestate.domain.PropertyImageURL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyImageURLRepository extends JpaRepository<PropertyImageURL, Long> {
}
