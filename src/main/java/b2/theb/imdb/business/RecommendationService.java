package b2.theb.imdb.business;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import b2.theb.imdb.business.dto.MovieDto;
import b2.theb.imdb.business.exception.UserNotFoundException;
import b2.theb.imdb.persistence.Genre;
import b2.theb.imdb.persistence.Movie;
import b2.theb.imdb.persistence.Rating;
import b2.theb.imdb.persistence.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserService userService;
    private final MovieService movieService;

    public List<MovieDto> getRecommendationsFor(int userId) {
        final User user = userService.getUserBy(userId);
        if (user == null) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }
        if (user.getRatings().isEmpty()) {
            return movieService.getHighRateMovies();
        }
        final List<Movie> movies = user.getRatings().stream().filter(rating -> rating.getRating() >= 4).map(Rating::getMovie).toList();
        final Map<Genre, Long> ratedGenreMap = movies.stream().collect(Collectors.groupingBy(Movie::getGenre, Collectors.counting()));
        final List<Genre> favoriteGenreList = ratedGenreMap.entrySet().stream().sorted(Entry.comparingByValue()).map(Entry::getKey).toList();
        List<Genre> relevantGenres = favoriteGenreList.size() > 1? favoriteGenreList.subList(0, 1): favoriteGenreList;
        
        final List<MovieDto> highRateMoviesByGenre = movieService.getHighRateMoviesBy(relevantGenres, movies.stream().map(Movie::getId).toList());
        if (highRateMoviesByGenre.isEmpty()) {
            return movieService.getHighestRatedMoviesByGenres(relevantGenres);
        }
        return highRateMoviesByGenre;
    }
}
