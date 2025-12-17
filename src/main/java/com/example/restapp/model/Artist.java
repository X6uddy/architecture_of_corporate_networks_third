package com.example.restapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "artist", schema = "work")
@Data
@XmlRootElement
public class Artist
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Min(value = 1000, message = "Birth year must be at least 1000")
    @Max(value = 2100, message = "Birth year must be no more than 2100")
    private int birthYear;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Picture> pictures;

    public Long getId() {return id ;}
    public void setId(Long id) {this.id = id;}

    public String getFullName() {return fullName;}
    public void setFullName(String fullName) {this.fullName = fullName;}

    public int getBirthYear() {return birthYear;}
    public void setBirthYear(int birthYear) {this.birthYear = birthYear;}

    public List<Picture> getPictures() {return pictures;}
    public void setPictures(List<Picture> pictures) {this.pictures = pictures;}
}


