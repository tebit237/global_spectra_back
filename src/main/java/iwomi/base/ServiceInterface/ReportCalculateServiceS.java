package iwomi.base.ServiceInterface;
import java.util.List;

import iwomi.base.objects.ReportCalculateS;
import iwomi.base.objects.ReportRepSS;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import org.json.JSONException;
import org.springframework.ui.Model;

public interface ReportCalculateServiceS  {
	List<ReportCalculateS> listAll();
        public abstract Map<String, Object> ReportGetCalculate(String json);
        public abstract void CalculData(String json);
        List<Long> dismantlePostCalculate(String calstring);
        List<Long> dismantlePostCalculate1(String calstring);
        List<Long> dismantlePostCalculate2(String calstring);
    	List<ReportCalculateS> constructfilterquery1(ReportCalculateS filter);
    	Model get_cells(ReportRepSS s);
		Map getFormularAndSubFields(String fichi, String post, String col, String date);
		List<Object> getAttributeNotExist(Model m);
		Map<String,String> CalculData1(String json,Model m);
		Map<String, String> getType1(String fich) throws SQLException, ClassNotFoundException, JSONException;
		void connec() throws SQLException, ClassNotFoundException, JSONException;
		Map<String, String> getType11(String fich) throws SQLException, ClassNotFoundException, JSONException;
		Map<String, String> getType12(String fich) throws SQLException, ClassNotFoundException, JSONException;
		String getmin() throws SQLException, ClassNotFoundException, JSONException;
//		double eval(String formule);

}