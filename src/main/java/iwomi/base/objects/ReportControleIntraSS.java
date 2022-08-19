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
 * @author User
 */
@Entity
@Table(name = "srpctrl")
public class ReportControleIntraSS {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String etab;
	private String fich;
	private String cg;
	private String typ;
	private String sign;
	private String val;
	private String cd;
	private String crdt;
	private String mdfi;
	private String per;
	private String dele;

	public ReportControleIntraSS() {
	}

	public ReportControleIntraSS(Long id, String etab, String fich, String cg, String typ, String sign, String val,
			String cd, String crdt, String mdfi, String per, String dele) {
		super();
		this.id = id;
		this.etab = etab;
		this.fich = fich;
		this.cg = cg;
		this.typ = typ;
		this.sign = sign;
		this.val = val;
		this.cd = cd;
		this.crdt = crdt;
		this.mdfi = mdfi;
		this.per = per;
		this.dele = dele;
	}

	public Boolean setByColumn1(ReportControleIntraSS d) {
		if (d.cd != null) {
			this.cd = d.cd;
		}
		if (d.cg != null) {
			this.cg = d.cg;
		}
		if (d.sign != null) {
			this.sign = d.sign;
		}
		if (d.val != null) {
			this.val = d.val;
		}
		if (d.per != null) {
			this.per = d.per;
		}
		return true;
	}

	public Boolean setByColumn(String column, String value) {
		switch (column) {
		case "typ":
			this.typ = value;
			break;
		case "cd":
			this.cd = value;
			break;
		case "cg":
			this.cg = value;
			break;
		case "fich":
			this.fich = value;
			break;
		case "sign":
			this.sign = value;
			break;
		case "per":
			this.per = value;
			break;
		case "val":
			this.val = value;
			break;
		default:
			return false;
		}
		return true;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEtab() {
		return etab;
	}

	public void setEtab(String etab) {
		this.etab = etab;
	}

	public String getFich() {
		return fich;
	}

	public void setFich(String fich) {
		this.fich = fich;
	}

	public String getCg() {
		return cg;
	}

	public void setCg(String cg) {
		this.cg = cg;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getCd() {
		return cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
	}

	public String getCrdt() {
		return crdt;
	}

	public void setCrdt(String crdt) {
		this.crdt = crdt;
	}

	public String getMdfi() {
		return mdfi;
	}

	public void setMdfi(String mdfi) {
		this.mdfi = mdfi;
	}

	public String getPer() {
		return per;
	}

	public void setPer(String per) {
		this.per = per;
	}

	public String getDele() {
		return dele;
	}

	public void setDele(String dele) {
		this.dele = dele;
	}

}
