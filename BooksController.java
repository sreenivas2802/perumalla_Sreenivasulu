package Rest;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Entity.Book;
import Entity.Borrow;
import repository.BookRepository;
import repository.BorrowRepository;

@RestController
@RequestMapping("/api")
public class BooksController {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BorrowRepository borrowRepository;

	@PostMapping("/")
	public ResponseEntity<?> addBook(@RequestBody Book book, Principal principal) {

		if (!principal.getName().equals("admin")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		if (bookRepository.findById(book.getId()).isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}

		bookRepository.save(book);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/book")
	public ResponseEntity<Book> updateBook(@RequestBody Book book) {

		// check user is admin or not
		if (!isAdmin()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		// check book exists or not
		Optional<Book> optionalBook = bookRepository.findById(book.getId());
		if (!optionalBook.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		// update book details
		Book existingBook = optionalBook.get();
		existingBook.setName(book.getName());
		existingBook.setAuthor(book.getAuthor());
		existingBook.setCategory(book.getCategory());
		existingBook.setIsbn(book.getIsbn());
		existingBook.setPublisher(book.getPublisher());
		bookRepository.save(existingBook);

		return ResponseEntity.ok(existingBook);
	}

	private boolean isAdmin() {
		// TODO Auto-generated method stub
		return false;
	}

	@GetMapping("/api/book/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable Long id) {
		// check book exists or not
		Optional<Book> bookOptional = bookRepository.findById(id);
		if (!bookOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		// if exists, get book details
		Book book = bookOptional.get();
		return ResponseEntity.ok().body(book);
	}

	@GetMapping("/api/books")
	public List<Book> getAllBooks(@RequestParam(required = false) String category,
			@RequestParam(required = false) String author, @RequestParam(required = false) Long id,
			@RequestParam(required = false) String name, @RequestParam(required = false) String publisher) {
		List<Book> books;
		if (category != null) {
			books = bookRepository.findByCategory(category);
		} else if (author != null) {
			books = bookRepository.findByAuthor(author);
		} else if (id != null) {
			books = bookRepository.findById1(id);
		} else if (name != null) {
			books = bookRepository.findByName(name);
		} else if (publisher != null) {
			books = bookRepository.findByPublisher(publisher);
		} else {
			books = bookRepository.findAll();
		}
		return books;
	}

	@DeleteMapping("/book/{id}")
	public ResponseEntity<?> deleteBook(@PathVariable("id") Long bookId) {
		// Check book exists or not
		Optional<Book> optionalBook = bookRepository.findById(bookId);
		if (!optionalBook.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Book book = optionalBook.get();

		// Check book is borrowed or not
		List<Borrow> borrows = borrowRepository.findByBookId(bookId);
		if (!borrows.isEmpty()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Book is borrowed and cannot be deleted.");
		}

		// Delete book
		bookRepository.delete(book);
		return ResponseEntity.ok().build();
	}

}
