package b2.theb.imdb.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT m FROM Movie m WHERE m.title = ?1")
    Movie findOneByTitle(String title);

    @Query("SELECT m FROM Movie m WHERE m.rating >= 4 order by m.rating desc limit 10")
    List<Movie> findHighRateMovies();

    @Query("SELECT m FROM Movie m WHERE m.genre in (?1) and m.rating >= 4 and not m.id in (?2) order by m.rating desc limit 10")
    List<Movie> findHighRateMoviesBy(List<Genre> genre, List<Long> filteredOutMovies);
}
