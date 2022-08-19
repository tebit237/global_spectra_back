/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.form;

/**
 *
 * @author fabri
 */
public class ReportResultFomS {
    
    
    
     String success;  // le statut de la transaction  1 succes 2 failled 
     String codeFic;  // code de traitement ca peut etre code fichier ou code inventaire  
     String statutFic;  // statut fichier 1 succes 2 fail 3 pending 
     String valueFic;   //  un double entre 1 et 100. 
     String statutOp;   //    statut de tout le traitement  1 succès 2 echec 3 pending
     String valueOp;   //      une valeure entre 0 et 100 
     String timeSpend;   //     time left

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getCodeFic() {
        return codeFic;
    }

    public void setCodeFic(String codeFic) {
        this.codeFic = codeFic;
    }

    public String getStatutFic() {
        return statutFic;
    }

    public void setStatutFic(String statutFic) {
        this.statutFic = statutFic;
    }

    public String getValueFic() {
        return valueFic;
    }

    public void setValueFic(String valueFic) {
        this.valueFic = valueFic;
    }

    public String getStatutOp() {
        return statutOp;
    }

    public void setStatutOp(String statutOp) {
        this.statutOp = statutOp;
    }

    public String getValueOp() {
        return valueOp;
    }

    public void setValueOp(String valueOp) {
        this.valueOp = valueOp;
    }     

    public ReportResultFomS(String success, String codeFic, String statutFic, String valueFic, String statutOp, String valueOp) {
        this.success = success;
        this.codeFic = codeFic;
        this.statutFic = statutFic;
        this.valueFic = valueFic;
        this.statutOp = statutOp;
        this.valueOp = valueOp;
    }
     
    
    
     
     public ReportResultFomS(){
      
      }

	public ReportResultFomS(String success, String codeFic, String statutFic, String valueFic, String statutOp,
			String valueOp, String timeSpend) {
		super();
		this.success = success;
		this.codeFic = codeFic;
		this.statutFic = statutFic;
		this.valueFic = valueFic;
		this.statutOp = statutOp;
		this.valueOp = valueOp;
		this.timeSpend = timeSpend;
	}

	public String getTimeSpend() {
		return timeSpend;
	}

	public void setTimeSpend(String timeSpend) {
		this.timeSpend = timeSpend;
	}

	

     
     
}
