package b2.theb.imdb.business;

import java.util.List;

import org.springframework.stereotype.Service;

import b2.theb.imdb.persistence.UserRepository;
import b2.theb.imdb.business.dto.MovieDto;
import b2.theb.imdb.business.dto.RatingDto;
import b2.theb.imdb.business.dto.UserDto;
import b2.theb.imdb.persistence.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<String> getAllUsernames() {
        return userRepository.findAll().stream().map(User::getUsername).toList();
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserBy(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean createUser(String username, String password, String email) {
        if (userRepository.findByUsername(username) != null) {
            return false;
        }

        userRepository.save(User.builder().username(username).password(password).email(email).build());
        return true;
    }

    protected UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        if (user.getRatings() == null) {
            return UserDto.builder().user(user.getUsername()).build();
        }
        return UserDto.builder()
        .user(user.getUsername())
        .ratings(user.getRatings().stream().map(rating -> RatingDto.builder()
            .movie(MovieDto.builder().title(rating.getMovie().getTitle()).build())
            .rating(rating.getRating()).build()).toList())
        .build();
    }
}
