package com.epam.bookshop.validator.impl;

import com.epam.bookshop.constant.ErrorMessageConstants;
import com.epam.bookshop.constant.RegexConstants;
import com.epam.bookshop.exception.ValidatorException;
import com.epam.bookshop.util.locale_manager.ErrorMessageManager;

/**
 * Validator for IBAN
 */
public class IbanValidator {

    private String locale = "US";

    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * Validates passed by client IBAN string
     *
     * @throws ValidatorException if string fails validation
     */
    public void validate(String IBAN) throws ValidatorException {

        if (StringValidator.getInstance().empty(IBAN)) {
            throw new ValidatorException(ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.EMPTY_IBAN));
        }

        if (!StringValidator.getInstance().validate(IBAN, RegexConstants.IBAN_REGEX)) {
            throw new ValidatorException(ErrorMessageManager.valueOf(locale).getMessage(ErrorMessageConstants.IBAN_INCORRECT));
        }
    }
}
