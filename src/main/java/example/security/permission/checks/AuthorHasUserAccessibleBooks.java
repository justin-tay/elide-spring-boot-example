package example.security.permission.checks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.yahoo.elide.annotation.SecurityCheck;
import com.yahoo.elide.core.Path;
import com.yahoo.elide.core.filter.Operator;
import com.yahoo.elide.core.filter.expression.FilterExpression;
import com.yahoo.elide.core.filter.predicates.FilterPredicate;
import com.yahoo.elide.core.security.RequestScope;
import com.yahoo.elide.core.security.checks.FilterExpressionCheck;
import com.yahoo.elide.core.type.Type;

import example.models.Author;
import example.models.AuthorBook;
import example.models.Book;

@SecurityCheck("author has user accessible books")
public class AuthorHasUserAccessibleBooks extends FilterExpressionCheck<Author> {

	@Override
	public FilterExpression getFilterExpression(Type<?> entityClass, RequestScope requestScope) {
		Path.PathElement author = new Path.PathElement(Author.class, AuthorBook.class, "authorBooks");
		Path.PathElement book = new Path.PathElement(AuthorBook.class, Book.class, "book");
		Path.PathElement id = new Path.PathElement(Book.class, Long.class, "id");
		
		List<Path.PathElement> pathList = new ArrayList<>();
		pathList.add(author);
		pathList.add(book);
		pathList.add(id);
		Path paths = new Path(pathList);
		
		// For simplicity this hard codes the book id to 1 and 2 instead of retrieving from the principal
		return new FilterPredicate(paths, Operator.IN, Arrays.asList(1,2));
	}
}
