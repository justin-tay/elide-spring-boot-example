package example.models;

import com.yahoo.elide.annotation.CreatePermission;
import com.yahoo.elide.annotation.DeletePermission;
import com.yahoo.elide.annotation.Exclude;
import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.annotation.Paginate;
import com.yahoo.elide.annotation.PaginationMode;
import com.yahoo.elide.annotation.ReadPermission;
import com.yahoo.elide.annotation.UpdatePermission;
import com.yahoo.elide.core.security.checks.prefab.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Artifact Group.
 * <p>
 * Use page[number] and page[size] to get records.
 */
@Include(name = "groupPage", description = "Artifact group page.", friendlyName = "Group Page")
@Data
@CreatePermission(expression = Role.NONE_ROLE)
@ReadPermission(expression = Role.ALL_ROLE)
@UpdatePermission(expression = Role.NONE_ROLE)
@DeletePermission(expression = Role.NONE_ROLE)
@Paginate(modes = PaginationMode.OFFSET, countable = true)
@Schema(description = """
        Use page[number] and page[size] to get records.
        """)
public class ArtifactGroupPage {
    @Id
    @Exclude
    private String name = "";

    private String commonName = "";

    private String description = "";
}
