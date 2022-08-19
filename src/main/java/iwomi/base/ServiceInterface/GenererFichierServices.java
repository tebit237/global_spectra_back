/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.ServiceInterface;

import iwomi.base.form.GenererFichierForm;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author fabri
 */
public interface GenererFichierServices {
    public String genererFichiers(GenererFichierForm fic);
    public String genererFichiersP(GenererFichierForm fic);
    public String genererFichiersLocal(GenererFichierForm fic);

}
