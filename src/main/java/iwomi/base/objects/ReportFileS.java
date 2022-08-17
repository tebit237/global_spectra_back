
package iwomi.base.objects;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "srppfich")
public class ReportFileS  implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String etab;
	private String poste;
	private String fich;
	private String tfic;
	public String getTfic() {
		return tfic;
	}
	public void setTfic(String tfic) {
		this.tfic = tfic;
	}
	private String gen;
	private Long rang;
	private String feui;
	private String source;
	private Long col;
	private String cuser;
	public ReportFileS() {
		super();
		// TODO Auto-generated constructor stub
	}
	private String muser;
	private Date crdt;
	public ReportFileS(Long id, String etab, String poste, String fich, String gen, Long rang, String feui,
			String source, Long col, String cuser, String muser, Date crdt, Date mdfi) {
		super();
		this.id = id;
		this.etab = etab;
		this.poste = poste;
		this.fich = fich;
		this.gen = gen;
		this.rang = rang;
		this.feui = feui;
		this.source = source;
		this.col = col;
		this.cuser = cuser;
		this.muser = muser;
		this.crdt = crdt;
		this.mdfi = mdfi;
	}
	private Date mdfi;
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
	public String getPoste() {
		return poste;
	}
	public void setPoste(String poste) {
		this.poste = poste;
	}
	public String getFich() {
		return fich;
	}
	public void setFich(String fich) {
		this.fich = fich;
	}
	public String getGen() {
		return gen;
	}
	public void setGen(String gen) {
		this.gen = gen;
	}
	public Long getRang() {
		return rang;
	}
	public void setRang(Long rang) {
		this.rang = rang;
	}
	public String getFeui() {
		return feui;
	}
	public void setFeui(String feui) {
		this.feui = feui;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Long getCol() {
		return col;
	}
	public void setCol(Long col) {
		this.col = col;
	}
	public String getCuser() {
		return cuser;
	}
	public void setCuser(String cuser) {
		this.cuser = cuser;
	}
	public String getMuser() {
		return muser;
	}
	public void setMuser(String muser) {
		this.muser = muser;
	}
	public Date getCrdt() {
		return crdt;
	}
	public void setCrdt(Date crdt) {
		this.crdt = crdt;
	}
	public Date getMdfi() {
		return mdfi;
	}
	public void setMdfi(Date mdfi) {
		this.mdfi = mdfi;
	}
	
}
