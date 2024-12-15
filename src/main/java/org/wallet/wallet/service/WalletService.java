package org.wallet.wallet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallet.wallet.entity.Wallet;
import org.wallet.wallet.repository.WalletRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletService {
    private final WalletRepository walletRepository;

    public Wallet getWalletByUserId(String userId) throws Exception {
        Wallet wallet = walletRepository.findByUserId(userId);
        System.out.println(userId);
        System.out.println(wallet);
        if (wallet == null) {
            throw new Exception("Wallet not found");
        }
        return walletRepository.findByUserId(userId);
    }

    public void updateWalletBalance(String userId, BigDecimal amount, boolean isCredit) throws Exception {
        Wallet wallet = getWalletByUserId(userId);
        BigDecimal newBalance = isCredit ? wallet.getBalance().add(amount) : wallet.getBalance().subtract(amount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("Insufficient balance");
        }

        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
    }

    public boolean hasSufficientBalance(String userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId);
        if (wallet == null) {
            return false;
        }


        return wallet.getBalance().compareTo(amount) >= 0;
    }

}
