package com.transferapp.dto.mapper;

import com.transferapp.dto.AccountStateDTO;
import com.transferapp.entity.Account;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AccountStateDTOMapperTest {

    @Test
    public void testMap() {
        Account account = new Account();
        account.setId(140L);
        account.setBalance(BigDecimal.valueOf(23.9));

        AccountStateDTO result = AccountStateDTOMapper.map(account);

        assertThat(result.getId(), is(140L));
        assertThat(result.getBalance(), is(BigDecimal.valueOf(23.9)));
    }

}
