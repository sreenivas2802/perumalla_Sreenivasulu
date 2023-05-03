package Rest;

import java.sql.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/late-fee")
public class LateFeeController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

// late fee year wise
	@GetMapping("/year-wise")
	public List<Map<String, Object>> getLateFeeByYear() {
		String sql = "SELECT YEAR(return_date) AS year, SUM(late_fee) AS late_fee FROM book_lending WHERE return_year IS NOT NULL GROUP BY YEAR(return_year)";

		return jdbcTemplate.queryForList(sql);
	}

	// late fee month wise
	@GetMapping("/month-wise")
	public List<Map<String, Object>> getLateFeeByMonth() {
		String sql = "SELECT YEAR(return_date) AS year, MONTH(return_date) AS month, SUM(late_fee) AS late_fee FROM book_lending WHERE return_date IS NOT NULL GROUP BY YEAR(return_date), MONTH(return_date)";

		return jdbcTemplate.queryForList(sql);
	}

	// late fee week wise
	@GetMapping("/week-wise")
	public List<Map<String, Object>> getLateFeeByWeek() {
		String sql = "SELECT YEAR(return_date) AS year, WEEK(return_date) AS week, SUM(late_fee) AS late_fee FROM book_lending WHERE return_date IS NOT NULL GROUP BY YEAR(return_date), WEEK(return_date)";

		return jdbcTemplate.queryForList(sql);
	}

	// late fee collected date wise
	@GetMapping("/date-wise")
	public List<Map<String, Object>> getLateFeeByDate() {
		String sql = "SELECT return_date, SUM(late_fee) AS late_fee FROM book_lending WHERE return_date IS NOT NULL GROUP BY return_date";

		return jdbcTemplate.queryForList(sql);
	}
}
