/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.services;

import iwomi.base.objects.Nomenclature;
import iwomi.base.objects.Pwd;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.aspectj.asm.internal.ProgramElement.trim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
//import static org.apache.commons.lang3.StringUtils.trim;
//import org.json.JSONArray;
//import org.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 *
 * @author yvo
 */
@Component
@Service
public class ServiceReporting_v1 extends GlobalService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Map Report_dropdown(Map<String, String> load) throws Exception {
        Map obj = new HashMap();
        Connection sr = connectDB();
        Statement r = sr.createStatement();
        ResultSet y = r.executeQuery("select * from sanm where tabcd = '8022' and dele = 0 and acscd = '" + load.get("acscd") + "'");
        y.next();
        String query = y.getString("lib2");
        String connect = y.getString("lib5");
        y = r.executeQuery("select * from sanm where tabcd = '8024' and dele = 0");
        String rr = "";
        y = r.executeQuery("select login,pass,lib1,lib2 from pwd where acscd = '" + connect.trim() + "'");
        y.next();
        byte[] decoder = Base64.getDecoder().decode(y.getString("pass"));
        String v = new String(decoder);
        Class.forName(y.getString("lib2"));
        Statement qConnx = DriverManager.getConnection(y.getString("lib1"), y.getString("login"), v).createStatement();
        ResultSet j = qConnx.executeQuery(query);
        List<List> data = convert2(j);
        obj.put("data", data);
        return obj;
    }

    public Map Report_info_export(Map<String, String> load) throws Exception {
        Map obj = new HashMap();
        Connection sr = connectDB();
        Statement r = sr.createStatement();
        ResultSet y = r.executeQuery("select * from sanm where tabcd = '8022' and dele = 0 and acscd = '" + load.get("acscd") + "'");
        y.next();
        String query = y.getString("lib2");
        String connect = y.getString("lib5");
        String selectElm = "";
        for (String g : y.getString("lib3").split("\\|")) {
            selectElm += (selectElm == "" ? "" : ",") + g.split(":")[0];
        }
        y = r.executeQuery("select * from sanm where tabcd = '8024' and dele = 0");
        String where = "WHERE 1=1 ";
        while (y.next()) {
            if (load.containsKey(y.getString("lib3").trim())) {
                if (y.getString("lib2").trim().equalsIgnoreCase("text")) {
                    where += " AND " + y.getString("lib3").trim() + " like  '%" + load.get(y.getString("lib3").trim()).trim() + "%'";
                    query = query.replaceAll(":" + y.getString("lib3").trim(), "'%" + load.get(y.getString("lib3").trim()).trim() + "%'");
                } else if (y.getString("lib2").trim().equalsIgnoreCase("date")) {
                    if (load.get(y.getString("lib3").trim()) != "") {
                        where += " AND " + y.getString("lib3").trim() + " = to_date('" + load.get(y.getString("lib3").trim()).trim() + "','yyyy-mm-dd')";
                    }
                    query = query.replaceAll(":" + y.getString("lib3").trim(), "to_date('" + load.get(y.getString("lib3").trim()).trim() + "','yyyy-mm-dd')");
                } else if (y.getString("lib2").trim().equalsIgnoreCase("date_between")) {
                    String[] i = load.get(y.getString("lib3").trim()).split("\\-");
                    where += " AND " + y.getString("lib3").trim() + " between to_date('" + i[0].trim() + "','mm/dd/yyyy') and to_date('" + i[1].trim() + "','mm/dd/yyyy')";
                    query = query.replaceAll(":" + y.getString("lib3").trim(), " to_date('" + i[0].trim() + "','mm/dd/yyyy') and to_date('" + i[1].trim() + "','mm/dd/yyyy')");
                } else if (y.getString("lib2").trim().equalsIgnoreCase("select")) {
                    if (load.get(y.getString("lib3").trim()) != "") {
                        where += " AND " + y.getString("lib3").trim() + " = '" + load.get(y.getString("lib3").trim()).trim() + "'";
                    }
                    query = query.replaceAll(":" + y.getString("lib3").trim(), "'" + load.get(y.getString("lib3").trim()).trim() + "'");
                }
            }
        }
        String rr = "";
        if (load.containsKey("start") && load.containsKey("length")) {
            rr = " OFFSET " + load.get("start") + " ROWS FETCH NEXT " + load.get("length") + "  ROWS ONLY";
        }
        y = r.executeQuery("select login,pass,lib1,lib2 from pwd where acscd = '" + connect.trim() + "'");
        y.next();
        byte[] decoder = Base64.getDecoder().decode(y.getString("pass"));
        String v = new String(decoder);
        Class.forName(y.getString("lib2"));
        Statement qConnx = DriverManager.getConnection(y.getString("lib1"), y.getString("login"), v).createStatement();
        ResultSet j = qConnx.executeQuery("select count(*) o from (" + query + ")" + where);
        j.next();
        obj.put("count_all", j.getString(1));
        System.out.println("select " + selectElm + " from (" + query + ")" + where + " " + rr);
        j = qConnx.executeQuery("select " + selectElm + " from (" + query + ")" + where + " " + rr);
        List<List> data = convert2(j);
        obj.put("data", data);
        obj.put("count_dat", data.size());
        return obj;
    }

    public Map Report_export(Map<String, String> load) throws Exception {
        Map obj = new HashMap();
        Connection sr = connectDB();
        Statement r = sr.createStatement();
        ResultSet y = r.executeQuery("select * from sanm where tabcd = '8022' and dele = 0 and acscd = '" + load.get("acscd") + "'");
        y.next();
        String query = y.getString("lib2");
        String connect = y.getString("lib5");
        String selectElm = "";
        List<String> gg = new ArrayList<>();
        for (String g : y.getString("lib3").split("\\|")) {
            gg.add(g.split(":").length > 1 ? g.split(":")[1] : g.split(":")[0]);
            selectElm += (selectElm == "" ? "" : ",") + g.split(":")[0];
        }
        y = r.executeQuery("select * from sanm where tabcd = '8024' and dele = 0");
        String where = "WHERE 1=1 ";
        while (y.next()) {
            if (load.containsKey(y.getString("lib3").trim())) {
                if (y.getString("lib2").trim().equalsIgnoreCase("text")) {
                    where += " AND " + y.getString("lib3").trim() + " like  '%" + load.get(y.getString("lib3").trim()).trim() + "%'";
                    query = query.replaceAll(":" + y.getString("lib3").trim(), "'%" + load.get(y.getString("lib3").trim()).trim() + "%'");
                } else if (y.getString("lib2").trim().equalsIgnoreCase("date")) {
                    if (load.get(y.getString("lib3").trim()) != "") {
                        where += " AND " + y.getString("lib3").trim() + " = to_date('" + load.get(y.getString("lib3").trim()).trim() + "','yyyy-mm-dd')";
                    }
                    query = query.replaceAll(":" + y.getString("lib3").trim(), "to_date('" + load.get(y.getString("lib3").trim()).trim() + "','yyyy-mm-dd')");
                } else if (y.getString("lib2").trim().equalsIgnoreCase("date_between")) {
                    String[] i = load.get(y.getString("lib3").trim()).split("\\-");
                    where += " AND " + y.getString("lib3").trim() + " between to_date('" + i[0].trim() + "','mm/dd/yyyy') and to_date('" + i[1].trim() + "','mm/dd/yyyy')";
                    query = query.replaceAll(":" + y.getString("lib3").trim(), " to_date('" + i[0].trim() + "','mm/dd/yyyy') and to_date('" + i[1].trim() + "','mm/dd/yyyy')");
                } else if (y.getString("lib2").trim().equalsIgnoreCase("select")) {
                    if (load.get(y.getString("lib3").trim()) != "") {
                        where += " AND " + y.getString("lib3").trim() + " = '" + load.get(y.getString("lib3").trim()).trim() + "'";
                    }
                    query = query.replaceAll(":" + y.getString("lib3").trim(), "'" + load.get(y.getString("lib3").trim()).trim() + "'");
                }
            }
        }
        System.out.println("query : " + query);
        String rr = "";
        y = r.executeQuery("select login,pass,lib1,lib2 from pwd where acscd = '" + connect.trim() + "'");
        y.next();
        byte[] decoder = Base64.getDecoder().decode(y.getString("pass"));
        String v = new String(decoder);
        Class.forName(y.getString("lib2"));
        Statement qConnx = DriverManager.getConnection(y.getString("lib1"), y.getString("login"), v).createStatement();
        ResultSet j = qConnx.executeQuery("select " + selectElm + " from (" + query + ")" + where + " " + rr);
        List<List> data = convert2(j);
        obj.put("data", data);
        obj.put("head", gg);
        return obj;
    }

    public static List<List> convert2(ResultSet resultSet) throws Exception {
        System.out.println("Start Convertion for display");

        List<List> jsonArray = new ArrayList();
        while (resultSet.next()) {
            int columns = resultSet.getMetaData().getColumnCount();
            List obj = new ArrayList();
            for (int i = 0; i < columns; i++) {
                switch (resultSet.getMetaData().getColumnType(i + 1)) {
                    case Types.VARCHAR:
                        obj.add((String) resultSet.getObject(i + 1));
                        break;
                    case Types.FLOAT:
                        obj.add((float) resultSet.getObject(i + 1));
                        break;
                    case Types.TIMESTAMP:
                        obj.add(resultSet.getObject(i + 1).toString());
                        break;
                    default:
                        obj.add(resultSet.getObject(i + 1).toString());
                }
            }

            jsonArray.add(obj);
        }
        System.out.println("End Conversion");
        return jsonArray;
    }

}
