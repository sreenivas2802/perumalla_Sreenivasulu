package Rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import service.ReportService;

@RestController
@RequestMapping("/api/library/report")
public class LibraryReportController {

	@Autowired
	private ReportService reportService;

	@GetMapping(produces = { "text/csv", "application/pdf",
			"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" })
	public ResponseEntity<ByteArrayResource> generateReport(@RequestParam("format") String format) throws IOException {
		byte[] report = reportService.generateReport(format);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(getContentType(format)));
		headers.setContentDisposition(
				ContentDisposition.builder("attachment").filename("library_report." + getExtension(format)).build());
		headers.setContentLength(report.length);

		return new ResponseEntity<>(new ByteArrayResource(report), headers, HttpStatus.OK);
	}

	private String getContentType(String format) {
		switch (format) {
		case "csv":
			return "text/csv";
		case "pdf":
			return "application/pdf";
		case "xlsx":
			return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		default:
			throw new IllegalArgumentException("Invalid report format: " + format);
		}
	}

	private String getExtension(String format) {
		switch (format) {
		case "csv":
			return "csv";
		case "pdf":
			return "pdf";
		case "xlsx":
			return "xlsx";
		default:
			throw new IllegalArgumentException("Invalid report format: " + format);
		}
	}
	
	
}
