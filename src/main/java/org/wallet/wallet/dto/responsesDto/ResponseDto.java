package org.wallet.wallet.dto.responsesDto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseDto<T> {
    private boolean status;
    private String msg;
}
