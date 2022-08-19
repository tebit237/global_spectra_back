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
@Table(name = "srpctrcx")
public class ReportControleComplexS {
	public String getDescp() {
		return descp;
	}
	public void setDescp(String descp) {
		this.descp = descp;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String etab;
	private String descp;
	private String typd;
	private String typg;
	private String cg;
	private String sign;
	private String cd;
	private String crdt;
	private String mdfi;
	private String per;
	private String dele;
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
	public String getDesc() {
		return descp;
	}
	public void setDesc(String desc) {
		this.descp = desc;
	}
	public String getCg() {
		return cg;
	}
	public void setCg(String cg) {
		this.cg = cg;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
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
	public String getTypd() {
		return typd;
	}
	public void setTypd(String typd) {
		this.typd = typd;
	}
	public String getTypg() {
		return typg;
	}
	public void setTypg(String typg) {
		this.typg = typg;
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
	public ReportControleComplexS(Long id, String etab, String desc, String cg, String sign, String cd, String crdt,
			String mdfi, String per, String dele) {
		super();
		this.id = id;
		this.etab = etab;
		this.descp = desc;
		this.cg = cg;
		this.sign = sign;
		this.cd = cd;
		this.crdt = crdt;
		this.mdfi = mdfi;
		this.per = per;
		this.dele = dele;
	}
	public ReportControleComplexS() {
		super();
	}

	public Boolean setByColumn1(ReportControleComplexS d) {
		if (d.cd != null) {
			this.cd = d.cd;
		}
		if (d.cg != null) {
			this.cg = d.cg;
		}
		if (d.sign != null) {
			this.sign = d.sign;
		}
		if (d.descp != null) {
			this.descp = d.descp;
		}
		if (d.typg != null) {
			this.typg = d.typg;
		}
		if (d.typd != null) {
			this.typd = d.typd;
		}
		
		return true;
	}
	
	public Boolean setByColumn(String column, String value) {
		switch (column) {
		case "cd":
			this.cd = value;
			break;
		case "cg":
			this.cg = value;
			break;
		case "descp":
			this.descp = value;
			break;
		case "sign":
			this.sign = value;
			break;
		case "per":
			this.per = value;
			break;
		default:
			return false;
		}
		return true;

	}
}
