package com.app.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private boolean isPremium;
}