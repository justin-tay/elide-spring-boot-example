package example.models;

import java.util.List;

import com.yahoo.elide.annotation.Include;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Include(name = "book", friendlyName = "Book")
@Table(name = "book")
@Entity
public class Book {
	@Id
	private Long id;
	
	private String title;
	
	@OneToMany(mappedBy = "book")
	private List<AuthorBook> authorBooks;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<AuthorBook> getAuthorBooks() {
		return authorBooks;
	}

	public void setAuthorBooks(List<AuthorBook> authorBooks) {
		this.authorBooks = authorBooks;
	}
}
