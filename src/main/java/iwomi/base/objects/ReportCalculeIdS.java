package iwomi.base.objects;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReportCalculeIdS implements Serializable {
    
	private String fichi;
	private String field;
	private String post;
	private String col;
	private String tysorce;
        
    
    public ReportCalculeIdS() {

    }

    public String getFichi() {
        return fichi;
    }

    public String getField() {
        return field;
    }

    public String getPost() {
        return post;
    }

    public String getCol() {
        return col;
    }

    public String getTysorce() {
        return tysorce;
    }

    public void setFichi(String fichi) {
        this.fichi = fichi;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public void setTysorce(String tysorce) {
        this.tysorce = tysorce;
    }


    

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReportCalculeIdS reportCalculeIdS = (ReportCalculeIdS) o;
        return fichi.equals(reportCalculeIdS.fichi) && col.equals(reportCalculeIdS.col) && field.equals(reportCalculeIdS.field)
                && post.equals(reportCalculeIdS.post) && col.equals(reportCalculeIdS.col)&& tysorce.equals(reportCalculeIdS.tysorce);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fichi,field,post,col,tysorce);
    }
}
