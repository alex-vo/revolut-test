package com.transferapp.dto.mapper;

import com.transferapp.dto.AccountStateDTO;
import com.transferapp.entity.Account;

public class AccountStateDTOMapper {

    public static AccountStateDTO map(Account account) {
        return AccountStateDTO.builder()
                .id(account.getId())
                .amount(account.getBalance())
                .build();
    }

}
