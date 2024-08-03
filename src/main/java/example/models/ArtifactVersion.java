/*
 * Copyright 2019, Yahoo Inc.
 * Licensed under the Apache License, Version 2.0
 * See LICENSE file in project root for terms.
 */
package example.models;

import com.yahoo.elide.annotation.Include;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

@Include(rootLevel = false, name = "version", description = "Artifact version.", friendlyName = "Version")
@Table(name = "artifactversion")
@Entity
public class ArtifactVersion {
    @Id
    private String name = "";

    @NotNull
    private OffsetDateTime createdOn = OffsetDateTime.now();

    @ManyToOne
    private ArtifactProduct artifact;
}
