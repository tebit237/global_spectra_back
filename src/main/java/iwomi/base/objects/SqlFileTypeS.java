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
@Entity(name = "ssqltype")
@Table(name = "ssqltype")
@TableGenerator(name = "tab", initialValue = 140, allocationSize = 50)
public class SqlFileTypeS {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
        }
        return s;
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

    public SqlFileTypeS(Long id, String etab, String col1, String col2, String col3, String col4, String col5,
            String col6, String col7, String col8, String col9, String col10, String col11, String col12, String col13,
            String col14, String col15, String col16, String col17, String col18, String col19, String col20, Long dele,
            String fichi, Date dar) {
        super();
        this.id = id;
        this.etab = etab;
        this.col1 = col1;
        this.col2 = col2;
        this.col3 = col3;
        this.col4 = col4;
        this.col5 = col5;
        this.col6 = col6;
        this.col7 = col7;
        this.col8 = col8;
        this.col9 = col9;
        this.col10 = col10;
        this.col11 = col11;
        this.col12 = col12;
        this.col13 = col13;
        this.col14 = col14;
        this.col15 = col15;
        this.col16 = col16;
        this.col17 = col17;
        this.col18 = col18;
        this.col19 = col19;
        this.col20 = col20;
        this.fichi = fichi;
        this.dar = dar;
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

    public SqlFileTypeS() {
    }

    public Boolean setByColumn1(SqlFileTypeS d) {
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
        return true;
    }
}
