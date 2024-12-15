package org.wallet.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wallet.wallet.entity.Wallet;
@Repository

public interface WalletRepository extends JpaRepository<Wallet, String> {
    @Query("SELECT w FROM Wallet w WHERE w.user.id = :userId")
    Wallet findByUserId(@Param("userId") String userId);
}
