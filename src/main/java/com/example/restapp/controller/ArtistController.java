package com.example.restapp.controller;

import com.example.restapp.model.Artist;
import com.example.restapp.model.ChangeType;
import com.example.restapp.messaging.ChangeEventPublisher;
import com.example.restapp.repository.ArtistRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {
    private final ArtistRepository repository;
    private final ChangeEventPublisher publisher;

    public ArtistController(ArtistRepository repository, ChangeEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @GetMapping(produces = {"application/json", "application/xml"})
    public Page<Artist> getAll(@RequestParam Optional<String> fullName, Pageable pageable)
    {
        return fullName.isPresent() ? repository.findByFullNameContaining(fullName.get(), pageable) : repository.findAll(pageable);
    }

    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml"})
    public ResponseEntity<Artist> getById(@PathVariable Long id)
    {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    public ResponseEntity<Artist> create(@Valid @RequestBody Artist artist, UriComponentsBuilder uriBuilder)
    {
        Artist saved = repository.save(artist);
        publisher.publish(ChangeType.INSERT, "Artist", saved.getId().toString(), artistPayload(saved));
        URI location = uriBuilder.path("/api/artists/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping(value = "/{id}", consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    public ResponseEntity<Artist> update(@PathVariable Long id, @Valid @RequestBody Artist details)
    {
        return repository.findById(id).map(artist ->
        {
            artist.setFullName(details.getFullName());
            artist.setBirthYear(details.getBirthYear());
            Artist updated = repository.save(artist);
            publisher.publish(ChangeType.UPDATE, "Artist", updated.getId().toString(), artistPayload(updated));
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id)
    {
        return repository.findById(id).map(artist ->
        {
            publisher.publish(ChangeType.DELETE, "Artist", artist.getId().toString(), artistPayload(artist));
            repository.delete(artist);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    private static Object artistPayload(Artist artist)
    {
        return java.util.Map.of(
                "id", artist.getId(),
                "fullName", artist.getFullName(),
                "birthYear", artist.getBirthYear()
        );
    }
}


