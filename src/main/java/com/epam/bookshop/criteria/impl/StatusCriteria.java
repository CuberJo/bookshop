package com.epam.bookshop.criteria.impl;

import com.epam.bookshop.context.annotation.Naming;
import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.domain.impl.Status;

public class StatusCriteria extends Criteria<Status> {

    @Naming(status = true)
    private String status;

    public StatusCriteria(Criteria.Builder<? extends Criteria.Builder> builder) {
        super(builder);
    }

    public String getStatus() {
        return status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Criteria.Builder<RoleCriteria.Builder> {

        @Naming(status = true)
        private String status;

        private Builder() {

        }

        public StatusCriteria.Builder role(String role) {
            this.status = status;
            return this;
        }

        @Override
        public StatusCriteria build() {
            StatusCriteria criteria = new StatusCriteria(this);
            criteria.status = status;

            return criteria;
        }
    }
}
