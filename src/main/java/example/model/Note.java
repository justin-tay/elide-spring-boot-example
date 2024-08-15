package example.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.yahoo.elide.annotation.DeletePermission;
import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.annotation.Paginate;
import com.yahoo.elide.annotation.PaginationMode;
import com.yahoo.elide.annotation.ReadPermission;
import com.yahoo.elide.annotation.UpdatePermission;

import example.security.check.NoteCreatorIsUserCheck;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Notes.
 * <p>
 * This is used to demonstrate the use of id obfuscation.
 */
@Include(name = "notes", description = "Notes.", friendlyName = "Note")
@Table(name = "note")
@Entity
@Data
@Paginate(modes = { PaginationMode.OFFSET, PaginationMode.CURSOR })
//@ReadPermission(expression = NoteCreatorIsUserCheck.NOTE_CREATOR_IS_USER)
@UpdatePermission(expression = NoteCreatorIsUserCheck.NOTE_CREATOR_IS_USER)
@DeletePermission(expression = NoteCreatorIsUserCheck.NOTE_CREATOR_IS_USER)
@EntityListeners(AuditingEntityListener.class)
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTE_SEQ")
    @SequenceGenerator(name = "NOTE_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "title")
    private String title = "";

    @Column(name = "content")
    private String content = "";

    @Column(name = "content_html")
    private String contentHtml = "";

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name="created_by")
    @CreatedBy
    @EqualsAndHashCode.Exclude
    private AppUser createdBy;

    @Column(name = "created_on")
    @CreatedDate
    private OffsetDateTime createdOn;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name="parent_id")
    @EqualsAndHashCode.Exclude
    private Note parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private List<Note> replies = new ArrayList<>();
}
