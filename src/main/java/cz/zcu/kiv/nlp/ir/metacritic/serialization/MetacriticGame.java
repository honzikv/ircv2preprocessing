package cz.zcu.kiv.nlp.ir.metacritic.serialization;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetacriticGame {

    private String name;

    @JsonDeserialize(using = MetacriticJsonDoubleDeserializer.class)
    private Double metacriticRating;

    @JsonDeserialize(using = MetacriticJsonDoubleDeserializer.class)
    private Double userRating;

    @JsonProperty("critic_reviews")
    private List<MetacriticReview> criticReviews;

    private List<MetacriticReview> userReviews;
}
