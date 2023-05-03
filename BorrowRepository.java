package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Entity.Book;
import Entity.Borrow;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {
	List<Borrow> findByUserId(Long userId);

	List<Borrow> findByBookId(Long bookId);

	Borrow findByUserIdAndBookId(Long userId, Long bookId);

	
}
