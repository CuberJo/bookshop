package com.epam.bookshop.validator.impl;

import com.epam.bookshop.constant.RegexConstants;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class RegexValidatorTest {

    @Test
    public void testValidateCorrectEmail() {
        assertTrue(RegexValidator.getInstance().validate("sdf@adsf.sdaf", RegexConstants.STRONG_EMAIL_REGEX));
    }

    @Test
    public void testValidateIncorrectEmail() {
        assertFalse(RegexValidator.getInstance().validate("sdf@adsfsdaf", RegexConstants.STRONG_EMAIL_REGEX));
    }

    @Test
    public void testValidateIncorrectEmail2() {
        assertFalse(RegexValidator.getInstance().validate("sdf@adsfewafdfddsd.csdaf", RegexConstants.STRONG_EMAIL_REGEX));
    }

    @Test
    public void testValidateTitle() {
        assertTrue(RegexValidator.getInstance().validate("Гормоны счастья. Как приучить мозг вырабатывать серотонин, дофамин, эндорфин и окситоцин", RegexConstants.TITLE_REGEX));
    }
}