package traductor;
public class Traductor  {
	private String err="";
	private String tipo="null";
	private String cod="";
	private String oper="";
	
	
	public String getErr() {
		return err;
	}
	
	public Traductor clone()
	{
		Traductor dev=new Traductor();
		dev.setCod(this.getCod());
		dev.setErr(this.getErr());
		dev.setTipo(this.getTipo());
		dev.setOper(this.getOper());
		return dev;
	}
	public void setErr(String err) {
		this.err = err;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getOper() {
		return this.oper;
	}
	public void setOper(String oper) {
		this.oper = oper;
	}
	public String getCod() {
		return cod;
	}
	public void setCod(String cod) {
		this.cod = cod;
	}
}
