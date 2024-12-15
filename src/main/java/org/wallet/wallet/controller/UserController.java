package org.wallet.wallet.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.wallet.wallet.dto.requestDto.RegistrationRequest;
import org.wallet.wallet.dto.responsesDto.ResponseDto;
import org.wallet.wallet.entity.User;
import org.wallet.wallet.service.UserService;

@RestController
@RequestMapping(
        value = "/api/v1/user",
        produces = {"application/json; charset=utf-8"},
        consumes = {"application/json"}
)
public class UserController {

private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "create user")
    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional(rollbackFor = Exception.class)
    public User addUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
      return  userService.addUser(registrationRequest);
    }
}
