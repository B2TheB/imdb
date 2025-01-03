package b2.theb.imdb.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RatingDto {
    
    private UserDto user;
    private MovieDto movie;
    private Integer rating;
    
    @Override
    public String toString() {
        return "RatingDto{" +
                "user=" + user +
                ", movie=" + movie +
                ", rating=" + rating +
                '}';
    }
}
