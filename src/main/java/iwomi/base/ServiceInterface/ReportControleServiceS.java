/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.ServiceInterface;

import iwomi.base.objects.ReportControleIntraSS;
import iwomi.base.objects.ReportControleInterSS;
import iwomi.base.objects.User;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author iwomi team
 */
public interface ReportControleServiceS {

    List<ReportControleInterSS> constructfilterqueryintra1(ReportControleInterSS s);

    List<ReportControleIntraSS> constructfilterqueryinter1(ReportControleIntraSS s);
    
    public abstract void Controle(String date,String etab,String cuser);
    
    public abstract void Controle1(String date,String etab,String cuser,List<String> controle_typ);

	void Controle2(String date, String etab, String cuser, List<String> controle_typ, String ssd);
    
    
}

