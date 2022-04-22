package com.reloadly.account.mapper;

import com.reloadly.model.dto.AccountDto;
import com.reloadly.model.entity.Account;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountDto convertToDto(Account entity) {
        AccountDto dto = new AccountDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
            dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        }
        return dto;
    }
}
