/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.ServiceInterface;

import iwomi.base.form.GenererFichierFormS;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author fabri
 */
public interface GenererFichierServicesS {
    public String genererFichiersS(GenererFichierFormS fic);
    public String genererFichiersP(GenererFichierFormS fic);
    public String genererFichiersLocal(GenererFichierFormS fic);

}
