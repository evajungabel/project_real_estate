package hu.progmasters.moovsmart.domain;

import org.springframework.data.jpa.domain.Specification;

public class AddressSpecifications {

    public static Specification<Address> hasZipcode(Integer zipcode) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("zipcode"), zipcode);
    }

    public static Specification<Address> hasCountry(String country) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("country"), country);
    }

    public static Specification<Address> hasCity(String city) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("country"), city);
    }
}
