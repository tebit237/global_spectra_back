package iwomi.base.objects;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ReportRepId implements Serializable {
	private Long rang;
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date dar;
	private String col;
	private String fichier;

	public ReportRepId() {

	}

	public ReportRepId(Long rang, Date dar, String col, String fichier) {
		this.rang = rang;
		this.dar = dar;
		this.col = col;
		this.fichier = fichier;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ReportRepId reportRepId = (ReportRepId) o;
		return dar.equals(reportRepId.dar) && col.equals(reportRepId.col) && fichier.equals(reportRepId.fichier)
				&& rang.equals(reportRepId.rang);
	}

	@Override
	public int hashCode() {
		return Objects.hash(dar, col ,fichier,rang);
	}
}
