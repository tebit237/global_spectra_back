package iwomi.base.ServiceInterface;

import java.util.List;

import iwomi.base.form.ChampFichier;
import iwomi.base.objects.ReportFile;

public interface ReportFileServiceS {
	 List<ReportFile> listAll();
	 List<ChampFichier> getChamFil(String filterCell);
}
