/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.ServiceInterface;

import java.util.Map;

/**
 *
 * @author TAGNE
 */
public interface ServiceExportFile {
    
     public Map<String, Object> update_excel_file(Map<String, String> request);
    
}
