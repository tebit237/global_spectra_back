/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwomi.base.objects;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *
 * @author fabri
 */
@Entity(name = "sqltype")
@Table(name = "sqltype")
//@TableGenerator(name = "tab", initialValue = 140, allocationSize = 50)
public class SqlFileType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String etab; // cetab
    private String col1; // cetab
    private String col2; // cetab
    private String col3; // cetab
    private String col4; // cetab
    private String col5; // cetab
    private String col6; // cetab
    private String col7; // cetab
    private String col8; // cetab
    private String col9; // cetab
    private String col10; // cetab
    private String col11; // cetab
    private String col12; // cetab
    private String col13; // cetab
    private String col14; // cetab
    private String col15; // cetab
    private String col16; // cetab
    private String col17; // cetab
    private String col18; // cetab
    private String col19; // cetab
    private String col20; // cetab
    private String col21; // cetab
    private String col22; // cetab
    private String col23; // cetab
    private String col24; // cetab
    private String col25; // cetab
    private String col26; // cetab
    private String col27; // cetab
    private String col28; // cetab
    private String col29; // cetab
    private String col30; // cetab
    private String fichi; // delete
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dar; // delete

    public String getFichi() {
        return fichi;
    }

    public String cellExtra(int n) {
        String s = "";
        switch (n) {
            case 1:
                s = col1;
                break;
            case 2:
                s = col2;
                break;
            case 3:
                s = col3;
                break;
            case 4:
                s = col4;
                break;
            case 5:
                s = col5;
                break;
            case 6:
                s = col6;
                break;
            case 7:
                s = col7;
                break;
            case 8:
                s = col8;
                break;
            case 9:
                s = col9;
                break;
            case 10:
                s = col10;
                break;
            case 11:
                s = col11;
                break;
            case 12:
                s = col12;
                break;
            case 13:
                s = col13;
                break;
            case 14:
                s = col14;
                break;
            case 15:
                s = col15;
                break;
            case 16:
                s = col16;
                break;
            case 17:
                s = col17;
                break;
            case 18:
                s = col18;
                break;
            case 19:
                s = col19;
                break;
            case 20:
                s = col20;
                break;
            case 21:
                s = col21;
                break;
            case 22:
                s = col22;
                break;
            case 23:
                s = col23;
                break;
            case 24:
                s = col24;
                break;
            case 25:
                s = col25;
                break;
            case 26:
                s = col26;
                break;
            case 27:
                s = col27;
                break;
            case 28:
                s = col28;
                break;
            case 29:
                s = col29;
                break;
            case 30:
                s = col30;
                break;
        }
        return s;
    }

    public SqlFileType cellinsert(int n, String v) {
        String s = "";
        switch (n) {
            case 1:
                col1 = v;
                break;
            case 2:
                col2 = v;
                break;
            case 3:
                col3 = v;
                break;
            case 4:
                col4 = v;
                break;
            case 5:
                col5 = v;
                break;
            case 6:
                col6 = v;
                break;
            case 7:
                col7 = v;
                break;
            case 8:
                col8 = v;
                break;
            case 9:
                col9 = v;
                break;
            case 10:
                col10 = v;
                break;
            case 11:
                col11 = v;
                break;
            case 12:
                col12 = v;
                break;
            case 13:
                col13 = v;
                break;
            case 14:
                col14 = v;
                break;
            case 15:
                col15 = v;
                break;
            case 16:
                col16 = v;
                break;
            case 17:
                col17 = v;
                break;
            case 18:
                col18 = v;
                break;
            case 19:
                col19 = v;
                break;
            case 20:
                col20 = v;
                break;
            case 21:
                col21 = v;
                break;
            case 22:
                col22 = v;
                break;
            case 23:
                col23 = v;
                break;
            case 24:
                col24 = v;
                break;
            case 25:
                col25 = v;
                break;
            case 26:
                col26 = v;
                break;
            case 27:
                col27 = v;
                break;
            case 28:
                col28 = v;
                break;
            case 29:
                col29 = v;
                break;
            case 30:
                col30 = v;
                break;
        }
       return this;
    }

    public void setFichi(String fichi) {
        this.fichi = fichi;
    }

    public String getCetab() {
        return etab;
    }

    public void setCetab(String cetab) {
        this.etab = cetab;
    }

    public String getEtab() {
        return etab;
    }

    public void setEtab(String etab) {
        this.etab = etab;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    public String getCol2() {
        return col2;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }

    public String getCol3() {
        return col3;
    }

    public void setCol21(String col21) {
        this.col21 = col21;
    }

    public void setCol22(String col22) {
        this.col22 = col22;
    }

    public void setCol23(String col23) {
        this.col23 = col23;
    }

    public void setCol24(String col24) {
        this.col24 = col24;
    }

    public void setCol25(String col25) {
        this.col25 = col25;
    }

    public void setCol26(String col26) {
        this.col26 = col26;
    }

    public void setCol27(String col27) {
        this.col27 = col27;
    }

    public void setCol28(String col28) {
        this.col28 = col28;
    }

    public void setCol29(String col29) {
        this.col29 = col29;
    }

    public void setCol30(String col30) {
        this.col30 = col30;
    }

    public String getCol21() {
        return col21;
    }

    public String getCol22() {
        return col22;
    }

    public String getCol23() {
        return col23;
    }

    public String getCol24() {
        return col24;
    }

    public String getCol25() {
        return col25;
    }

    public String getCol26() {
        return col26;
    }

    public String getCol27() {
        return col27;
    }

    public String getCol28() {
        return col28;
    }

    public String getCol29() {
        return col29;
    }

    public String getCol30() {
        return col30;
    }

    public void setCol3(String col3) {
        this.col3 = col3;
    }

    public String getCol4() {
        return col4;
    }

    public void setCol4(String col4) {
        this.col4 = col4;
    }

    public String getCol5() {
        return col5;
    }

    public void setCol5(String col5) {
        this.col5 = col5;
    }

    public String getCol6() {
        return col6;
    }

    public void setCol6(String col6) {
        this.col6 = col6;
    }

    public String getCol7() {
        return col7;
    }

    public void setCol7(String col7) {
        this.col7 = col7;
    }

    public String getCol8() {
        return col8;
    }

    public void setCol8(String col8) {
        this.col8 = col8;
    }

    public String getCol9() {
        return col9;
    }

    public void setCol9(String col9) {
        this.col9 = col9;
    }

    public String getCol10() {
        return col10;
    }

    public void setCol10(String col10) {
        this.col10 = col10;
    }

    public String getCol11() {
        return col11;
    }

    public void setCol11(String col11) {
        this.col11 = col11;
    }

    public String getCol12() {
        return col12;
    }

    public void setCol12(String col12) {
        this.col12 = col12;
    }

    public String getCol13() {
        return col13;
    }

    public void setCol13(String col13) {
        this.col13 = col13;
    }

    public String getCol14() {
        return col14;
    }

    public void setCol14(String col14) {
        this.col14 = col14;
    }

    public String getCol15() {
        return col15;
    }

    public void setCol15(String col15) {
        this.col15 = col15;
    }

    public String getCol16() {
        return col16;
    }

    public void setCol16(String col16) {
        this.col16 = col16;
    }

    public String getCol17() {
        return col17;
    }

    public void setCol17(String col17) {
        this.col17 = col17;
    }

    public String getCol18() {
        return col18;
    }

    public void setCol18(String col18) {
        this.col18 = col18;
    }

    public String getCol19() {
        return col19;
    }

    public void setCol19(String col19) {
        this.col19 = col19;
    }

    public String getCol20() {
        return col20;
    }

    public void setCol20(String col20) {
        this.col20 = col20;
    }

    public Date getDar() {
        return dar;
    }

    public void setDar(Date dar) {
        this.dar = dar;
    }

    public SqlFileType() {
    }

    public Boolean setByColumn1(SqlFileType d) {
        if (d.col1 != null) {
            this.col1 = d.col1;
        }
        if (d.col2 != null) {
            this.col2 = d.col2;
        }
        if (d.col3 != null) {
            this.col3 = d.col3;
        }
        if (d.col4 != null) {
            this.col4 = d.col4;
        }
        if (d.col5 != null) {
            this.col5 = d.col5;
        }
        if (d.col6 != null) {
            this.col6 = d.col6;
        }
        if (d.col7 != null) {
            this.col7 = d.col7;
        }
        if (d.col8 != null) {
            this.col8 = d.col8;
        }
        if (d.col9 != null) {
            this.col9 = d.col9;
        }
        if (d.col10 != null) {
            this.col10 = d.col10;
        }
        if (d.col11 != null) {
            this.col11 = d.col11;
        }
        if (d.col12 != null) {
            this.col12 = d.col12;
        }
        if (d.col13 != null) {
            this.col13 = d.col13;
        }
        if (d.col14 != null) {
            this.col14 = d.col14;
        }
        if (d.col15 != null) {
            this.col15 = d.col15;
        }
        if (d.col16 != null) {
            this.col16 = d.col16;
        }
        if (d.col17 != null) {
            this.col17 = d.col17;
        }
        if (d.col18 != null) {
            this.col18 = d.col18;
        }
        if (d.col19 != null) {
            this.col19 = d.col19;
        }
        if (d.col20 != null) {
            this.col20 = d.col20;
        }
        if (d.col21 != null) {
            this.col21 = d.col21;
        }
        if (d.col22 != null) {
            this.col22 = d.col22;
        }
        if (d.col23 != null) {
            this.col23 = d.col23;
        }
        if (d.col24 != null) {
            this.col24 = d.col24;
        }
        if (d.col25 != null) {
            this.col25 = d.col25;
        }
        if (d.col26 != null) {
            this.col26 = d.col26;
        }
        if (d.col27 != null) {
            this.col27 = d.col27;
        }
        if (d.col28 != null) {
            this.col28 = d.col28;
        }
        if (d.col29 != null) {
            this.col29 = d.col29;
        }
        if (d.col30 != null) {
            this.col30 = d.col30;
        }
        return true;
    }
}
