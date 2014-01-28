package traductor;
public class Linea {
	String nombre;
	String tipo;
	double valorDouble;
	int valorInt;
	int dirMemoria;
	public Linea (Declaracion dec)
	{
		this.nombre = dec.getNombre();
		this.tipo = dec.getTipo();
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public double getValorDouble() {
		return valorDouble;
	}
	public void setValorDouble(double valorDouble) {
		this.valorDouble = valorDouble;
	}
	public int getValorInt() {
		return valorInt;
	}
	public void setValorInt(int valorInt) {
		this.valorInt = valorInt;
	}
	public int getDirMemoria() {
		return dirMemoria;
	}
	public void setDirMemoria(int dirMemoria) {
		this.dirMemoria = dirMemoria;
	}
	@Override
	public String toString() {
		return "Linea [nombre=" + nombre + ", tipo=" + tipo + ", dirMemoria="
				+ dirMemoria + "]";
	}
	
}
