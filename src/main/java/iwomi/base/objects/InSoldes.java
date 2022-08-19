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
 * ²
 *
 * @author fabri
 */
@Entity(name = "insld")
@Table(name = "insld")
public class InSoldes {

    @Id
    @GeneratedValue
    private Long id;
    private String cetab;
    private Date dar; // date arretée
    private String age; // agence
    private String com; // compte
    private String cle;// cle
    private String dev;// devise
    private String cli; // client
    private String chap; // chapitre
    private Double sldd;// solde en devise au debut
    private Double sldcvd; // solde en contre valeur au debut
    private Double sldcvc; // solde en contre valeur au debut
    private Double cumc; // cumul des mouvements au credit
    private Double cumd; // cumul des mouvements au debit
    private Double sldf; // solde en devise la fin
    private Double sldcvf;// solde en contre valeur la fin
    private Double txb; // taux
    private Date dcre; // date creation
    private Date dmod; // date de modification
    private String uticre; // utilisateur creation
    private String utimod; // utilisateur modification
    private Long dele; // delete
    private String nat; // delete

    public String getNat() {
        return nat;
    }

    public void setNat(String nat) {
        this.nat = nat;
    }

    private String chl1;
    private String chl3;
    private String chl4;
    private String chl5;
    private String chl6;
    private String chl7;
    private String chl8;
    private String chl9;
    private String chl10;
    private String chl2;
    private String chl11;
    private String chl25;
    private String chl22;
    private String chl23;
    private String chl24;
    private String chl26;
    private String chl27;
    private String chl28;
    private String chl29;
    private String chl30;
    private String chl34;
    private String chl31;
    private String chl32;
    private String chl33;
    private String chl35;
    private String chl36;
    private String chl37;
    private String chl38;
    private String chl39;
    private String chl40;
    private String chl21;
    //private String column1;
    private String chl13;
    private String res;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((age == null) ? 0 : age.hashCode());
        result = prime * result + ((cetab == null) ? 0 : cetab.hashCode());
        result = prime * result + ((chap == null) ? 0 : chap.hashCode());
        result = prime * result + ((chl1 == null) ? 0 : chl1.hashCode());
        result = prime * result + ((chl10 == null) ? 0 : chl10.hashCode());
        result = prime * result + ((chl11 == null) ? 0 : chl11.hashCode());
        result = prime * result + ((chl12 == null) ? 0 : chl12.hashCode());
        result = prime * result + ((chl13 == null) ? 0 : chl13.hashCode());
        result = prime * result + ((chl14 == null) ? 0 : chl14.hashCode());
        result = prime * result + ((chl15 == null) ? 0 : chl15.hashCode());
        result = prime * result + ((chl16 == null) ? 0 : chl16.hashCode());
        result = prime * result + ((chl17 == null) ? 0 : chl17.hashCode());
        result = prime * result + ((chl18 == null) ? 0 : chl18.hashCode());
        result = prime * result + ((chl19 == null) ? 0 : chl19.hashCode());
        result = prime * result + ((chl2 == null) ? 0 : chl2.hashCode());
        result = prime * result + ((chl20 == null) ? 0 : chl20.hashCode());
        result = prime * result + ((chl3 == null) ? 0 : chl3.hashCode());
        result = prime * result + ((chl4 == null) ? 0 : chl4.hashCode());
        result = prime * result + ((chl5 == null) ? 0 : chl5.hashCode());
        result = prime * result + ((chl6 == null) ? 0 : chl6.hashCode());
        result = prime * result + ((chl7 == null) ? 0 : chl7.hashCode());
        result = prime * result + ((chl8 == null) ? 0 : chl8.hashCode());
        result = prime * result + ((chl9 == null) ? 0 : chl9.hashCode());
        result = prime * result + ((cle == null) ? 0 : cle.hashCode());
        result = prime * result + ((cli == null) ? 0 : cli.hashCode());
        //result = prime * result + ((column1 == null) ? 0 : column1.hashCode());
        result = prime * result + ((com == null) ? 0 : com.hashCode());
        result = prime * result + ((cumc == null) ? 0 : cumc.hashCode());
        result = prime * result + ((cumd == null) ? 0 : cumd.hashCode());
        result = prime * result + ((dar == null) ? 0 : dar.hashCode());
        result = prime * result + ((dcre == null) ? 0 : dcre.hashCode());
        result = prime * result + ((dele == null) ? 0 : dele.hashCode());
        result = prime * result + ((dev == null) ? 0 : dev.hashCode());
        result = prime * result + ((dmod == null) ? 0 : dmod.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nat == null) ? 0 : nat.hashCode());
        result = prime * result + ((sldcvc == null) ? 0 : sldcvc.hashCode());
        result = prime * result + ((sldcvd == null) ? 0 : sldcvd.hashCode());
        result = prime * result + ((sldcvf == null) ? 0 : sldcvf.hashCode());
        result = prime * result + ((sldd == null) ? 0 : sldd.hashCode());
        result = prime * result + ((sldf == null) ? 0 : sldf.hashCode());
        result = prime * result + ((txb == null) ? 0 : txb.hashCode());
        result = prime * result + ((uticre == null) ? 0 : uticre.hashCode());
        result = prime * result + ((utimod == null) ? 0 : utimod.hashCode());
        return result;
    }

//	public String getColumn1() {
//		return column1;
//	}
//
//	public void setColumn1(String column1) {
//		this.column1 = column1;
//	}
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        InSoldes other = (InSoldes) obj;
        if (age == null) {
            if (other.age != null) {
                return false;
            }
        } else if (!age.equals(other.age)) {
            return false;
        }
        if (cetab == null) {
            if (other.cetab != null) {
                return false;
            }
        } else if (!cetab.equals(other.cetab)) {
            return false;
        }
        if (chap == null) {
            if (other.chap != null) {
                return false;
            }
        } else if (!chap.equals(other.chap)) {
            return false;
        }
        if (chl1 == null) {
            if (other.chl1 != null) {
                return false;
            }
        } else if (!chl1.equals(other.chl1)) {
            return false;
        }
        if (chl10 == null) {
            if (other.chl10 != null) {
                return false;
            }
        } else if (!chl10.equals(other.chl10)) {
            return false;
        }
        if (chl11 == null) {
            if (other.chl11 != null) {
                return false;
            }
        } else if (!chl11.equals(other.chl11)) {
            return false;
        }
        if (chl12 == null) {
            if (other.chl12 != null) {
                return false;
            }
        } else if (!chl12.equals(other.chl12)) {
            return false;
        }
        if (chl13 == null) {
            if (other.chl13 != null) {
                return false;
            }
        } else if (!chl13.equals(other.chl13)) {
            return false;
        }
        if (chl14 == null) {
            if (other.chl14 != null) {
                return false;
            }
        } else if (!chl14.equals(other.chl14)) {
            return false;
        }
        if (chl15 == null) {
            if (other.chl15 != null) {
                return false;
            }
        } else if (!chl15.equals(other.chl15)) {
            return false;
        }
        if (chl16 == null) {
            if (other.chl16 != null) {
                return false;
            }
        } else if (!chl16.equals(other.chl16)) {
            return false;
        }
        if (chl17 == null) {
            if (other.chl17 != null) {
                return false;
            }
        } else if (!chl17.equals(other.chl17)) {
            return false;
        }
        if (chl18 == null) {
            if (other.chl18 != null) {
                return false;
            }
        } else if (!chl18.equals(other.chl18)) {
            return false;
        }
        if (chl19 == null) {
            if (other.chl19 != null) {
                return false;
            }
        } else if (!chl19.equals(other.chl19)) {
            return false;
        }
        if (chl2 == null) {
            if (other.chl2 != null) {
                return false;
            }
        } else if (!chl2.equals(other.chl2)) {
            return false;
        }
        if (chl20 == null) {
            if (other.chl20 != null) {
                return false;
            }
        } else if (!chl20.equals(other.chl20)) {
            return false;
        }
        if (chl3 == null) {
            if (other.chl3 != null) {
                return false;
            }
        } else if (!chl3.equals(other.chl3)) {
            return false;
        }
        if (chl4 == null) {
            if (other.chl4 != null) {
                return false;
            }
        } else if (!chl4.equals(other.chl4)) {
            return false;
        }
        if (chl5 == null) {
            if (other.chl5 != null) {
                return false;
            }
        } else if (!chl5.equals(other.chl5)) {
            return false;
        }
        if (chl6 == null) {
            if (other.chl6 != null) {
                return false;
            }
        } else if (!chl6.equals(other.chl6)) {
            return false;
        }
        if (chl7 == null) {
            if (other.chl7 != null) {
                return false;
            }
        } else if (!chl7.equals(other.chl7)) {
            return false;
        }
        if (chl8 == null) {
            if (other.chl8 != null) {
                return false;
            }
        } else if (!chl8.equals(other.chl8)) {
            return false;
        }
        if (chl9 == null) {
            if (other.chl9 != null) {
                return false;
            }
        } else if (!chl9.equals(other.chl9)) {
            return false;
        }
        if (cle == null) {
            if (other.cle != null) {
                return false;
            }
        } else if (!cle.equals(other.cle)) {
            return false;
        }
        if (cli == null) {
            if (other.cli != null) {
                return false;
            }
        } else if (!cli.equals(other.cli)) {
            return false;
        }
//		if (column1 == null) {
//			if (other.column1 != null)
//				return false;
//		} else if (!column1.equals(other.column1))
//			return false;
        if (com == null) {
            if (other.com != null) {
                return false;
            }
        } else if (!com.equals(other.com)) {
            return false;
        }
        if (cumc == null) {
            if (other.cumc != null) {
                return false;
            }
        } else if (!cumc.equals(other.cumc)) {
            return false;
        }
        if (cumd == null) {
            if (other.cumd != null) {
                return false;
            }
        } else if (!cumd.equals(other.cumd)) {
            return false;
        }
        if (dar == null) {
            if (other.dar != null) {
                return false;
            }
        } else if (!dar.equals(other.dar)) {
            return false;
        }
        if (dcre == null) {
            if (other.dcre != null) {
                return false;
            }
        } else if (!dcre.equals(other.dcre)) {
            return false;
        }
        if (dele == null) {
            if (other.dele != null) {
                return false;
            }
        } else if (!dele.equals(other.dele)) {
            return false;
        }
        if (dev == null) {
            if (other.dev != null) {
                return false;
            }
        } else if (!dev.equals(other.dev)) {
            return false;
        }
        if (dmod == null) {
            if (other.dmod != null) {
                return false;
            }
        } else if (!dmod.equals(other.dmod)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (nat == null) {
            if (other.nat != null) {
                return false;
            }
        } else if (!nat.equals(other.nat)) {
            return false;
        }
        if (sldcvc == null) {
            if (other.sldcvc != null) {
                return false;
            }
        } else if (!sldcvc.equals(other.sldcvc)) {
            return false;
        }
        if (sldcvd == null) {
            if (other.sldcvd != null) {
                return false;
            }
        } else if (!sldcvd.equals(other.sldcvd)) {
            return false;
        }
        if (sldcvf == null) {
            if (other.sldcvf != null) {
                return false;
            }
        } else if (!sldcvf.equals(other.sldcvf)) {
            return false;
        }
        if (sldd == null) {
            if (other.sldd != null) {
                return false;
            }
        } else if (!sldd.equals(other.sldd)) {
            return false;
        }
        if (sldf == null) {
            if (other.sldf != null) {
                return false;
            }
        } else if (!sldf.equals(other.sldf)) {
            return false;
        }
        if (txb == null) {
            if (other.txb != null) {
                return false;
            }
        } else if (!txb.equals(other.txb)) {
            return false;
        }
        if (uticre == null) {
            if (other.uticre != null) {
                return false;
            }
        } else if (!uticre.equals(other.uticre)) {
            return false;
        }
        if (utimod == null) {
            if (other.utimod != null) {
                return false;
            }
        } else if (!utimod.equals(other.utimod)) {
            return false;
        }
        return true;
    }

    public void setChl34(String chl34) {
        this.chl34 = chl34;
    }

    public void setChl31(String chl31) {
        this.chl31 = chl31;
    }

    public void setChl32(String chl32) {
        this.chl32 = chl32;
    }

    public void setChl33(String chl33) {
        this.chl33 = chl33;
    }

    public void setChl35(String chl35) {
        this.chl35 = chl35;
    }

    public void setChl36(String chl36) {
        this.chl36 = chl36;
    }

    public void setChl37(String chl37) {
        this.chl37 = chl37;
    }

    public void setChl38(String chl38) {
        this.chl38 = chl38;
    }

    public void setChl39(String chl39) {
        this.chl39 = chl39;
    }

    public void setChl40(String chl40) {
        this.chl40 = chl40;
    }

    public String getChl34() {
        return chl34;
    }

    public String getChl31() {
        return chl31;
    }

    public String getChl32() {
        return chl32;
    }

    public String getChl33() {
        return chl33;
    }

    public String getChl35() {
        return chl35;
    }

    public String getChl36() {
        return chl36;
    }

    public String getChl37() {
        return chl37;
    }

    public String getChl38() {
        return chl38;
    }

    public String getChl39() {
        return chl39;
    }

    public String getChl40() {
        return chl40;
    }

    private String chl14;
    private String chl15;
    private String chl16;
    private String chl17;
    private String chl18;
    private String chl19;
    private String chl20;
    private String chl12;

    public String getChl12() {
        return chl12;
    }

    public void setChl12(String chl12) {
        this.chl12 = chl12;
    }

    public String getChl11() {
        return chl11;
    }

    public void setChl25(String chl25) {
        this.chl25 = chl25;
    }

    public void setChl22(String chl22) {
        this.chl22 = chl22;
    }

    public void setChl23(String chl23) {
        this.chl23 = chl23;
    }

    public void setChl24(String chl24) {
        this.chl24 = chl24;
    }

    public void setChl26(String chl26) {
        this.chl26 = chl26;
    }

    public void setChl27(String chl27) {
        this.chl27 = chl27;
    }

    public void setChl28(String chl28) {
        this.chl28 = chl28;
    }

    public void setChl29(String chl29) {
        this.chl29 = chl29;
    }

    public void setChl30(String chl30) {
        this.chl30 = chl30;
    }

    public void setChl21(String chl21) {
        this.chl21 = chl21;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getChl25() {
        return chl25;
    }

    public String getChl22() {
        return chl22;
    }

    public String getChl23() {
        return chl23;
    }

    public String getChl24() {
        return chl24;
    }

    public String getChl26() {
        return chl26;
    }

    public String getChl27() {
        return chl27;
    }

    public String getChl28() {
        return chl28;
    }

    public String getChl29() {
        return chl29;
    }

    public String getChl30() {
        return chl30;
    }

    public String getChl21() {
        return chl21;
    }

    public String getRes() {
        return res;
    }

    public void setChl11(String chl11) {
        this.chl11 = chl11;
    }

    public String getChl13() {
        return chl13;
    }

    public void setChl13(String chl13) {
        this.chl13 = chl13;
    }

    public String getChl14() {
        return chl14;
    }

    public void setChl14(String chl14) {
        this.chl14 = chl14;
    }

    public String getChl15() {
        return chl15;
    }

    public void setChl15(String chl15) {
        this.chl15 = chl15;
    }

    public String getChl16() {
        return chl16;
    }

    public void setChl16(String chl16) {
        this.chl16 = chl16;
    }

    public String getChl17() {
        return chl17;
    }

    public void setChl17(String chl17) {
        this.chl17 = chl17;
    }

    public String getChl18() {
        return chl18;
    }

    public void setChl18(String chl18) {
        this.chl18 = chl18;
    }

    public String getChl19() {
        return chl19;
    }

    public void setChl19(String chl19) {
        this.chl19 = chl19;
    }

    public String getChl20() {
        return chl20;
    }

    public void setChl20(String chl20) {
        this.chl20 = chl20;
    }

    public Double getSldcvc() {
        return sldcvc;
    }

    public void setSldcvc(Double sldcvc) {
        this.sldcvc = sldcvc;
    }

    public String getChl1() {
        return chl1;
    }

    public void setChl1(String chl1) {
        this.chl1 = chl1;
    }

    public InSoldes(String cetab, String nat, Date date, String age, String com, String cle, String dev, String cli,
            String chap, Double sldd, Double sldcvd, Double sldcvc, Double cumc, Double cumd, Double sldf,
            Double sldcvf, Double txb, Date date2, Date date3, String uticre, String utimod, Long dele) {
        this.cetab = cetab;
        this.nat = nat;
        this.dar = (Date) date;
        this.age = age;
        this.com = com;
        this.cle = cle;
        this.dev = dev;
        this.cli = cli;
        this.chap = chap;
        this.sldd = sldd;
        this.sldcvd = sldcvd;
        this.sldcvc = sldcvc;
        this.cumc = cumc;
        this.cumd = cumd;
        this.sldf = sldf;
        this.sldcvf = sldcvf;
        this.txb = txb;
        this.dcre = date2;
        this.dmod = date3;
        this.uticre = uticre;
        this.utimod = utimod;
        this.dele = dele;
    }

    public InSoldes(String nat, String cetab, Date dar, String age, String com, String cle, String dev, String cli,
            String chap, Double sldd, Double sldcvd, Double cumc, Double cumd, Double txb,
            Date dcre, Date dmod, String uticre, String utimod, Long dele, String chl1, String chl2, String chl3,
            String chl4, String chl5, String chl6, String chl7, String chl8, String chl9, String chl10, String chl11,
            String chl12, String chl13, String chl14, String chl15, String chl16, String chl17, String chl18,
            String chl19, String chl20) {

        this.cetab = cetab;
        this.dar = dar;
        this.age = age;
        this.com = com;
        this.cle = cle;
        this.dev = dev;
        this.cli = cli;
        this.chap = chap;
        this.sldd = sldd;
        this.sldcvd = sldcvd;
        this.cumc = cumc;
        this.cumd = cumd;
        this.txb = txb;
        this.dcre = dcre;
        this.dmod = dmod;
        this.uticre = uticre;
        this.utimod = utimod;
        this.dele = dele;
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
        this.chl11 = chl11;
        this.chl12 = chl12;
        this.chl13 = chl13;
        this.chl14 = chl14;
        this.chl15 = chl15;
        this.chl16 = chl16;
        this.chl17 = chl17;
        this.chl18 = chl18;
        this.chl19 = chl19;
        this.chl20 = chl20;
    }

    public InSoldes(Long id, String nat, String cetab, Date dar, String age, String com, String cle, String dev,
            String cli, String chap, Double sldd, Double sldcvd, Double cumc, Double cumd, Double sldf, Double sldcvf,
            Double txb, Date dcre, Date dmod, String uticre, String utimod, Long dele, String chl1, String chl2,
            String chl3, String chl4, String chl5, String chl6, String chl7, String chl8, String chl9, String chl10,
            String chl11, String chl12, String chl13, String chl14, String chl15, String chl16, String chl17,
            String chl18, String chl19, String chl20) {

        this.id = id;
        this.cetab = cetab;
        this.dar = dar;
        this.age = age;
        this.com = com;
        this.cle = cle;
        this.dev = dev;
        this.cli = cli;
        this.chap = chap;
        this.sldd = sldd;
        this.sldcvd = sldcvd;
        this.cumc = cumc;
        this.cumd = cumd;
        this.sldf = sldf;
        this.sldcvf = sldcvf;
        this.txb = txb;
        this.dcre = dcre;
        this.dmod = dmod;
        this.uticre = uticre;
        this.utimod = utimod;
        this.dele = dele;
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
        this.chl11 = chl11;
        this.chl12 = chl12;
        this.chl13 = chl13;
        this.chl14 = chl14;
        this.chl15 = chl15;
        this.chl16 = chl16;
        this.chl17 = chl17;
        this.chl18 = chl18;
        this.chl19 = chl19;
        this.chl20 = chl20;
    }

    public InSoldes(String cetab, Date dar, String age, String com, String cle, String dev, String cli,
            String chap, Double sldd, Double sldcvd, String nat, String res, Double txb, Double cumc, Double cumd,
            String chl1, String chl2, String chl3,
            String chl4, String chl5, String chl6, String chl7, String chl8, String chl9, String chl10, String chl11,
            String chl12, String chl13, String chl14, String chl15, String chl16, String chl17, String chl18,
            String chl19, String chl20, Date dcre, Date dmod, String uticre, String utimod, String chl21, String chl22, String chl23, String chl24, String chl25, String chl26, String chl27, String chl28, String chl29, String chl30) {

        this.cetab = cetab;
        this.dar = dar;
        this.age = age;
        this.com = com;
        this.cle = cle;
        this.dev = dev;
        this.cli = cli;
        this.chap = chap;
        this.sldd = sldd;
        this.sldcvd = sldcvd;
        this.nat = nat;
        this.res = res;
        this.txb = txb;
        this.cumc = cumc;
        this.cumd = cumd;
        this.dele = 0L;
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
        this.chl11 = chl11;
        this.chl12 = chl12;
        this.chl13 = chl13;
        this.chl14 = chl14;
        this.chl15 = chl15;
        this.chl16 = chl16;
        this.chl17 = chl17;
        this.chl18 = chl18;
        this.chl19 = chl19;
        this.chl20 = chl20;
        this.dcre = dcre;
        this.dmod = dmod;
        this.uticre = uticre;
        this.utimod = utimod;
        this.chl21 = chl21;
        this.chl22 = chl22;
        this.chl23 = chl23;
        this.chl24 = chl24;
        this.chl25 = chl25;
        this.chl26 = chl26;
        this.chl27 = chl27;
        this.chl28 = chl28;
        this.chl29 = chl29;
        this.chl30 = chl30;

    }
public InSoldes(String cetab, Date dar, String age, String com, String cle, String dev, String cli,
            String chap, Double sldd, Double sldcvd, String nat, String res, Double txb, Double cumc, Double cumd,
            String chl1, String chl2, String chl3,
            String chl4, String chl5, String chl6, String chl7, String chl8, String chl9, String chl10, String chl11,
            String chl12, String chl13, String chl14, String chl15, String chl16, String chl17, String chl18,
            String chl19, String chl20, Date dcre, Date dmod, String uticre, String utimod) {

        this.cetab = cetab;
        this.dar = dar;
        this.age = age;
        this.com = com;
        this.cle = cle;
        this.dev = dev;
        this.cli = cli;
        this.chap = chap;
        this.sldd = sldd;
        this.sldcvd = sldcvd;
        this.nat = nat;
        this.res = res;
        this.txb = txb;
        this.cumc = cumc;
        this.cumd = cumd;
        this.dele = 0L;
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
        this.chl11 = chl11;
        this.chl12 = chl12;
        this.chl13 = chl13;
        this.chl14 = chl14;
        this.chl15 = chl15;
        this.chl16 = chl16;
        this.chl17 = chl17;
        this.chl18 = chl18;
        this.chl19 = chl19;
        this.chl20 = chl20;
        this.dcre = dcre;
        this.dmod = dmod;
        this.uticre = uticre;
        this.utimod = utimod;

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

    public Date getDar() {
        return dar;
    }

    public void setDar(Date dar) {
        this.dar = dar;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getCle() {
        return cle;
    }

    public void setCle(String cle) {
        this.cle = cle;
    }

    public String getDev() {
        return dev;
    }

    public void setDev(String dev) {
        this.dev = dev;
    }

    public String getCli() {
        return cli;
    }

    public void setCli(String cli) {
        this.cli = cli;
    }

    public String getChap() {
        return chap;
    }

    public void setChap(String chap) {
        this.chap = chap;
    }

    public Double getSldd() {
        return sldd;
    }

    public void setSldd(Double sldd) {
        this.sldd = sldd;
    }

    public Double getSldcvd() {
        return sldcvd;
    }

    public void setSldcvd(Double sldcvd) {
        this.sldcvd = sldcvd;
    }

    public Double getCumc() {
        return cumc;
    }

    public void setCumc(Double cumc) {
        this.cumc = cumc;
    }

    public Double getCumd() {
        return cumd;
    }

    public void setCumd(Double cumd) {
        this.cumd = cumd;
    }

    public Double getSldf() {
        return sldf;
    }

    public void setSldf(Double sldf) {
        this.sldf = sldf;
    }

    public Double getSldcvf() {
        return sldcvf;
    }

    public void setSldcvf(Double sldcvf) {
        this.sldcvf = sldcvf;
    }

    public Double getTxb() {
        return txb;
    }

    public void setTxb(Double txb) {
        this.txb = txb;
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

    public InSoldes() {
        super();
        // TODO Auto-generated constructor stub
    }

}
