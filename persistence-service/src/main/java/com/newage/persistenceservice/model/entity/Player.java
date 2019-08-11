package com.newage.persistenceservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "player")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;
}
