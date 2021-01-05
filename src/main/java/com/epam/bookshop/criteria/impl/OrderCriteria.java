package com.epam.bookshop.criteria.impl;

import com.epam.bookshop.criteria.Criteria;
import com.epam.bookshop.domain.impl.Order;

import java.time.LocalDateTime;

public class OrderCriteria extends Criteria<Order> {
    private Long libraryUserId;
    private LocalDateTime orderTime;
    private Long statusId;

    public OrderCriteria(Criteria.Builder<? extends Criteria.Builder> builder) {
        super(builder);
    }

    public Long getLibraryUserId() {
        return libraryUserId;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public Long getStatusId() {
        return statusId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Criteria.Builder<OrderCriteria.Builder> {
        private Long libraryUserId;
        private LocalDateTime orderTime;
        private Long statusId;

        private Builder() {

        }

        public Builder libraryUserId(Long libraryUserId) {
            this.libraryUserId = libraryUserId;
            return this;
        }


        public Builder orderTime(LocalDateTime orderTime) {
            this.orderTime = orderTime;
            return this;
        }

        public Builder statusId(Long statusId) {
            this.statusId = statusId;
            return this;
        }

        public OrderCriteria build() {
            OrderCriteria criteria = new OrderCriteria(this);
            criteria.libraryUserId = libraryUserId;
            criteria.orderTime = orderTime;
            criteria.statusId = statusId;

            return criteria;
        }
    }
}
