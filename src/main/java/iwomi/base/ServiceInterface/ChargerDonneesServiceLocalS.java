/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.ServiceInterface;

import iwomi.base.form.ClientToSystemFormS;
import iwomi.base.form.DataIntegrationFormS;
import java.util.Map;

/**
 *
 * @author fabri
 */
public interface ChargerDonneesServiceLocalS {
    
    String extrairesFromClientDatabase(ClientToSystemFormS fic);
    String writeInDataBaseSystem(DataIntegrationFormS fic);  
     Map<String, String>  getGenerationAndSavingParam();
    
}
