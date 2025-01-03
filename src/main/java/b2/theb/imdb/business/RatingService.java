package b2.theb.imdb.business;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import b2.theb.imdb.business.dto.RatingDto;
import b2.theb.imdb.business.exception.MovieNotFoundException;
import b2.theb.imdb.business.exception.UserNotFoundException;
import b2.theb.imdb.persistence.Movie;
import b2.theb.imdb.persistence.MovieRepository;
import b2.theb.imdb.persistence.Rating;
import b2.theb.imdb.persistence.RatingRepository;
import b2.theb.imdb.persistence.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;
    private final UserService userService;
    private final MovieService movieService;

    public List<RatingDto> getAllRatings() {
        return ratingRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional
    public void createRating(final String username, final String movieTitle, final int rating) {
        final User user = userService.getUser(username);
        if (user == null) {
            throw new UserNotFoundException("User " + username + " not found");
        }
        final Movie movie = movieRepository.findOneByTitle(movieTitle);
        if (movie == null) {
            throw new MovieNotFoundException("Movie " + movieTitle + " not found");
        }

        final Rating newRating = Rating.builder().user(user).movie(movie).rating(rating).build();
        ratingRepository.save(newRating);

        // update movie rating
        if (movie.getRatingCount() == null) {
            movie.setRating(rating * 1.0);
            movie.setRatingCount(1);
        } else {
            movie.setRating((movie.getRating() * movie.getRatingCount() + rating) / (movie.getRatingCount() + 1));
            movie.setRatingCount(movie.getRatingCount() + 1);
        }
        movieRepository.save(movie);
    }

    protected RatingDto toDto(Rating rating) {
        if (rating == null) {
            return null;
        }
        return RatingDto.builder().rating(rating.getRating()).movie(movieService.toDto(rating.getMovie())).build();
    }
}
