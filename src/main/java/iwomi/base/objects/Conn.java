package iwomi.base.objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "conn")
@Entity(name = "conn")

public class Conn {
	@Id
	private String conn;

	public String getConn() {
		return conn;
	}

	public void setConn(String conn) {
		this.conn = conn;
	}
	
}
