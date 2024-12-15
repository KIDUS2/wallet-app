package org.wallet.wallet.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.wallet.wallet.dto.requestDto.RegistrationRequest;
import org.wallet.wallet.entity.User;
import org.wallet.wallet.entity.Wallet;
import org.wallet.wallet.exception.AlreadyExistingException;
import org.wallet.wallet.repository.UserRepository;
import org.wallet.wallet.repository.WalletRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    public User addUser(RegistrationRequest registrationRequest) {
        User existingUser = userRepository.findByPhoneNumber(registrationRequest.getPhone());
        if (existingUser != null) {
            throw new AlreadyExistingException("Phone Number already exist");
        } else {
            User user = new User();
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setPhoneNumber(registrationRequest.getPhone());
            user.setAge(registrationRequest.getAge());
            user.setUsername(registrationRequest.getUserName());
            user.setFullName(registrationRequest.getFullName());
            user.setDateOfBirth(registrationRequest.getBirthDate());
            user.setEmail(registrationRequest.getEmail());
            user = userRepository.save(user);
            Wallet wallet = new Wallet();
            wallet.setUser(user);
            walletRepository.save(wallet);
            return user;
        }
    }
}
