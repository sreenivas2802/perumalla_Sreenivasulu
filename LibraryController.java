package Rest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/library")
public class LibraryController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/books-borrowed-per-month")
	public List<BookBorrowedPerMonth> getBooksBorrowedPerMonth() {
		List<BookBorrowedPerMonth> booksBorrowedPerMonth = new ArrayList<>();

		String sqlQuery = "SELECT COUNT(*) AS books_borrowed, MONTH(borrowed_date) AS month " + "FROM book_borrowed "
				+ "WHERE YEAR(borrowed_date) = YEAR(CURRENT_DATE()) " + "GROUP BY MONTH(borrowed_date)";

		jdbcTemplate
				.query(sqlQuery,
						(rs, rowNum) -> new BookBorrowedPerMonth(rs.getInt("books_borrowed"), rs.getInt("month")))
				.forEach(booksBorrowedPerMonth::add);

		return booksBorrowedPerMonth;
	}

	@GetMapping("/books-borrowed-per-year")
	public List<BookBorrowedPerYear> getBooksBorrowedPerYear(@RequestParam String startDate,
			@RequestParam String endDate) {
		List<BookBorrowedPerYear> booksBorrowedPerYear = new ArrayList<>();

		String sqlQuery = "SELECT COUNT(*) AS books_borrowed, YEAR(borrowed_date) AS year " + "FROM book_borrowed "
				+ "WHERE borrowed_date BETWEEN ? AND ? " + "GROUP BY YEAR(borrowed_date)";

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date start = null;
		Date end = null;
		try {
			start = dateFormat.parse(startDate);
			end = dateFormat.parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		jdbcTemplate
				.query(sqlQuery, new Object[] { start, end },
						(rs, rowNum) -> new BookBorrowedPerYear(rs.getInt("books_borrowed"), rs.getInt("year")))
				.forEach(booksBorrowedPerYear::add);

		return booksBorrowedPerYear;
	}

	@GetMapping("/books-borrowed-for-month")
	public int getBooksBorrowedForMonth(@RequestParam int year, @RequestParam int month) {

		String sqlQuery = "SELECT COUNT(*) AS books_borrowed " + "FROM book_borrowed "
				+ "WHERE YEAR(borrowed_date) = ? AND MONTH(borrowed_date) = ?";

		return jdbcTemplate.queryForObject(sqlQuery, new Object[] { year, month }, Integer.class);
	}
}
