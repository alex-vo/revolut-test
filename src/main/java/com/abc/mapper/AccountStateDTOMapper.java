package com.abc.mapper;

import com.abc.dto.AccountStateDTO;
import com.abc.entity.Account;

public class AccountStateDTOMapper {

    public static AccountStateDTO map(Account account) {
        return AccountStateDTO.builder()
                .id(account.getId())
                .amount(account.getBalance())
                .build();
    }

}
