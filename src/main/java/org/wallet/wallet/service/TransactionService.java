package org.wallet.wallet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallet.wallet.dto.requestDto.DepositRequest;
import org.wallet.wallet.dto.requestDto.TransferRequest;
import org.wallet.wallet.dto.requestDto.WithdrawalRequest;
import org.wallet.wallet.entity.Transaction;
import org.wallet.wallet.entity.User;
import org.wallet.wallet.entity.Wallet;
import org.wallet.wallet.enumuration.TransactionType;
import org.wallet.wallet.repository.TransactionRepository;
import org.wallet.wallet.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final UserRepository userRepository;

    public void createTransaction(String userId, TransactionType type, BigDecimal amount) throws Exception {
        Transaction transaction = new Transaction();
        transaction.setUser(walletService.getWalletByUserId(userId).getUser());
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setStatus("SUCCESS");
        transactionRepository.save(transaction);
    }

    public void deposit(DepositRequest request) throws Exception {
        walletService.updateWalletBalance(request.getUserId(), request.getAmount(), true);
        createTransaction(request.getUserId(), TransactionType.DEPOSIT, request.getAmount());
    }

    public void withdraw(WithdrawalRequest request) throws Exception {
        Wallet wallet = walletService.getWalletByUserId(request.getUserId());

        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new Exception("Insufficient balance for withdrawal");
        }

        walletService.updateWalletBalance(request.getUserId(), request.getAmount(), false);
        createTransaction(request.getUserId(), TransactionType.WITHDRAWAL, request.getAmount());
    }
    @Transactional
    public void transfer(TransferRequest request) throws Exception {
        Optional<User> senderOptional = userRepository.findByAccountNumber(request.getSenderId());
        System.out.println(senderOptional);
        if (!senderOptional.isPresent()) {
            throw new Exception("Sender account does not exist");
        }
        User sender = senderOptional.get();
        Optional<User> recipientOptional = userRepository.findByAccountNumber(request.getRecipientId());
        if (!recipientOptional.isPresent()) {
            throw new Exception("Recipient account does not exist");
        }
        User recipient = recipientOptional.get();

        if (!walletService.hasSufficientBalance(sender.getId(), request.getAmount())) {
            throw new Exception("Insufficient balance for transfer");
        }
        System.out.println("rrrrrrrrrrrrr"+recipient.getId());
        System.out.println("ssssssssss"+sender.getId());
        walletService.updateWalletBalance(sender.getId(), request.getAmount(), false);
        walletService.updateWalletBalance(recipient.getId(), request.getAmount(), true);

        createTransaction(sender.getId(), TransactionType.TRANSFER, request.getAmount());
        createTransaction(recipient.getId(), TransactionType.RECEIVED, request.getAmount());
    }





    public List<Transaction> getTransactionHistory(String userId) {
        return transactionRepository.findByUserId(userId);
    }
}
