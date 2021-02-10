package com.epam.bookshop.util;

import com.epam.bookshop.constant.RegexConstants;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class TransliteratorTest {

    @Test
    public void testTransliterate() {
        String s = "Гоша";
        if (s.matches(RegexConstants.CYRILLIC)) {
            System.out.println(Transliterator.getInstance().transliterate(s));
        }
    }
}