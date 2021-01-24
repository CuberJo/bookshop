package com.epam.bookshop.criteria.impl;

import com.epam.bookshop.context.annotation.Naming;
import com.epam.bookshop.domain.impl.Book;
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

    private Boolean admin;

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

    public Boolean isAdmin() {
        return admin;
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

        private Boolean admin;

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

        public Builder admin(Boolean admin) {
            this.admin = admin;
            return this;
        }

        @Override
        public UserCriteria build() {
            UserCriteria criteria = new UserCriteria(this);
            criteria.name = name;
            criteria.login = login;
            criteria.password = password;
            criteria.email = email;
            criteria.admin = admin;

            return criteria;
        }
    }
}
