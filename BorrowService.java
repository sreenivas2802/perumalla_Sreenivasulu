package service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Entity.Book;
import Entity.Borrow;
import Entity.User;
import repository.BookRepository;
import repository.BorrowRepository;

@Service
public class BorrowService {
    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;
    
    @Autowired
	private BookRepository bookRepository;


    public void borrowBook(Long userId, Long bookId) throws Exception {
        User user = userService.getUserById(userId);
        Book book = bookService.getBookById(bookId);

        if (!book.isAvailable()) {
            throw new Exception("Book is not available for borrowing");
        }

        Borrow borrow = new Borrow();
        borrow.setUser(user);
        borrow.setBook(book);
        borrow.setBorrowDate(LocalDate.now());
        borrow.setDueDate(LocalDate.now().plusDays(user.getBorrowingDays()));

        book.setAvailable(false);
        bookService.saveBook(book);

        borrowRepository.save(borrow);
    }

    public void returnBook(Long userId, Long bookId, LocalDate returnDate) throws Exception {
        User user = userService.getUserById(userId);
        Book book = bookService.getBookById(bookId);
        Borrow borrow = borrowRepository.findByUserIdAndBookId(userId, bookId);

        if (borrow == null) {
            throw new Exception("Borrow not found");
        }

        borrow.setReturnDate(returnDate);
        book.setAvailable(true);

        if (returnDate.isAfter(borrow.getDueDate())) {
            int daysLate = (int) ChronoUnit.DAYS.between(borrow.getDueDate(), returnDate);
            user.setBorrowingDaysBlocked(user.getBorrowingDaysBlocked() + daysLate);
        }

        userService.saveUser(user);
        bookService.saveBook(book);
        borrowRepository.save(borrow);
    }
}

