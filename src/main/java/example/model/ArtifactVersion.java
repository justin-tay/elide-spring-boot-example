/*
 * Copyright 2019, Yahoo Inc.
 * Licensed under the Apache License, Version 2.0
 * See LICENSE file in project root for terms.
 */
package example.model;

import com.yahoo.elide.annotation.Include;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;

@Include(rootLevel = false, name = "versions", description = "Artifact version.", friendlyName = "Version")
@Table(name = "artifactversion")
@Entity
@Data
public class ArtifactVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VERSION_SEQ")
    @SequenceGenerator(name = "VERSION_SEQ", allocationSize = 1)
    private Long id;

    private String name = "";

    @NotNull
    private OffsetDateTime createdOn = OffsetDateTime.now();

    @ManyToOne
    private ArtifactProduct product;
}
