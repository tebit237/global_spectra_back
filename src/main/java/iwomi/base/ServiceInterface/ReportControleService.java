/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.ServiceInterface;

import iwomi.base.objects.ReportControleInter;
import iwomi.base.objects.ReportControleIntra;
import iwomi.base.objects.User;
import java.math.BigDecimal;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author iwomi team
 */
public interface ReportControleService {

    List<ReportControleIntra> constructfilterqueryintra1(ReportControleIntra s);

    List<ReportControleInter> constructfilterqueryinter1(ReportControleInter s);

    public abstract void Controle(String date, String etab, String cuser);

    public abstract void Controle1(String date, String etab, String cuser, List<String> controle_typ);

    void Controle2(String date, String etab, String cuser, List<String> controle_typ, String ssd);

    String subquerytab(String ty, String date, String f, int colm,String cond);

    BigDecimal getResultComplex3(String arguments, String date,String cod) ;
    
    void autoCorrectInterControl(String date, String etab);
    
    BigDecimal getResultComplex4(String cod, String arguments, String date, Map<String, Map<String, String>> yy, String etab) throws NumberFormatException, DataAccessException ;
}
