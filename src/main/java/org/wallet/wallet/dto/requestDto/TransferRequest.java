package org.wallet.wallet.dto.requestDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {
    @NotNull
    private String senderId;

    @NotNull
    private String recipientId;

    @NotNull
    @Positive
    private BigDecimal amount;
}