package org.wallet.wallet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wallet.wallet.dto.requestDto.LoginDTO;
import org.wallet.wallet.dto.responsesDto.LoginResposeDto;
import org.wallet.wallet.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<LoginResposeDto> authenticate(@RequestBody LoginDTO request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
