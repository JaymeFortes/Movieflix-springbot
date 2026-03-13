package com.devsuperior.movieflix.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.movieflix.dto.GenreDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.repositories.GenreRepository;

@Service
public class GenreService {

    @Autowired
    private GenreRepository repository;

     public Page<GenreDTO> findAllPaged(Pageable pageable) {
        Page<Genre> categories = repository.findAll(pageable);
        return categories.map(x -> new GenreDTO(x));
     }
    
}
