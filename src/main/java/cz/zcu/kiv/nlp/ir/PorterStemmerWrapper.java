package cz.zcu.kiv.nlp.ir;

import opennlp.tools.stemmer.PorterStemmer;

/**
 * Compat for interface
 */
public class PorterStemmerWrapper implements Stemmer {

    private final PorterStemmer porterStemmer = new PorterStemmer();

    @Override
    public String stem(String input) {
        return porterStemmer.stem(input);
    }
}
