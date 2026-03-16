package com.devsuperior.movieflix.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieIdProjection;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query(nativeQuery = true, value = """
            SELECT DISTINCT tb_movie.id AS id
            FROM tb_movie
            WHERE (:genreIds IS NULL OR tb_movie.genre_id IN (:genreIds))
            """, countQuery = """
            SELECT COUNT(*) FROM (
            SELECT DISTINCT tb_movie.id
            FROM tb_movie
            WHERE (:genreIds IS NULL OR tb_movie.genre_id IN (:genreIds))
            ) AS count_query
            """)
    Page<MovieIdProjection> searchMovies(@Param("genreIds") List<Long> genresIds, Pageable pageable);

    @Query("SELECT obj FROM Movie obj JOIN FETCH obj.genre "
            + "WHERE obj.id IN :movieIds ORDER BY obj.title")
    List<Movie> searchMovieWithgenres(@Param("movieIds") List<Long> movieIds);

}
