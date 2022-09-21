package iwomi.base.ServiceInterface;

import java.util.List;

import iwomi.base.objects.ReportCalculate;
import iwomi.base.objects.ReportControleIntra;
import iwomi.base.objects.ReportControleQuality;
import iwomi.base.objects.ReportRep;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Map;

import org.json.JSONException;
import org.springframework.ui.Model;

public interface ReportCalculateService {

    List<ReportCalculate> listAll();

    public abstract Map<String, Object> ReportGetCalculate(String json);

    public abstract void CalculData(String json);

    List<Long> dismantlePostCalculate(String calstring);

    List<Long> dismantlePostCalculate1(String calstring);

    List<Long> dismantlePostCalculate2(String calstring);

    List<ReportCalculate> constructfilterquery1(ReportCalculate filter);

    Map<String, String> getGenerationAndSavingParam() throws SQLException, ClassNotFoundException, JSONException;

    String insertPeriodsValiables(String e, Statement r) throws SQLException, ClassNotFoundException, JSONException;

    Statement conac();

    Statement conac1();

    Model get_cells(ReportRep s);

    Map getFormularAndSubFields(String fichi, String post, String col, String date);

    Map<String, Object> getFormularAndSubFieldsControl(Long idOpe, String key, String date);

    List<Object> getAttributeNotExist(Model m);

    Map<String, String> CalculData1(String json, Model m);

    Map<String, String> getType1(String fich) throws SQLException, ClassNotFoundException, JSONException;

    void connec() throws SQLException, ClassNotFoundException, JSONException;

    void connec1() throws SQLException, ClassNotFoundException, JSONException;

    Map<String, String> getType11(String fich) throws SQLException, ClassNotFoundException, JSONException;

    Map<String, String> getType12(String fich) throws SQLException, ClassNotFoundException, JSONException;

    String getmin() throws SQLException, ClassNotFoundException, JSONException;
//		double eval(String formule);

    List<ReportControleIntra> QUERYSET(Statement s) throws SQLException, ClassNotFoundException, JSONException;

    Map<String, Map<String, String>> getType13() throws SQLException, ClassNotFoundException, JSONException;
}
