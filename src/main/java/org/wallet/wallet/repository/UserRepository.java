package org.wallet.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wallet.wallet.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    User findByPhoneNumber(String phoneNo);
    @Query("SELECT u FROM User u WHERE u.email = :identifier OR u.phoneNumber = :identifier")
    Optional<User> findByEmailOrPhoneNumber(@Param("identifier") String identifier);

    Optional<User> findByAccountNumber(String accountNumber);
}
