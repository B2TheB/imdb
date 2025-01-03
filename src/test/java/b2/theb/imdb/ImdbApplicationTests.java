package b2.theb.imdb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import b2.theb.imdb.application.models.CreateUserRequest;
import b2.theb.imdb.persistence.Genre;
import b2.theb.imdb.persistence.Movie;
import b2.theb.imdb.persistence.MovieRepository;
import b2.theb.imdb.persistence.UserRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ImdbApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		userRepository.deleteAll();
	}

	@Test
	void shouldCreateUser() throws Exception {
		// given & when
		createUser("testuser");

		// then
		Assertions.assertNotNull(userRepository.findByUsername("testuser"));
	}
	
	void createUser(final String username) throws Exception {
        CreateUserRequest request = new CreateUserRequest(username, "testpassword", "testuser@example.com");

        // when
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
	}

	@Test
	void shouldNotCreateUserWhenUsernameExists() throws Exception {
		// given
		createUser("testuser");
		CreateUserRequest request = new CreateUserRequest("testuser", "testpassword", "testuser@example.com");

		// when & then
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
	}

	@Test
	void shouldRateAMovie() throws Exception {
		// given
		createUser("testuser");
		final Movie movie = Movie.builder().title("Dumb and Dumber").genre(Genre.COMEDY).build();
		movieRepository.save(movie);
		
		// when
		mockMvc.perform(post("/rating")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\":\"testuser\",\"movieTitle\":\"Dumb and Dumber\",\"rating\":5}"))
				.andExpect(status().isCreated());

		// then
		Assertions.assertEquals(5.0, movieRepository.findOneByTitle("Dumb and Dumber").getRating());
	}
	
	@Test
	void shouldUpdateRating() throws Exception {
		// given
		createUser("testuser1");
		createUser("testuser2");
		createUser("testuser3");
		createUser("testuser4");
		final Movie movie = Movie.builder().title("Dumb and Dumber").genre(Genre.COMEDY).build();
		movieRepository.save(movie);
		
		// when
		mockMvc.perform(post("/rating")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\":\"testuser1\",\"movieTitle\":\"Dumb and Dumber\",\"rating\":5}"))
				.andExpect(status().isCreated());
		
		// when
		mockMvc.perform(post("/rating")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\":\"testuser2\",\"movieTitle\":\"Dumb and Dumber\",\"rating\":2}"))
				.andExpect(status().isCreated());
		
		// when
		mockMvc.perform(post("/rating")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\":\"testuser3\",\"movieTitle\":\"Dumb and Dumber\",\"rating\":3}"))
				.andExpect(status().isCreated());
		
		// when
		mockMvc.perform(post("/rating")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\":\"testuser4\",\"movieTitle\":\"Dumb and Dumber\",\"rating\":4}"))
				.andExpect(status().isCreated());

		// then
		Assertions.assertEquals((2+3+4+5)/4.0, movieRepository.findOneByTitle("Dumb and Dumber").getRating());
	}
}
