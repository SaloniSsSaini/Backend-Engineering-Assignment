package com.app.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Bot {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String personaDescription;
}