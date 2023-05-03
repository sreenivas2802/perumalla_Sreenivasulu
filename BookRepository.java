package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Entity.Book;
import Entity.Borrow;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	public Optional<Book> findById(Long id);

	public List<Book> findByCategory(String category);

	public List<Book> findByAuthor(String author);

	public List<Book> findByName(String name);

	public List<Book> findByPublisher(String publisher);

	public List<Book> findAll();

	public List<Book> findById1(Long id);

	public void delete(Book book);

}
