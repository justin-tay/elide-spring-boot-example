package example.models;

import java.util.List;

import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.annotation.ReadPermission;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Include(name = "author", friendlyName = "Author")
@Table(name = "author")
@Entity
@ReadPermission(expression = "author has user accessible books")
@SequenceGenerator(name = "authorIdSeq", sequenceName = "AUTHOR_ID_SEQ", allocationSize = 1)
public class Author {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "authorIdSeq")
	private Long id;
	
	private String name;
	
	@OneToMany(mappedBy = "author")
	private List<AuthorBook> authorBooks;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
