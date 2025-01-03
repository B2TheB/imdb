package b2.theb.imdb.application;

import org.springframework.web.bind.annotation.RestController;

import b2.theb.imdb.application.models.CreateUserRequest;
import b2.theb.imdb.business.RecommendationService;
import b2.theb.imdb.business.UserService;
import b2.theb.imdb.business.dto.MovieDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequiredArgsConstructor
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    private final UserService userService;
    private final RecommendationService recommendationService;

    @GetMapping("user/all")
    public ResponseEntity<String> getAllUsers() {
        final String users = String.join(",", userService.getAllUsernames());
        return ResponseEntity.ok(users);
    }

    @PostMapping("user")
    public ResponseEntity<String> createUser(@RequestBody CreateUserRequest request) {
        
        LOGGER.info("Creating user with username: {}", request.getUsername());
        userService.createUser(request.getUsername(), request.getPassword(), request.getEmail());
        return ResponseEntity.status(201).body("User created");
    }
    
    @GetMapping("user/{id}/recommendations")
    public ResponseEntity<String> getRecommendationsFor(@PathVariable("id") int userId) {
        final List<MovieDto> recommendations = recommendationService.getRecommendationsFor(userId);
        return ResponseEntity.ok(String.join("\n", recommendations.stream().map(MovieDto::toString).toList()));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
