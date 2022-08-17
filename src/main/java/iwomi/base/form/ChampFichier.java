
package iwomi.base.form;

public class ChampFichier {
	private long id;
	private  String ch;
	public ChampFichier(long id, String ch) {
		super();
		this.id = id;
		this.ch = ch;
	}
	public ChampFichier() {
		
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCh() {
		return ch;
	}
	public void setCh(String ch) {
		this.ch = ch;
	}
}