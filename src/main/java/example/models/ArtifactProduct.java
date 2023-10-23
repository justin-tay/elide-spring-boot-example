/*
 * Copyright 2019, Yahoo Inc.
 * Licensed under the Apache License, Version 2.0
 * See LICENSE file in project root for terms.
 */
package example.models;

import com.yahoo.elide.annotation.Include;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Include(rootLevel = false, name = "product", description = "Artifact product.", friendlyName = "Product")
@Table(name = "artifactproduct")
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ArtifactProduct {
    @Id
    private String name = "";

    private String commonName = "";

    private String description = "";

    @ManyToOne
    private ArtifactGroup group = null;

    @OneToMany(mappedBy = "artifact")
    private List<ArtifactVersion> versions = new ArrayList<>();
}
