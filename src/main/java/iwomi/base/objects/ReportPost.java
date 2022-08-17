
package iwomi.base.objects;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "rppost")
public class ReportPost {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String etab;
	private String codep;
	private String lebel;
	private String char1;
	private String char2;
	private String sens;
	private String cuser;
	private String muser;
	private Long dele;
	private String crtd;
	private Date mdfi;

	public Long getDele() {
		return dele;
	}

	public void setDele(Long dele) {
		this.dele = dele;
	}

	public ReportPost() {

	}

	public Long getId() {
		return id;
	}

	public String getEtab() {
		return etab;
	}

	public String getCodep() {
		return codep;
	}

	public String getLebel() {
		return lebel;
	}

	public String getChar1() {
		return char1;
	}

	public String getChar2() {
		return char2;
	}

	public String getSens() {
		return sens;
	}

	public String getCuser() {
		return cuser;
	}

	public String getMuser() {
		return muser;
	}

	public String getCrdt() {
		return crtd;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setEtab(String etab) {
		this.etab = etab;
	}

	public void setCodep(String codep) {
		this.codep = codep;
	}

	public void setLebel(String lebel) {
		this.lebel = lebel;
	}

	public void setChar1(String char1) {
		this.char1 = char1;
	}

	public void setChar2(String char2) {
		this.char2 = char2;
	}

	public void setSens(String sens) {
		this.sens = sens;
	}

	public void setCuser(String cuser) {
		this.cuser = cuser;
	}

	public void setMuser(String muser) {
		this.muser = muser;
	}

	public void setCrdt(String crdt) {
		this.crtd = crdt;
	}

	public void setMdfi(Date mdfi) {
		this.mdfi = mdfi;
	}

	public Date getMdfi() {
		return mdfi;
	}

	public ReportPost(Long id, String etab, String codep, String lebel, String char1, String char2, String sens,
			String cuser, String muser, Date crdt, Date mdfi, String crtd) {
		super();
		this.id = id;
		this.etab = etab;
		this.codep = codep;
		this.lebel = lebel;
		this.char1 = char1;
		this.char2 = char2;
		this.sens = sens;
		this.cuser = cuser;
		this.muser = muser;
		this.crtd = crtd;
		this.mdfi = mdfi;
	}
	public Boolean setByColumn(String column, String value) {
		switch (column) {
		case "char1":
			this.char1 = value;
			break;

		case "char2":
			this.char2 = value;
			break;

		case "sens":
			this.sens = value;
			break;
		default:
			return false;
		}
		return true;

	}

	public Boolean setByColumn1(ReportPost d) {
		if (d.char1 != null) {
			this.char1 = d.char1;
		}
		if (d.char2 != null) {
			this.char2 = d.char2;
		}
		if (d.sens != null) {
			this.sens = d.sens;
		}
		return true;
	}
}
