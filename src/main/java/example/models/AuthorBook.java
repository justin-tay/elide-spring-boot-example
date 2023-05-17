package example.models;

import com.yahoo.elide.annotation.Include;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Include(rootLevel = false, name = "authorBook", friendlyName = "Author Book")
@Table(name = "author_book")
@Entity
@SequenceGenerator(name = "authorBookIdSeq", sequenceName = "AUTHOR_BOOK_ID_SEQ", allocationSize = 1)
public class AuthorBook {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "authorBookIdSeq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "AUTHOR_ID")
	private Author author;

	@ManyToOne
	@JoinColumn(name = "BOOK_ID")
	private Book book;

}
