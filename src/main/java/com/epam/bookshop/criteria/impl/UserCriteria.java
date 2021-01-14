package com.epam.bookshop.criteria.impl;

import com.epam.bookshop.context.annotation.Naming;
import com.epam.bookshop.domain.impl.User;
import com.epam.bookshop.criteria.Criteria;
import org.mindrot.jbcrypt.BCrypt;

public class UserCriteria extends Criteria<User> {

    @Naming(name = true)
    private String name;

    @Naming(login = true)
    private java.lang.String login;

    private java.lang.String password;

    @Naming(email = true)
    private String email;

    private Long roleId;

    public UserCriteria(Criteria.Builder<? extends Criteria.Builder> builder) {
        super(builder);
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Long getRoleId() {
        return roleId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Criteria.Builder<UserCriteria.Builder> {

        @Naming(name = true)
        private String name;

        @Naming(login = true)
        private java.lang.String login;

        private java.lang.String password;

        @Naming(email = true)
        private String email;

        private Long roleId;

        private Builder() {

        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder password(String password) {
            this.password =  BCrypt.hashpw(password, BCrypt.gensalt());
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder roleId(Long roleId) {
            this.roleId = roleId;
            return this;
        }

        @Override
        public UserCriteria build() {
            UserCriteria criteria = new UserCriteria(this);
            criteria.name = name;
            criteria.login = login;
            criteria.password = password;
            criteria.email = email;
            criteria.roleId = roleId;

            return criteria;
        }
    }
}
