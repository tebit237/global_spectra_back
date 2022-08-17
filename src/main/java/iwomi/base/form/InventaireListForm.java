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
public class InventaireListForm {
    
    private String codeInv;
    private String labelInv;
    private String typeInv;
    private String queryelm;
    private String tableInv;
    private Integer nbrColoneTable;
    private String nomFic;
    private String cetab;
    private String conect;
    private String columns;
    private Boolean inv;

    public InventaireListForm() {
    }

    public void setQueryelm(String queryelm) {
        this.queryelm = queryelm;
    }

    public void setInv(Boolean inv) {
        this.inv = inv;
    }

    public String getQueryelm() {
        return queryelm;
    }

    public Boolean getInv() {
        return inv;
    }

    public String getCodeInv() {
        return codeInv;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public String getColumns() {
        return columns;
    }

    public void setCodeInv(String codeInv) {
        this.codeInv = codeInv;
    }

    public void setConect(String conect) {
        this.conect = conect;
    }

    public String getConect() {
        return conect;
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

    public String getQueryem() {
        return queryelm;
    }

    public void setQueryem(String cheminInv) {
        this.queryelm = cheminInv;
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

    public InventaireListForm(String codeInv, String labelInv, String typeInv,String cheminInv, String tableInv, Integer nbrColoneTable, String nomFic,String cetab) {
        
        this.codeInv = codeInv;
        this.labelInv = labelInv;
        this.typeInv = typeInv;
        this.queryelm = cheminInv;
        this.tableInv = tableInv;
        this.nbrColoneTable = nbrColoneTable;
        this.nomFic = nomFic;
        this.cetab = cetab;
    }
    
    
    
}
