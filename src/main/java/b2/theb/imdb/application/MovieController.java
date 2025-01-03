package b2.theb.imdb.application;

import org.springframework.web.bind.annotation.RestController;

import b2.theb.imdb.business.MovieService;
import b2.theb.imdb.business.dto.MovieDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequiredArgsConstructor
public class MovieController {

    private static final Logger LOGGER = LogManager.getLogger(MovieController.class);

    private final MovieService movieService;

    @GetMapping("movie/all")
    public ResponseEntity<String> getAllMovies() {
        final List<String> movies = movieService.getAllMovies().stream().map(MovieDto::toString).toList();

        return ResponseEntity.ok(String.join("\n", movies));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
