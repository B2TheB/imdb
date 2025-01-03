package b2.theb.imdb.application;

import org.springframework.web.bind.annotation.RestController;

import b2.theb.imdb.application.models.CreateRatingRequest;
import b2.theb.imdb.business.RatingService;
import b2.theb.imdb.business.dto.RatingDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequiredArgsConstructor
public class RatingController {

    private static final Logger LOGGER = LogManager.getLogger(RatingController.class);

    private final RatingService ratingService;

    @GetMapping("rating/all")
    public ResponseEntity<String> getAllRatings(@RequestParam("pageNr") int pageNr, @RequestParam("pageSize") int pageSize) {
        final List<String> ratings = ratingService.getAllRatings(pageNr, pageSize).stream().map(RatingDto::toString).toList();

        return ResponseEntity.ok(String.join("\n", ratings));
    }

    @PostMapping("rating")
    public ResponseEntity<String> createRating(@RequestBody @Valid CreateRatingRequest request) {
        
        LOGGER.info("Creating rating {} for user: {} and movie: {}", request.getRating(), request.getUsername(), request.getMovieTitle());
        ratingService.createRating(request.getUsername(), request.getMovieTitle(), request.getRating());
        
        return ResponseEntity.status(201).body("Rating created");
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
