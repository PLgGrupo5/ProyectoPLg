package traductor;
public class Declaracion {

	String tipo;
	String nombre;
	int linea=0;
	int columna=0;

	
	public Declaracion ()
	{
		this.tipo = "";
		this.nombre = "";
	}
	public Declaracion (String tipo, String nombre)
	{
		this.tipo = tipo;
		this.nombre = nombre;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public int getLinea() {
		return this.linea;
	}
	public void setLinea(int linea) {
		this.linea = linea;
	}
	public int getColumna() {
		return this.columna;
	}
	public void setColumna(int columna) {
		this.columna = columna;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
