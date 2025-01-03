package b2.theb.imdb.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MovieDto {

    private String title;
    private double rating;

    @Override
    public String toString() {
        return "MovieDto{" +
                "title='" + title + '\'' +
                ", rating=" + rating +
                '}';
    }
}
