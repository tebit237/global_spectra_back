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
@Table(name = "srpctrlt")
public class ReportControleInterSS {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String cg;
	private String typ;
	private String sign;
	private String val;
	private String dele;
	private String mdfi;
	private String crdt;
	private String cd;
	private String per;

	public ReportControleInterSS() {

	}

	public ReportControleInterSS(Long id, String val, String sign, String cg, String cd, String typ, String per) {
		super();
		this.id = id;
		this.per = per;
		this.cg = cg;
		this.typ = typ;
		this.sign = sign;
		this.val = val;
		this.cd = cd;

	}

	public Boolean setByColumn(String column, String value) {
		switch (column) {
		case "cg":
			this.cg = value;
			break;
			
		case "cd":
			this.cd = value;
			break;
		case "typ":
			this.typ = value;
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

	public String getDele() {
		return dele;
	}

	public void setDele(String dele) {
		this.dele = dele;
	}

	public String getMdfi() {
		return mdfi;
	}

	public String getCrdt() {
		return crdt;
	}

	public void setCrdt(String crdt) {
		this.crdt = crdt;
	}

	public void setMdfi(String mdfi) {
		this.mdfi = mdfi;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCg(String cg) {
		this.cg = cg;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public void setCd(String cd) {
		this.cd = cd;
	}

	public void setPer(String per) {
		this.per = per;
	}

	public Long getId() {
		return id;
	}

	public String getPer() {
		return per;
	}

	public String getCg() {
		return cg;
	}

	public String getTyp() {
		return typ;
	}

	public String getSign() {
		return sign;
	}

	public String getVal() {
		return val;
	}

	public String getCd() {
		return cd;
	}
	public Boolean setByColumn1(ReportControleInterSS d) {
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

}
