package example.controller;

import example.model.Book;
import example.service.BooksService;

import java.util.List;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

/**
 * GraphQL Controller for Books.
 */
@Controller
public class BooksController {

    private final BooksService booksService;

    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @QueryMapping
    public List<Book> books() {
        return booksService.getBooks();
    }

}

