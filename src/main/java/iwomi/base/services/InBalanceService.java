/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.services;

import iwomi.base.objects.InBalance;
import iwomi.base.repositories.InBalanceRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author fabri
 */
@Service
public class InBalanceService {
    
    @Autowired
    protected InBalanceRepository inBalanceRepository;
    
    public void save(List<InBalance> inBalance) {
        inBalanceRepository.save(inBalance);
    } 
    
}
