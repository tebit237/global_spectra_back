
package iwomi.base.objects;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SequenceGenerator(name = "seq", initialValue = 5, allocationSize = 310)
@Entity
@Table(name = "srpatt") 

public class ReportAttributeS {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	private Long id;
	private String etab;
	
	private String att;
	private String val;
	private String type;
	private String lib;
	private String tab;
	private String sel;
	private String cuser;
	private String muser;
	private Date crdt;
	private Date mdfi;
	private Boolean dele;

	public Boolean setByColumn1(ReportAttributeS d) {
		if (d.att != null) {
			this.att = d.att;
		}
		if (d.type != null) {
			this.type = d.type;
		}
		if (d.lib != null) {
			this.lib = d.lib;
		}
		if (d.val != null) {
			this.val = d.val;
		}
		if (d.tab != null) {
			this.tab = d.tab;
		}
		if (d.sel != null) {
			this.sel = d.sel;
		}
		return true;
	}

    public void setSel(String sel) {
        this.sel = sel;
    }

    public String getSel() {
        return sel;
    }

	public Boolean getDele() {
		return dele;
	}

	public void setDele(Boolean dele) {
		this.dele = dele;
	}

	public ReportAttributeS() {

	}

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getTab() {
        return tab;
    }

	public void setId(Long id) {
		this.id = id;
	}

	public void setEtab(String etab) {
		this.etab = etab;
	}

	public void setAtt(String att) {
		this.att = att;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setLib(String lib) {
		this.lib = lib;
	}

	public void setCuser(String cuser) {
		this.cuser = cuser;
	}

	public void setMuser(String muser) {
		this.muser = muser;
	}

	public void setMdfi(Date mdfi) {
		this.mdfi = mdfi;
	}

	public Long getId() {
		return id;
	}

	public String getEtab() {
		return etab;
	}

	public String getAtt() {
		return att;
	}

	public String getVal() {
		return val;
	}

	public String getType() {
		return type;
	}

	public String getLib() {
		return lib;
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

	public ReportAttributeS(Long id, String etab, String att, String val, String type, String lib, String cuser,
			String muser, Date crdt, Date mdfi, Boolean dele) {
		super();
		this.id = id;
		this.etab = etab;
		this.att = att;
		this.val = val;
		this.type = type;
		this.lib = lib;
		this.cuser = cuser;
		this.muser = muser;
		this.crdt = crdt;
		this.mdfi = mdfi;
		this.dele = dele;
	}

	public void setCrdt(Date crdt) {
		this.crdt = crdt;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> mapObj = new HashMap<>();
		mapObj.put("att", this.att);
		mapObj.put("type", this.type);
		mapObj.put("lib", this.lib);
		mapObj.put("val", this.val);
		return mapObj;
	}

	public Boolean setByColumn(String column, String value) {
		switch (column) {
		case "att":
			this.att = value;
			break;

		case "type":
			this.type = value;
			break;

		case "val":
			this.val = value;
			break;
		case "lib1":
			this.lib = value;
			break;
		default:
			return false;
		}
		return true;

	}

}
