/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author TAGNE
 */
@Entity
@Table(name ="pwd")
public class Pwd {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
        private String login;//login user
 	private String pass;//mot de passe
	private String descrip;//description
	private String etab;//code etablissement
	private String acscd;//code dacces
        private String lib1;
	private String lib2;
	private String crtd;
	private String mdfi;
        private String dele;

    public Pwd() {
    }

    public Pwd(Long id, String login, String pass, String descrip, String etab, String acscd, String lib1, String lib2, String crtd, String mdfi, String dele) {
        this.id = id;
        this.login = login;
        this.pass = pass;
        this.descrip = descrip;
        this.etab = etab;
        this.acscd = acscd;
        this.lib1 = lib1;
        this.lib2 = lib2;
        this.crtd = crtd;
        this.mdfi = mdfi;
        this.dele = dele;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getEtab() {
        return etab;
    }

    public void setEtab(String etab) {
        this.etab = etab;
    }

    public String getAcscd() {
        return acscd;
    }

    public void setAcscd(String acscd) {
        this.acscd = acscd;
    }

    public String getLib1() {
        return lib1;
    }

    public void setLib1(String lib1) {
        this.lib1 = lib1;
    }

    public String getLib2() {
        return lib2;
    }

    public void setLib2(String lib2) {
        this.lib2 = lib2;
    }

    public String getCrtd() {
        return crtd;
    }

    public void setCrtd(String crtd) {
        this.crtd = crtd;
    }

    public String getMdfi() {
        return mdfi;
    }

    public void setMdfi(String mdfi) {
        this.mdfi = mdfi;
    }

    public String getDele() {
        return dele;
    }

    public void setDele(String dele) {
        this.dele = dele;
    }
        
}
