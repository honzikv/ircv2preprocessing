package cz.zcu.kiv.nlp.ir.metacritic.serialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Jackson deserialization class
 */
public class MetacriticJsonDeserializer {

    public List<MetacriticGame> deserialize(String filename) throws IOException {
        var mapper = new JsonMapper();
        var fileUri = getClass().getClassLoader().getResource(filename);
        if (fileUri == null) {
            throw new FileNotFoundException("Could not find file: " + filename + " in the resources folder");
        }

        // First read by year
        var yearsMap = mapper.readValue(fileUri,
                new TypeReference<HashMap<String, List<LinkedHashMap<String, Object>>>>() { });

        var mappedItems = new ArrayList<MetacriticGame>();
        yearsMap.forEach((year, gamesList) -> {
            mappedItems.addAll(gamesList.stream().map(gameJson -> {
                var gameMapped = mapper.convertValue(gameJson, MetacriticGame.class);
                gameMapped.getUserReviews().forEach(review -> {
                    // User reviews are in scale of 0 - 10 so normalize them to 0 - 100
                    review.setScore(review.getScore() * 10);
                });
                return gameMapped;
            }).toList());
        });

        return mappedItems;
    }

}
