package com.example.restapp.controller;

import com.example.restapp.model.Picture;
import com.example.restapp.repository.PictureRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/pictures")
public class PictureController
{
    private final PictureRepository repository;

    public PictureController(PictureRepository repository) {
        this.repository = repository;
    }

    @GetMapping(produces = {"application/json", "application/xml"})
    public Page<Picture> getAll(@RequestParam Optional<Long> artistId, Pageable pageable)
    {
        return artistId.isPresent() ? repository.findByArtistId(artistId.get(), pageable) : repository.findAll(pageable);
    }

    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml"})
    public ResponseEntity<Picture> getById(@PathVariable Long id)
    {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    public ResponseEntity<Picture> create(@Valid @RequestBody Picture picture, UriComponentsBuilder uriBuilder)
    {
        Picture saved = repository.save(picture);
        URI location = uriBuilder.path("/api/pictures/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping(value = "/{id}", consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    public ResponseEntity<Picture> update(@PathVariable Long id, @Valid @RequestBody Picture details)
    {
        return repository.findById(id).map(picture ->
        {
            picture.setTitle(details.getTitle());
            picture.setYear(details.getYear());
            picture.setArtist(details.getArtist());
            return ResponseEntity.ok(repository.save(picture));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id)
    {
        return repository.findById(id).map(picture ->
        {
            repository.delete(picture);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}


