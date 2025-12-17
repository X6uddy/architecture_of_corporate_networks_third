package com.example.restapp.repository;

import com.example.restapp.model.Picture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, Long>
{
    Page<Picture> findByArtistId(Long artistId, Pageable pageable);
}


