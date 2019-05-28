package org.fenixedu;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public class NumberToText {

    private static final String[] EURO = { "euro", "euros" };
    private static final String[] REAL = { "real", "reais" };
    private static final String[] METICAL = { "metical", "meticais" };
    private static final String[] KWANZA = { "kwanza", "kwanzas" };
    private static final String[] DOLLAR = { "dollar", "dollars" };
    private static final String[] POUND = { "libra", "libras" };

    private static final Map<Locale, Function<BigDecimal, String>> TO_TEXT_FUNCTIONS = Collections.synchronizedMap(new HashMap<>());
    static {
        final Function<BigDecimal, String> ptEuro = number -> new NumberToTextPT(EURO, number).toString();
        TO_TEXT_FUNCTIONS.put(new Locale("pt"), ptEuro);
        TO_TEXT_FUNCTIONS.put(new Locale("pt", "PT"), ptEuro);
        TO_TEXT_FUNCTIONS.put(new Locale("pt", "BR"), number -> new NumberToTextPT(REAL, number).toString());
        TO_TEXT_FUNCTIONS.put(new Locale("pt", "MZ"), number -> new NumberToTextPT(METICAL, number).toString());
        TO_TEXT_FUNCTIONS.put(new Locale("pt", "AO"), number -> new NumberToTextPT(KWANZA, number).toString());
        TO_TEXT_FUNCTIONS.put(new Locale("pt", "US"), number -> new NumberToTextPT(DOLLAR, number).toString());
        TO_TEXT_FUNCTIONS.put(new Locale("pt", "UK"), number -> new NumberToTextPT(POUND, number).toString());
    }

    public static String toText(final Locale locale, final BigDecimal number) {
        if (locale == null) {
            throw new NullPointerException("A locale must be specified.");
        }
        final Function<BigDecimal, String> function = TO_TEXT_FUNCTIONS.get(locale);
        if (function == null) {
            throw new Error("There is no function registered to map numbers to text for locale: " + locale.getLanguage() + " " + locale.getCountry());
        }
        return function.apply(number);
    }

    public static void registerMappingFunction(final Locale locale, final Function<BigDecimal, String> function) {
        if (locale == null) {
            throw new NullPointerException("A locale must be specified.");
        }
        if (function == null) {
            throw new NullPointerException("A function must be specified.");
        }
        TO_TEXT_FUNCTIONS.put(locale, function);
    }

}
