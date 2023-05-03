package Rest;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import service.BorrowService;

@RestController
@RequestMapping("/api/book")
public class BorrowController {
	@Autowired
	private BorrowService borrowService;

	@PostMapping("/{bookId}/borrow")
	public void borrowBook(@PathVariable Long bookId, @RequestParam Long userId) throws Exception {
		borrowService.borrowBook(userId, bookId);
	}

	@PostMapping("/{bookId}/return")
	public void returnBook(@PathVariable Long bookId, @RequestParam Long userId, @RequestParam LocalDate returnDate)
			throws Exception {
		borrowService.returnBook(userId, bookId, returnDate);
	}
}
