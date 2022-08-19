/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.services;

import iwomi.base.objects.Nomenclature;
import iwomi.base.repositories.NomenclatureRepository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author tebit roger
 */
public class GlobalService {

    @Autowired
    NomenclatureRepository nomenclatureRepository;

    public Connection connectDB() throws SQLException, ClassNotFoundException, JSONException {
        Class.forName("com.mysql.jdbc.Driver");
        try {
            Nomenclature s = nomenclatureRepository.findBytabcdAndDeleAndAcscd("4002", 0, "0001");
            return DriverManager.getConnection(s.getLib1(), s.getLib2(), s.getLib3());
        } catch (SQLException ex) {
            System.out.println("the error during connection ERRO1" + ex.getMessage());
        }
        return null;
    }

    public Map<String, String> r(String f) {
        try {
            Connection r = connectDB();
            return getType_v1(r.createStatement(), f);
        } catch (Exception e) {
            System.out.println("Error, did not fine file info");
            return null;
        }
    }

    public Map<String, Map<String, String>> getType_v1(Statement s) throws SQLException, ClassNotFoundException, JSONException {
        String select = "";
        Map<String, Map<String, String>> er = new HashMap<>();
        select = "SELECT taux1,lib2,taux4,mnt1,mnt2,mnt3 FROM sanm  WHERE tabcd='3009' AND dele='0'";
        ResultSet result = s.executeQuery(select);
        String ppp = "";
        int ee = 0;
        while (result.next()) {
            ee++;
            Map<String, String> elm = new HashMap<String, String>();
            switch (result.getString("taux1")) {
                case "0":
                    ppp = "calculate";
                    break;
                case "1":
                    ppp = "duplicate";
                    break;
                case "2":
                    ppp = "sql";
                    break;
                case "3":
                    ppp = "duplicateNoPost";
                    break;
                default:
                    ppp = "not define";
            }
            elm.put("result", ppp);
            elm.put("ncol", result.getString("taux4"));
            elm.put("auto_correct", result.getString("mnt3"));
            elm.put("typ", ppp);
            elm.put("postlike_query", result.getString("mnt2"));
            er.put(result.getString("lib2"), elm);
        }
        result.close();
        if (ee == 0) {
            System.out.println("noting found for this request");
        }
        return er;
    }

    public Map<String, String> getType_v1(Statement s, String f) throws SQLException, ClassNotFoundException, JSONException {
        String select = "";
        Map<String, Map<String, String>> er = new HashMap<>();
        select = "SELECT taux1,lib2,taux4,mnt1,mnt2 FROM sanm  WHERE tabcd='3009' AND dele='0' and lib2 = '" + f + "'";
        System.out.println(select);
        ResultSet result = s.executeQuery(select);
        String ppp = "";
        int ee = 0;
        Map<String, String> elm = new HashMap<String, String>();
        while (result.next()) {
            ee++;
            switch (result.getString("taux1")) {
                case "0":
                    ppp = "calculate";
                    break;
                case "1":
                    ppp = "duplicate";
                    break;
                case "2":
                    ppp = "sql";
                    break;
                case "3":
                    ppp = "duplicateNoPost";
                    break;
                default:
                    ppp = "not define";
            }
            elm.put("result", ppp);
        }
        result.close();
        System.out.println("seen " + elm);
        return elm;
    }

}
