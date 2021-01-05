package com.epam.bookshop.criteria.impl;

import com.epam.bookshop.context.annotation.Naming;
import com.epam.bookshop.domain.impl.Role;
import com.epam.bookshop.criteria.Criteria;

public class RoleCriteria extends Criteria<Role> {

    @Naming(role = true)
    private String role;

    public RoleCriteria(Criteria.Builder<? extends Criteria.Builder> builder) {
        super(builder);
    }

    public String getRole() {
        return role;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Criteria.Builder<RoleCriteria.Builder> {

        @Naming(role = true)
        private String role;

        private Builder() {

        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        @Override
        public RoleCriteria build() {
            RoleCriteria criteria = new RoleCriteria(this);
            criteria.role = role;

            return criteria;
        }
    }
}
