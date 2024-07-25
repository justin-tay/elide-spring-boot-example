package example.models;

import java.util.ArrayList;
import java.util.List;

import com.yahoo.elide.annotation.EntityId;
import com.yahoo.elide.annotation.Include;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Discussion message posts.
 * <p>
 * This is used to demonstrate the use of {@link EntityId}.
 */
@Include(name = "posts", description = "Discussion message posts.", friendlyName = "Post")
@Table(name = "post")
@Entity
@Data
public class Post {
    @Id
    @Embedded
    private ObfuscatedId id;

    @Column(name = "title")
    private String title = "";

    @Column(name = "content")
    private String content = "";

    @Column(name = "content_html")
    private String contentHtml = "";

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name="parent_id")
    private Post parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> replies = new ArrayList<>();
}
