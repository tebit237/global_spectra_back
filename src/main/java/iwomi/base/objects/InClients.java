/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.objects;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author fabri
 */

@Entity(name = "incli")
@Table(name = "incli")
public class InClients {

	@Id
	@GeneratedValue
	private Long id;
	private String age; // agence
	private String cli; // client
	private String nom;
	private String pre;
	private String sig;
	private String rso;
	private String nomc;
	private String typ;
	private String nper;
	private String preper;
	private String nmer;
	private String premer;
	private Date dnai;
	private String lnai;
	private String typpie;
	private String numpie;
	private String ddel;
	private String ldel;
	private String dexp;
	private String ncc;
	private String nrc;
	private String dnrc;
	private String sec;
	private String catn;
	private String naema;

	private String res;
	private String nat;
	private String tel1;
	private String tel2;
	private String tel3;
	private String tel4;
	private String tel5;
	private String tel6;
	private String fax1;
	private String fax2;
	private String fax3;
	private String fax4;
	private String fax5;
	private String fax6;
	private String adr1;
	private String adr2;
	private String adr3;
	private String adr4;
	private String adr5;
	private String adr6;
	private String email1;
	private String email2;
	private String email3;
	private String chl1;
	private String chl2;
	private String chl3;
	private String chl4;
	private String chl5;
	private String chl6;
	private String chl7;
	private String chl8;
	private String chl9;
	private String chl10;
	private String uticre;
	private String utimod;
	private Date dcre;
	private Date dmod;
	private Long dele;
	private Date dar;
	private String cetab;

	public InClients() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCetab() {
		return cetab;
	}

	public void setCetab(String cetab) {
		this.cetab = cetab;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getCli() {
		return cli;
	}

	public void setCli(String cli) {
		this.cli = cli;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPre() {
		return pre;
	}

	public void setPre(String pre) {
		this.pre = pre;
	}

	public String getSig() {
		return sig;
	}

	public void setSig(String sig) {
		this.sig = sig;
	}

	public String getRso() {
		return rso;
	}

	public void setRso(String rso) {
		this.rso = rso;
	}

	public String getNomc() {
		return nomc;
	}

	public void setNomc(String nomc) {
		this.nomc = nomc;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public String getNper() {
		return nper;
	}

	public void setNper(String nper) {
		this.nper = nper;
	}

	public String getPreper() {
		return preper;
	}

	public void setPreper(String preper) {
		this.preper = preper;
	}

	public String getNmer() {
		return nmer;
	}

	public void setNmer(String nmer) {
		this.nmer = nmer;
	}

	public String getPremer() {
		return premer;
	}

	public void setPremer(String premer) {
		this.premer = premer;
	}

	public String getLnai() {
		return lnai;
	}

	public void setLnai(String lnai) {
		this.lnai = lnai;
	}

	public String getTyppie() {
		return typpie;
	}

	public void setTyppie(String typpie) {
		this.typpie = typpie;
	}

	public String getNumpie() {
		return numpie;
	}

	public void setNumpie(String numpie) {
		this.numpie = numpie;
	}

	public String getDdel() {
		return ddel;
	}

	public void setDdel(String ddel) {
		this.ddel = ddel;
	}

	public String getLdel() {
		return ldel;
	}

	public void setLdel(String ldel) {
		this.ldel = ldel;
	}

	public String getDexp() {
		return dexp;
	}

	public void setDexp(String dexp) {
		this.dexp = dexp;
	}

	public String getNcc() {
		return ncc;
	}

	public void setNcc(String ncc) {
		this.ncc = ncc;
	}

	public String getNrc() {
		return nrc;
	}

	public void setNrc(String nrc) {
		this.nrc = nrc;
	}

	public String getDnrc() {
		return dnrc;
	}

	public void setDnrc(String dnrc) {
		this.dnrc = dnrc;
	}

	public String getSec() {
		return sec;
	}

	public void setSec(String sec) {
		this.sec = sec;
	}

	public String getNaema() {
		return naema;
	}

	public void setNaema(String naema) {
		this.naema = naema;
	}

	public Long getDele() {
		return dele;
	}

	public void setDele(Long dele) {
		this.dele = dele;
	}

	public Date getDar() {
		return dar;
	}

	public void setDar(Date dar) {
		this.dar = dar;
	}

	public String getCatn() {
		return catn;
	}

	public void setCatn(String catn) {
		this.catn = catn;
	}

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public String getNat() {
		return nat;
	}

	public void setNat(String nat) {
		this.nat = nat;
	}

	public String getTel1() {
		return tel1;
	}

	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}

	public String getTel2() {
		return tel2;
	}

	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}

	public String getTel3() {
		return tel3;
	}

	public void setTel3(String tel3) {
		this.tel3 = tel3;
	}

	public String getTel4() {
		return tel4;
	}

	public void setTel4(String tel4) {
		this.tel4 = tel4;
	}

	public String getTel5() {
		return tel5;
	}

	public void setTel5(String tel5) {
		this.tel5 = tel5;
	}

	public String getTel6() {
		return tel6;
	}

	public void setTel6(String tel6) {
		this.tel6 = tel6;
	}

	public String getFax1() {
		return fax1;
	}

	public void setFax1(String fax1) {
		this.fax1 = fax1;
	}

	public String getFax2() {
		return fax2;
	}

	public void setFax2(String fax2) {
		this.fax2 = fax2;
	}

	public String getFax3() {
		return fax3;
	}

	public void setFax3(String fax3) {
		this.fax3 = fax3;
	}

	public String getFax4() {
		return fax4;
	}

	public void setFax4(String fax4) {
		this.fax4 = fax4;
	}

	public String getFax5() {
		return fax5;
	}

	public void setFax5(String fax5) {
		this.fax5 = fax5;
	}

	public String getFax6() {
		return fax6;
	}

	public void setFax6(String fax6) {
		this.fax6 = fax6;
	}

	public String getAdr1() {
		return adr1;
	}

	public void setAdr1(String adr1) {
		this.adr1 = adr1;
	}

	public String getAdr2() {
		return adr2;
	}

	public void setAdr2(String adr2) {
		this.adr2 = adr2;
	}

	public String getAdr3() {
		return adr3;
	}

	public void setAdr3(String adr3) {
		this.adr3 = adr3;
	}

	public String getAdr4() {
		return adr4;
	}

	public void setAdr4(String adr4) {
		this.adr4 = adr4;
	}

	public String getAdr5() {
		return adr5;
	}

	public void setAdr5(String adr5) {
		this.adr5 = adr5;
	}

	public String getAdr6() {
		return adr6;
	}

	public void setAdr6(String adr6) {
		this.adr6 = adr6;
	}

	public String getEmail1() {
		return email1;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getEmail3() {
		return email3;
	}

	public void setEmail3(String email3) {
		this.email3 = email3;
	}

	public String getChl1() {
		return chl1;
	}

	public void setChl1(String chl1) {
		this.chl1 = chl1;
	}

	public String getChl2() {
		return chl2;
	}

	public void setChl2(String chl2) {
		this.chl2 = chl2;
	}

	public String getChl3() {
		return chl3;
	}

	public void setChl3(String chl3) {
		this.chl3 = chl3;
	}

	public String getChl4() {
		return chl4;
	}

	public void setChl4(String chl4) {
		this.chl4 = chl4;
	}

	public String getChl5() {
		return chl5;
	}

	public void setChl5(String chl5) {
		this.chl5 = chl5;
	}

	public String getChl6() {
		return chl6;
	}

	public void setChl6(String chl6) {
		this.chl6 = chl6;
	}

	public String getChl7() {
		return chl7;
	}

	public void setChl7(String chl7) {
		this.chl7 = chl7;
	}

	public String getChl8() {
		return chl8;
	}

	public void setChl8(String chl8) {
		this.chl8 = chl8;
	}

	public String getChl9() {
		return chl9;
	}

	public void setChl9(String chl9) {
		this.chl9 = chl9;
	}

	public String getChl10() {
		return chl10;
	}

	public void setChl10(String chl10) {
		this.chl10 = chl10;
	}

	public String getUticre() {
		return uticre;
	}

	public void setUticre(String uticre) {
		this.uticre = uticre;
	}

	public String getUtimod() {
		return utimod;
	}

	public void setUtimod(String utimod) {
		this.utimod = utimod;
	}

	public Date getDcre() {
		return dcre;
	}

	public void setDcre(Date dcre) {
		this.dcre = dcre;
	}

	public Date getDmod() {
		return dmod;
	}

	public void setDmod(Date dmod) {
		this.dmod = dmod;
	}
	
	public InClients(String age, String cli, String nom, String pre, String sig, String rso, String nomc, String typ,
			String nper, String preper, String nmer, String premer, Date dnai, String lnai, String typpie,
			String numpie, String ddel, String ldel, String dexp, String ncc, String nrc, String dnrc, String sec,
			String catn, String naema, String res, String nat, String tel1, String tel2, String tel3, String tel4,
			String tel5, String tel6, String fax1, String fax2, String fax3, String fax4, String fax5, String fax6,
			String adr1, String adr2, String adr3, String adr4, String adr5, String adr6, String email1, String email2,
			String email3, String chl1, String chl2, String chl3, String chl4, String chl5, String chl6, String chl7,
			String chl8, String chl9, String chl10, Date dcre, Date dmod, String uticre, String utimod,
			String cetab, Date dar) {
		super();
		this.cetab = cetab;
		this.age = age;
		this.cli = cli;
		this.nom = nom;
		this.pre = pre;
		this.sig = sig;
		this.rso = rso;
		this.nomc = nomc;
		this.typ = typ;
		this.nper = nper;
		this.preper = preper;
		this.nmer = nmer;
		this.premer = premer;
		this.dnai = dnai;
		this.lnai = lnai;
		this.typpie = typpie;
		this.numpie = numpie;
		this.ddel = ddel;
		this.ldel = ldel;
		this.dexp = dexp;
		this.ncc = ncc;
		this.nrc = nrc;
		this.dnrc = dnrc;
		this.sec = sec;
		this.catn = catn;
		this.naema = naema;
		this.res = res;
		this.nat = nat;
		this.tel1 = tel1;
		this.tel2 = tel2;
		this.tel3 = tel3;
		this.tel4 = tel4;
		this.tel5 = tel5;
		this.tel6 = tel6;
		this.fax1 = fax1;
		this.fax2 = fax2;
		this.fax3 = fax3;
		this.fax4 = fax4;
		this.fax5 = fax5;
		this.fax6 = fax6;
		this.adr1 = adr1;
		this.adr2 = adr2;
		this.adr3 = adr3;
		this.adr4 = adr4;
		this.adr5 = adr5;
		this.adr6 = adr6;
		this.email1 = email1;
		this.email2 = email2;
		this.email3 = email3;
		this.chl1 = chl1;
		this.chl2 = chl2;
		this.chl3 = chl3;
		this.chl4 = chl4;
		this.chl5 = chl5;
		this.chl6 = chl6;
		this.chl7 = chl7;
		this.chl8 = chl8;
		this.chl9 = chl9;
		this.chl10 = chl10;
		this.uticre = uticre;
		this.utimod = utimod;
		this.dele = dele;
		this.dar = dar;
		this.dcre = dcre;
		this.dmod = dmod;
	}

	public Date getDnai() {
		return dnai;
	}

	public void setDnai(Date dnai) {
		this.dnai = dnai;
	}

}
