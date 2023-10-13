package com.bootcamp.multipolar.gatewaybank.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class AccountDTO {
    private String id;
    private AccountTypeDTO accountTypeDTO;
    private AccountStatusDTO accountStatusDTO;
    private AccountHolderDTO accountHolderDTO;
    private double balance;
    private LocalDateTime openingDate = LocalDateTime.now();
    private LocalDate closingDate;
}
