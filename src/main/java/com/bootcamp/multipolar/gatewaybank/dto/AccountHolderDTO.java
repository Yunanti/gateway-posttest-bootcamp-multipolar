package com.bootcamp.multipolar.gatewaybank.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class AccountHolderDTO {
    private String nik;
    private String name;
    private String address;
}
