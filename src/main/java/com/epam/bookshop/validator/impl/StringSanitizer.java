package com.epam.bookshop.validator.impl;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class StringSanitizer {
    public String sanitize(String s) {
        return Jsoup.clean(s, Whitelist.none());
    }
}
