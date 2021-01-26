package com.epam.bookshop.util.criteria.impl;

import com.epam.bookshop.util.criteria.Criteria;
import com.epam.bookshop.domain.impl.Payment;

import java.time.LocalDateTime;

public class PaymentCriteria extends Criteria<Payment> {
    private Long libraryUserId;
    private Long bookId;
    private LocalDateTime paymentTime;
    private Double price;

    public PaymentCriteria(Criteria.Builder<? extends Criteria.Builder> builder) {
        super(builder);
    }

    public Long getLibraryUserId() {
        return libraryUserId;
    }

    public Long getBookId() {
        return bookId;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public Double getPrice() {
        return price;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Criteria.Builder<PaymentCriteria.Builder> {
        private Long libraryUserId;
        private Long bookId;
        private LocalDateTime paymnetTime;
        private Double price;

        private Builder() {

        }

        public Builder libraryUserId(Long libraryUserId) {
            this.libraryUserId = libraryUserId;
            return this;
        }

        public Builder bookId(Long bookId) {
            this.bookId = bookId;
            return this;
        }


        public Builder paymentTime(LocalDateTime paymnetTime) {
            this.paymnetTime = paymnetTime;
            return this;
        }

        public Builder price(Double price) {
            this.price = price;
            return this;
        }

        public PaymentCriteria build() {
            PaymentCriteria criteria = new PaymentCriteria(this);
            criteria.libraryUserId = libraryUserId;
            criteria.bookId = bookId;
            criteria.paymentTime = paymnetTime;
            criteria.price = price;

            return criteria;
        }
    }
}
