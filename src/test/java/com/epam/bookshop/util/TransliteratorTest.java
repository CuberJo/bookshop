package com.epam.bookshop.util;

import com.epam.bookshop.constant.RegexConstants;
import com.epam.bookshop.util.convertor.Transliterator;
import org.testng.annotations.Test;

public class TransliteratorTest {

    @Test
    public void testTransliterate() {
        String s = "Гоша";
        if (s.matches(RegexConstants.CYRILLIC)) {
            System.out.println(Transliterator.getInstance().transliterate(s));
        }
    }
}