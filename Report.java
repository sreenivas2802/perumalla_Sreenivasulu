package Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reports")
public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "report_type")
	private ReportType reportType;

	@Column(name = "report_format")
	private ReportFormat reportFormat;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public ReportFormat getReportFormat() {
		return reportFormat;
	}

	public void setReportFormat(ReportFormat reportFormat) {
		this.reportFormat = reportFormat;
	}

}

enum ReportType {
	BOOKS_BORROWED_PER_MONTH, LATE_RETURNS_USER_WISE, LATE_RETURNS_DATE_WISE, AMOUNT_OF_LATE_FEE_COLLECTED
}

enum ReportFormat {
	CSV, PDF, XLSX
}
