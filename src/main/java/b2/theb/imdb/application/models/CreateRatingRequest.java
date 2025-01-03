package b2.theb.imdb.application.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateRatingRequest {

    private String username;
    private String movieTitle;
    @Min(1)
    @Max(5)
    private int rating;
}
