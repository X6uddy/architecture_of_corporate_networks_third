package com.example.restapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Entity
@Table(name = "picture", schema = "work")
@Data
@XmlRootElement
public class Picture
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    @Column(nullable = false)
    private String title;

    @Min(value = 1000, message = "Year must be at least 1000")
    @Max(value = 2100, message = "Year must be no more than 2100")
    @Column(name = "\"year\"")
    private int year;

    @NotNull(message = "Artist is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Artist artist;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public int getYear() {return year;}
    public void setYear(int year) {this.year = year;}

    public Artist getArtist() {return artist;}
    public void setArtist(Artist artist) {this.artist = artist;}
}


