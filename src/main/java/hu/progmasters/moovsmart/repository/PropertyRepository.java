package hu.progmasters.moovsmart.repository;

import hu.progmasters.moovsmart.domain.Property;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {



//    List<Property> findAllByPrice(double price, Pageable pageable);

//    @Query("select u from Post u where u.userName=:userName")
//    Page<Post> findByUser(@Param("userName") String userName, Pageable pageReq);
//
//    default Page<Post> findByUser(User user, Pageable pageReq) {
//        return findByUser(user.getName(), pageReq);
}
