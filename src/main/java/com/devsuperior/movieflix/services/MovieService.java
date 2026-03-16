package com.devsuperior.movieflix.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieIdProjection;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repository;

    @Transactional(readOnly = true)
    public MovieDetailsDTO findById(Long id) {

        Movie entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Id " + id + " not found"));

        return new MovieDetailsDTO(entity, entity.getGenre());
    }

    @Transactional(readOnly = true)
    public Page<MovieCardDTO> findMovieByGenre(Long genreId, Pageable pageable) {

        List<Long> genresIds = null;

        if (genreId != null && genreId != 0L) {
            genresIds = List.of(genreId);
        }

        Page<MovieIdProjection> page = repository.searchMovies(genresIds, pageable);
        List<Long> moviesIds = page.map(x -> x.getId()).toList();

        List<Movie> list = repository.searchMovieWithgenres(moviesIds);
        List<MovieCardDTO> listDto = list.stream().map(x -> new MovieCardDTO(x)).toList();

        Page<MovieCardDTO> pageDto = new PageImpl<>(listDto, pageable, page.getTotalElements());

        return pageDto;
    }
}
