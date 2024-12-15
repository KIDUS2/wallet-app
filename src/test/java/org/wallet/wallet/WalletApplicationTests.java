package org.wallet.wallet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.wallet.wallet.dto.requestDto.DepositRequest;
import org.wallet.wallet.dto.requestDto.LoginDTO;
import org.wallet.wallet.dto.requestDto.TransferRequest;
import org.wallet.wallet.dto.requestDto.WithdrawalRequest;
import org.wallet.wallet.dto.responsesDto.LoginResposeDto;
import org.wallet.wallet.entity.Transaction;
import org.wallet.wallet.entity.User;
import org.wallet.wallet.entity.Wallet;
import org.wallet.wallet.repository.TransactionRepository;
import org.wallet.wallet.repository.UserRepository;
import org.wallet.wallet.repository.WalletRepository;
import org.wallet.wallet.service.AuthenticationService;
import org.wallet.wallet.service.TransactionService;
import org.wallet.wallet.service.UserService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.wallet.wallet.enumuration.Role.ADMIN;

@SpringBootTest
class WalletApplicationTests {

    static String user_id;
    static String sender_account;
    static String recipient_account;
    static String transaction_id;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private  BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private WalletRepository walletRepository;

    @Test
    void contextLoads() {
    }

    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveUserTest() {

        User user = new User();
        user.setUsername("kifiya");
        user.setEmail("kifiya@gmail.com");
        user.setCreatedAt(Instant.now());
        user.setAge(21);
        user.setPhoneNumber("0933225544");
        user.setPassword(passwordEncoder.encode("kifiya@123"));
        user.setFullName("kifiya");
        user.setRole(ADMIN);
        userRepository.save(user);

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        walletRepository.save(wallet);
        user_id = user.getId();
        Assertions.assertThat(user_id).isNotNull();
    }

    @Test
    @Order(2)
    public void loginUserTest() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setPrincipal("kifiya2@gmail.com");
        loginDTO.setPassword("kifiya@123");
        LoginResposeDto response = authenticationService.authenticate(loginDTO);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo("successfully login");
        Assertions.assertThat(response.getCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getAccessToken()).isNotNull();
        Assertions.assertThat(response.getRefreshToken()).isNotNull();
    }
    @Test
    @Order(3)
    @Rollback(value = false)
    public void depositTest() throws Exception {
        User user = userRepository.findByEmailOrPhoneNumber("kifiya2@example.com")
                .orElseThrow(() -> new Exception("User not found"));

        Assertions.assertThat(user).isNotNull();
        user_id = user.getId();
        Assertions.assertThat(user_id).isNotNull();
        DepositRequest depositRequest = new DepositRequest(user_id, BigDecimal.valueOf(1000));
        System.out.println(depositRequest);
        transactionService.deposit(depositRequest);
        List<Transaction> transactions = transactionRepository.findByUserId(user_id);
        Assertions.assertThat(transactions.size()).isGreaterThan(0);
        transaction_id = transactions.get(0).getId();
        Assertions.assertThat(transaction_id).isNotNull();
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void withdrawTest() throws Exception {
        User user = userRepository.findByEmailOrPhoneNumber("kifiya2@gmail.com")
                .orElseThrow(() -> new Exception("User not found"));
        Assertions.assertThat(user).isNotNull();
        user_id = user.getId();
        WithdrawalRequest withdrawalRequest = new WithdrawalRequest(user_id, BigDecimal.valueOf(50));
        transactionService.withdraw(withdrawalRequest);
        List<Transaction> transactions = transactionRepository.findByUserId(user_id);
        Assertions.assertThat(transactions.size()).isGreaterThan(1);
        transaction_id = transactions.get(transactions.size() - 1).getId();
        Assertions.assertThat(transaction_id).isNotNull();
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    public void transferTest() throws Exception {
        User sender = userRepository.findByEmailOrPhoneNumber("kifiya2@gmail.com")
                .orElseThrow(() -> new Exception("Sender not found"));
        User recipient = userRepository.findByEmailOrPhoneNumber("kifiya@gmail.com")
                .orElseThrow(() -> new Exception("Recipient not found"));
        Assertions.assertThat(sender).isNotNull();
        Assertions.assertThat(recipient).isNotNull();
        TransferRequest transferRequest = new TransferRequest(
                sender.getAccountNumber(),
                recipient.getAccountNumber(),
                BigDecimal.valueOf(200)
        );
        transactionService.transfer(transferRequest);
        List<Transaction> senderTransactions = transactionRepository.findByUserId(sender.getId());
        Assertions.assertThat(senderTransactions.size()).isGreaterThan(0);
        List<Transaction> recipientTransactions = transactionRepository.findByUserId(recipient.getId());
        Assertions.assertThat(recipientTransactions.size()).isGreaterThan(0);
        transaction_id = senderTransactions.get(senderTransactions.size() - 1).getId();
        Assertions.assertThat(transaction_id).isNotNull();
    }

    @Test
    @Order(6)
    public void getListOfTransactionsTest() throws Exception {
        User user = userRepository.findByEmailOrPhoneNumber("kifiya2@gmail.com")
                .orElseThrow(() -> new Exception("User not found"));
        Assertions.assertThat(user).isNotNull();
        user_id = user.getId();
        List<Transaction> transactions = transactionRepository.findByUserId(user_id);
        Assertions.assertThat(transactions).isNotEmpty();
    }

    @Test
    @Order(7)
    @Rollback(value = false)
    public void getTransactionByWeekTest() throws Exception {
        Instant oneWeekAgo = Instant.now().minusSeconds(604800);
        User user = userRepository.findByEmailOrPhoneNumber("kifiya2@gmail.com")
                .orElseThrow(() -> new Exception("User not found"));
        Assertions.assertThat(user).isNotNull();
        user_id = user.getId();
        List<Transaction> transactions = transactionRepository.findByUserIdAndCreatedAtBetween(
                user_id, oneWeekAgo, Instant.now()
        );
        Assertions.assertThat(transactions.size()).isGreaterThan(0);
    }

    @Test
    @Order(7)
    public void getUserByIdTest() throws Exception {
        User user = userRepository.findByEmailOrPhoneNumber("kifiya2@gmail.com")
                .orElseThrow(() -> new Exception("User not found"));
        Assertions.assertThat(user).isNotNull();
        user_id = user.getId();
        User user1 = userRepository.findById(user_id).orElseThrow();
        Assertions.assertThat(user1.getId()).isEqualTo(user_id);
    }
}
