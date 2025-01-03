package b2.theb.imdb.business;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import b2.theb.imdb.business.dto.MovieDto;
import b2.theb.imdb.persistence.Genre;
import b2.theb.imdb.persistence.Movie;
import b2.theb.imdb.persistence.MovieRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public List<MovieDto> getAllMovies(final int pageNr, final int pageSize) {
        return movieRepository.findAll(PageRequest.of(pageNr, pageSize)).stream().map(this::toDto).toList();
    }

    public List<MovieDto> getHighRateMovies() {
        return movieRepository.findHighRateMovies().stream().map(this::toDto).toList();
    }

    public List<MovieDto> getHighRateMoviesBy(final List<Genre> favoriteGenres, final List<Long> filteredOutMovies) {
        return movieRepository.findHighRateMoviesBy(favoriteGenres, filteredOutMovies).stream().map(this::toDto).toList();
    }

    public MovieDto getMovie(String title) {
        return toDto(movieRepository.findOneByTitle(title));
    }

    protected MovieDto toDto(Movie movie) {
        if (movie == null) {
            return null;
        }
        return MovieDto.builder().title(movie.getTitle()).rating(movie.getRating()).build();
    }

    public List<MovieDto> getHighestRatedMoviesByGenres(List<Genre> relevantGenres) {
        return getHighRateMoviesBy(relevantGenres, List.of());
    }
}
