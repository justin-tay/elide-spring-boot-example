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
 * Use page[first] to start scrolling from the start and page[after] and
 * page[size] to get next records.
 * <p>
 * Use page[last] to start scrolling from the end and page[before] and
 * page[size] to get previous records.
 */
@Include(name = "groupStream", description = "Artifact group stream.", friendlyName = "Group Stream")
@Data
@CreatePermission(expression = Role.NONE_ROLE)
@ReadPermission(expression = Role.ALL_ROLE)
@UpdatePermission(expression = Role.NONE_ROLE)
@DeletePermission(expression = Role.NONE_ROLE)
@Paginate(modes = PaginationMode.CURSOR, countable = true)
@Schema(description = """
        Use page[first] to start scrolling from the start and page[after] and page[size] to get next records.
        
        Use page[last] to start scrolling from the end and page[before] and page[size] to get previous records
        """)
public class ArtifactGroupStream {
    @Id
    @Exclude
    private String name = "";

    private String commonName = "";

    private String description = "";
}
