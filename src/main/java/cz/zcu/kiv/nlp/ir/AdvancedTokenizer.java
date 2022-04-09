/**
 * Copyright (c) 2014, Michal Konkol
 * All rights reserved.
 */
package cz.zcu.kiv.nlp.ir;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Michal Konkol
 */
public class AdvancedTokenizer implements Tokenizer {
    //cislo |  | html | tecky a ostatni
    public static final String DEFAULT_REGEX = "(\\d+[.,](\\d+)?)|([\\p{L}\\d]+)|(<.*?>)|([\\p{Punct}])";
    public static final String URL_REGEX =
            "^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))" +
                    "(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)" +
                    "([).!';/?:,][[:blank:]])?$";
    public static final String CENSORED_REGEX = "[\\w+À-ž]+\\*[\\w+À-ž]*|[\\w+À-ž]*\\*[\\w+À-ž]+";
    public static final String DATE_REGEX = "\\d{1,2}\\.\\s?\\d{1,2}\\.\\s*\\d{0,4}";

    // Used for cleaning out sentences that have some english in them but also other non-latin symbol such
    // as arabic letters...
    public static final String BASIC_LATIN_CHARACTERS = "[\\p{IsCyrillic}\\p{script=Han}\\p{script=Hiragana}" +
            "\\p{script=Katakana}\\u0600-\\u06FF]";

    private static final Set<String> regexes = Set.of(DEFAULT_REGEX, URL_REGEX, CENSORED_REGEX, DATE_REGEX);

    public static String[] tokenize(String text, String regex) {
        var cleanedText = text.replaceAll(BASIC_LATIN_CHARACTERS, "");
        var pattern = Pattern.compile(regex);
        var words = new HashSet<String>();
        var matcher = pattern.matcher(cleanedText);
        while (matcher.find()) {
            var start = matcher.start();
            var end = matcher.end();
            words.add(cleanedText.substring(start, end));
        }

        var ws = new String[words.size()];
        ws = words.toArray(ws);
        return ws;
    }

    public static String removeAccents(String text) {
        return text == null ? null : Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    @Override
    public String[] tokenize(String text) {
        var items = new HashSet<String>();

        // Split by whitespaces
        var words = new ArrayList<>(List.of(text.split("\\s+")));
        words.add(text);

        regexes.forEach(regex -> words.forEach(word -> items.addAll(List.of(tokenize(word, regex)))));

        var arr = new String[items.size()];
        items.toArray(arr);
        return arr;
    }
}
