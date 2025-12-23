package com.example.restapp.controller;

import com.example.restapp.model.Picture;
import com.example.restapp.model.ChangeType;
import com.example.restapp.messaging.ChangeEventPublisher;
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
    private final ChangeEventPublisher publisher;

    public PictureController(PictureRepository repository, ChangeEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
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
        publisher.publish(ChangeType.INSERT, "Picture", saved.getId().toString(), picturePayload(saved));
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
            Picture updated = repository.save(picture);
            publisher.publish(ChangeType.UPDATE, "Picture", updated.getId().toString(), picturePayload(updated));
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id)
    {
        return repository.findById(id).map(picture ->
        {
            publisher.publish(ChangeType.DELETE, "Picture", picture.getId().toString(), picturePayload(picture));
            repository.delete(picture);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    private static Object picturePayload(Picture picture)
    {
        Long artistId = picture.getArtist() != null ? picture.getArtist().getId() : null;
        return java.util.Map.of(
                "id", picture.getId(),
                "title", picture.getTitle(),
                "year", picture.getYear(),
                "artistId", artistId
        );
    }
}


