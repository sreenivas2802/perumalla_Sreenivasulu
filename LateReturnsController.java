package Rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/late-returns")
public class LateReturnsController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/user-wise")
	public List<Map<String, Object>> getLateReturnsUserWise() {
		String query = "SELECT user_id, COUNT(*) as late_returns_count FROM borrow_history WHERE return_date > due_date GROUP BY user_id";
		return jdbcTemplate.queryForList(query);
	}

	@GetMapping("/date-wise")
	public List<Map<String, Object>> getLateReturnsDateWise() {
		String sql = "SELECT DATE_FORMAT(return_date, '%Y-%m-%d') as date, COUNT(*) as count FROM borrow_history WHERE return_date < due_date GROUP BY date";
		return jdbcTemplate.queryForList(sql);
	}

	@GetMapping("/week-wise")
	public List<Map<String, Object>> getLateReturnsWeekWise() {
		String sql = "SELECT CONCAT(YEAR(return_date), '-W', LPAD(WEEK(return_date), 2, '0')) as week, COUNT(*) as count FROM borrow_history WHERE return_date < due_date GROUP BY week";
		return jdbcTemplate.queryForList(sql);
	}

	@GetMapping("/month-wise")
	public List<Map<String, Object>> getLateReturnsMonthWise() {
		String sql = "SELECT DATE_FORMAT(return_date, '%Y-%m') as month, COUNT(*) as count FROM borrow_history WHERE return_date < due_date GROUP BY month";
		return jdbcTemplate.queryForList(sql);
	}
}
