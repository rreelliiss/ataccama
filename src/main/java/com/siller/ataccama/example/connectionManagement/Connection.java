package com.siller.ataccama.example.connectionManagement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Connection {
    @Id
    @NotNull
    private String name;

    @NotNull
    @Valid
    @Embedded
    private ConnectionDetails connectionDetails;
}
