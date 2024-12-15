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
public class DepositRequest {
    @NotNull
    private String userId;

    @NotNull
    @Positive
    private BigDecimal amount;
}
