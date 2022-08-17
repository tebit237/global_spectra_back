package iwomi.base.objects;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@SequenceGenerator(name="seqe", initialValue=1, allocationSize = 5000)
@Entity
@Table(name = "rpcalc")
public class ReportCalculate {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqe")
//	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String etab;
	private String fichi;
	private String field;
	private String lib;
	private String post;
	private String col;
	private String calc;
	private String source;
	private String typeval;
	private String divd;
	private String cuser;
	private String descc;
	private String tysorce;
	private Date crdt;
	private Date mdfi;
	private long dele;
	private String muser;

	public String getDescc() {
		return descc;
	}

	public void setDescc(String descc) {
		this.descc = descc;
	}

	public String getTysorce() {
		return tysorce;
	}

	public void setTysorce(String tysorce) {
		this.tysorce = tysorce;
	}

	public long getDele() {
		return dele;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDele(long dele) {
		this.dele = dele;
	}

	public void setEtab(String etab) {
		this.etab = etab;
	}

	public void setFichi(String fichi) {
		this.fichi = fichi;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setLib(String lib) {
		this.lib = lib;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public void setCol(String col) {
		this.col = col;
	}

	public void setCalc(String calc) {
		this.calc = calc;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setTypeval(String typeval) {
		this.typeval = typeval;
	}

	public void setDivd(String divd) {
		this.divd = divd;
	}

	public void setCuser(String cuser) {
		this.cuser = cuser;
	}

	public void setMuser(String muser) {
		this.muser = muser;
	}

	public void setCrdt(Date crdt) {
		this.crdt = crdt;
	}

	public void setMdfi(Date mdfi) {
		this.mdfi = mdfi;
	}

	public ReportCalculate() {

	}

	public Long getId() {
		return id;
	}

	public String getEtab() {
		return etab;
	}

	public String getFichi() {
		return fichi;
	}

	public String getField() {
		return field;
	}

	public String getSource() {
		return source;
	}

	public String getTypeval() {
		return typeval;
	}

	public String getLib() {
		return lib;
	}

	public String getPost() {
		return post;
	}

	public String getCol() {
		return col;
	}

	public String getCalc() {
		return calc;
	}

	public String getDivd() {
		return divd;
	}

	public String getCuser() {
		return cuser;
	}

	public String getMuser() {
		return muser;
	}

	public Date getCrdt() {
		return crdt;
	}

	public Date getMdfi() {
		return mdfi;
	}

	public ReportCalculate(Long id, String etab, String fichi, String field, String lib, String post, String col,
			String calc, String source, String typeval, String divd, String cuser, String descc, String tysorce,
			Date crdt, Date mdfi, long dele, String muser) {
		super();
		this.id = id;
		this.etab = etab;
		this.fichi = fichi;
		this.field = field;
		this.lib = lib;
		this.post = post;
		this.col = col;
		this.calc = calc;
		this.source = source;
		this.typeval = typeval;
		this.divd = divd;
		this.cuser = cuser;
		this.descc = descc;
		this.tysorce = tysorce;
		this.crdt = crdt;
		this.mdfi = mdfi;
		this.dele = dele;
		this.muser = muser;
	}


	

}
