/*
 * Copyright 2019, Yahoo Inc.
 * Licensed under the Apache License, Version 2.0
 * See LICENSE file in project root for terms.
 */
package example.models.v1;

import com.yahoo.elide.annotation.Include;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Include(name = "group", description = "Artifact group.", friendlyName = "GroupV1")
@Table(name = "artifactgroup")
@Entity
@Data
public class ArtifactGroupV1 {
    @Id
    private String name = "";

    private String commonName = "";

    private String description = "";

    @OneToMany(mappedBy = "group")
    private List<ArtifactProductV1> products = new ArrayList<>();
}
