package org.wallet.wallet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wallet.wallet.dto.requestDto.DepositRequest;
import org.wallet.wallet.dto.requestDto.TransferRequest;
import org.wallet.wallet.dto.requestDto.WithdrawalRequest;
import org.wallet.wallet.entity.Transaction;
import org.wallet.wallet.service.TransactionService;
import org.wallet.wallet.service.WalletService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody DepositRequest request)throws Exception  {
        transactionService.deposit(request);
        return ResponseEntity.ok("Deposit successful");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody WithdrawalRequest request)throws Exception  {
        transactionService.withdraw(request);
        return ResponseEntity.ok("Withdrawal successful");
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request) throws Exception {
        transactionService.transfer(request);
        return ResponseEntity.ok("Transfer successful");
    }

    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable String userId) {
        return ResponseEntity.ok(transactionService.getTransactionHistory(userId));
    }

}
