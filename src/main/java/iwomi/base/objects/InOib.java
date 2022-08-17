package iwomi.base.objects;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "INOIB")
@Entity(name = "INOIB")

public class InOib {
	
	@Id
	    @GeneratedValue

	private Long id;
	private String age;
	private String cha;
	private String cle;
	private String cli;
	private String com;
	private String dcre;
	private String deb;
	private String dev;
	private String dmep;
	private String dmod;
	private String dod;
	private String fin;
	private String issu;
	private String mob;
	private String moncv;
	private String mont;
	private String nat;
	private String ref;
	private String sen;
	private String tau;
	private String typ;
	private String uticre;
	private String utimod;
	private Long dele;
	private String cetab;
	private Date dar;
	
	public InOib() {
		
	}

    public Date getDar() {
        return dar;
    }

    public void setDar(Date dar) {
        this.dar = dar;
    }

        
        
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCha() {
        return cha;
    }

    public void setCha(String cha) {
        this.cha = cha;
    }

    public String getCle() {
        return cle;
    }

    public void setCle(String cle) {
        this.cle = cle;
    }

    public String getCli() {
        return cli;
    }

    public void setCli(String cli) {
        this.cli = cli;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getDcre() {
        return dcre;
    }

    public void setDcre(String dcre) {
        this.dcre = dcre;
    }

    public String getDeb() {
        return deb;
    }

    public void setDeb(String deb) {
        this.deb = deb;
    }

    public String getDev() {
        return dev;
    }

    public void setDev(String dev) {
        this.dev = dev;
    }

    public String getDmep() {
        return dmep;
    }

    public void setDmep(String dmep) {
        this.dmep = dmep;
    }

    public String getDmod() {
        return dmod;
    }

    public void setDmod(String dmod) {
        this.dmod = dmod;
    }

    public String getDod() {
        return dod;
    }

    public void setDod(String dod) {
        this.dod = dod;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public String getIssu() {
        return issu;
    }

    public void setIssu(String issu) {
        this.issu = issu;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getMoncv() {
        return moncv;
    }

    public void setMoncv(String moncv) {
        this.moncv = moncv;
    }

    public String getMont() {
        return mont;
    }

    public void setMont(String mont) {
        this.mont = mont;
    }

    public String getNat() {
        return nat;
    }

    public void setNat(String nat) {
        this.nat = nat;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getSen() {
        return sen;
    }

    public void setSen(String sen) {
        this.sen = sen;
    }

    public String getTau() {
        return tau;
    }

    public void setTau(String tau) {
        this.tau = tau;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
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

    public Long getDele() {
        return dele;
    }

    public void setDele(Long dele) {
        this.dele = dele;
    }

    public String getCetab() {
        return cetab;
    }

    public void setCetab(String cetab) {
        this.cetab = cetab;
    }

    public InOib(String age, String cha, String cle, String cli, String com, String dcre, String deb, String dev, String dmep, String dmod, String dod, String fin, String issu, String mob, String moncv, String mont, String nat, String ref, String sen, String tau, String typ, String uticre, String utimod, Long dele, String cetab,Date dar) {
        this.age = age;
        this.cha = cha;
        this.cle = cle;
        this.cli = cli;
        this.com = com;
        this.dcre = dcre;
        this.deb = deb;
        this.dev = dev;
        this.dmep = dmep;
        this.dmod = dmod;
        this.dod = dod;
        this.fin = fin;
        this.issu = issu;
        this.mob = mob;
        this.moncv = moncv;
        this.mont = mont;
        this.nat = nat;
        this.ref = ref;
        this.sen = sen;
        this.tau = tau;
        this.typ = typ;
        this.uticre = uticre;
        this.utimod = utimod;
        this.dele = dele;
        this.cetab = cetab;
        this.dar = dar;
    }
        
    


}
