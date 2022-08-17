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
public class InventaireListFormS {
    
    private String codeInv;
    private String labelInv;
    private String typeInv;
    private String cheminInv;
    private String tableInv;
    private Integer nbrColoneTable;
    private String nomFic;
    private String cetab;


    public String getCodeInv() {
        return codeInv;
    }

    public void setCodeInv(String codeInv) {
        this.codeInv = codeInv;
    }

    public String getLabelInv() {
        return labelInv;
    }

    public void setLabelInv(String labelInv) {
        this.labelInv = labelInv;
    }

    public String getTypeInv() {
        return typeInv;
    }

    public void setTypeInv(String typeInv) {
        this.typeInv = typeInv;
    }

    public String getCheminInv() {
        return cheminInv;
    }

    public void setCheminInv(String cheminInv) {
        this.cheminInv = cheminInv;
    }

    public String getTableInv() {
        return tableInv;
    }

    public void setTableInv(String tableInv) {
        this.tableInv = tableInv;
    }

    public Integer getNbrColoneTable() {
        return nbrColoneTable;
    }

    public void setNbrColoneTable(Integer nbrColoneTable) {
        this.nbrColoneTable = nbrColoneTable;
    }

    public String getNomFic() {
        return nomFic;
    }

    public void setNomFic(String nomFic) {
        this.nomFic = nomFic;
    }
    
    public String getCetab() {
        return cetab;
    }
    public void setCetab(String cetab) {
        this.cetab = cetab;
    }

    public InventaireListFormS(String codeInv, String labelInv, String typeInv,String cheminInv, String tableInv, Integer nbrColoneTable, String nomFic,String cetab) {
        
        this.codeInv = codeInv;
        this.labelInv = labelInv;
        this.typeInv = typeInv;
        this.cheminInv = cheminInv;
        this.tableInv = tableInv;
        this.nbrColoneTable = nbrColoneTable;
        this.nomFic = nomFic;
        this.cetab = cetab;
    }
    
    
    
}
