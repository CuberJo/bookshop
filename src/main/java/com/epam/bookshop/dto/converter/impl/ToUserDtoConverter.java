package com.epam.bookshop.dto.converter.impl;

import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.dto.converter.Converter;
import com.epam.bookshop.dto.impl.UserDto;

/**
 * Converts {@link User} to {@link UserDto}
 */
public class ToUserDtoConverter implements Converter<UserDto, User> {

    @Override
    public UserDto convert(User user) {
        return new UserDto(user.getEntityId(), user.getName(), user.getLogin(), user.getEmail());
    }
}
