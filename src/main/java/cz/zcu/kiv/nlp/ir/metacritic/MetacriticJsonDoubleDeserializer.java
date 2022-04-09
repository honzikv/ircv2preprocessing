package cz.zcu.kiv.nlp.ir.metacritic;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Some reviews may have "TBD" rating which should get mapped to null
 */
public class MetacriticJsonDoubleDeserializer extends JsonDeserializer<Double> {
    @Override
    public Double deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        var value = jsonParser.getText();
        return NumberUtils.isParsable(value) ? NumberUtils.toDouble(value) : null;
    }
}
