/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.ServiceInterface;

import iwomi.base.form.ClientToSystemForm;
import iwomi.base.form.DataIntegrationForm;

/**
 *
 * @author fabri
 */
public interface ChargerDonneesServiceS {
          
    String extrairesFromClientDatabase(ClientToSystemForm fic);
    String writeInDataBaseSystem(DataIntegrationForm fic);    
    
}
