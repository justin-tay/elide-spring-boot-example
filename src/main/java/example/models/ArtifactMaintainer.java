/*
 * Copyright 2019, Yahoo Inc.
 * Licensed under the Apache License, Version 2.0
 * See LICENSE file in project root for terms.
 */
package example.models;

import java.util.ArrayList;
import java.util.List;

import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.graphql.subscriptions.annotations.Subscription;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Include(name = "maintainer", description = "Artifact maintainer.", friendlyName = "Maintainer")
@Table(name = "artifactmaintainer")
@Entity
@Subscription
@Data
public class ArtifactMaintainer {
    @Id
    private String name = "";

    @ElementCollection
    @CollectionTable(name="Tags", joinColumns=@JoinColumn(name="name"))
    private List<String> tags = new ArrayList<>();
}
