package cz.zcu.kiv.nlp.ir.metacritic;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MetacriticReview {

    private String reviewerName;

    @JsonDeserialize(using = MetacriticJsonDateDeserializer.class)
    private Date dateReviewed;

    // This is probably unnecessary but just to be sure
    @JsonDeserialize(using = MetacriticJsonDoubleDeserializer.class)
    private Double score;

    private String text;

}
