package cz.zcu.kiv.nlp.ir;


import java.util.*;
import lombok.Getter;

/**
 * Created by Tigi on 29.2.2016.
 */
@Getter
public class BasicPreprocessing implements Preprocessing {

    private final Map<String, Integer> wordFrequencies = new HashMap<>();
    private final Stemmer stemmer;
    private final Tokenizer tokenizer;
    private final Set<String> stopwords;
    private final boolean removeAccentsBeforeStemming;
    private final boolean removeAccentsAfterStemming;
    private final boolean toLowercase;

    public BasicPreprocessing(Stemmer stemmer,
                              Tokenizer tokenizer,
                              Set<String> stopwords,
                              boolean removeAccentsBeforeStemming,
                              boolean removeAccentsAfterStemming,
                              boolean toLowercase) {
        this.stemmer = stemmer;
        this.tokenizer = tokenizer;
        this.stopwords = stopwords == null ? new HashSet<>() : stopwords;
        this.removeAccentsBeforeStemming = removeAccentsBeforeStemming;
        this.removeAccentsAfterStemming = removeAccentsAfterStemming;
        this.toLowercase = toLowercase;
    }

    @Override
    public void index(String document) {
        if (toLowercase) {
            document = document.toLowerCase();
        }
        if (removeAccentsBeforeStemming) {
            document = removeAccents(document);
        }
        var tokens = tokenizer.tokenize(document);
        for (String token : tokens) {
            if (stopwords.contains(token)) {
                continue;
            }
            if (stemmer != null) {
                token = stemmer.stem(token);
            }
            if (removeAccentsAfterStemming) {
                token = removeAccents(token);
            }

            if (!wordFrequencies.containsKey(token)) {
                wordFrequencies.put(token, 0);
            }
            wordFrequencies.put(token, wordFrequencies.get(token) + 1);
        }
    }

    @Override
    public String getProcessedForm(String text) {
        if (toLowercase) {
            text = text.toLowerCase();
        }
        if (removeAccentsBeforeStemming) {
            text = removeAccents(text);
        }
        if (stemmer != null) {
            text = stemmer.stem(text);
        }
        if (removeAccentsAfterStemming) {
            text = removeAccents(text);
        }
        return text;
    }

    final String withDiacritics = "áàÁčĆćďĎéÉěĚíÍňŇóÓřŘšŠťŤúÚůŮýÝžŽ";
    final String withoutDiacritics = "aaAcCcdDeEeEiInNoOrRsStTuUuUyYzZ";

    private String removeAccents(String text) {
        for (int i = 0; i < withDiacritics.length(); i++) {
            text = text.replaceAll("" + withDiacritics.charAt(i), "" + withoutDiacritics.charAt(i));
        }
        return text;
    }

    public Map<String, Integer> getWordFrequencies() {
        return wordFrequencies;
    }
}
