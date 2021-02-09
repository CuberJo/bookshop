package com.epam.bookshop.dto.impl;

import com.epam.bookshop.domain.Entity;

import java.util.Objects;

/**
 * Implementation of DTO patters to {@link com.epam.bookshop.domain.impl.User}
 */
public class UserDto extends Entity {

    private String name;
    private String login;
    private String email;

    public UserDto(long entityId, String name, String login, String email) {
        this.entityId = entityId;
        this.name = name;
        this.login = login;
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserDto userDto = (UserDto) o;
        return name.equals(userDto.name) &&
                login.equals(userDto.login) &&
                email.equals(userDto.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, login, email);
    }
}
