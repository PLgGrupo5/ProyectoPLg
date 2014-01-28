package traductor;
import java.util.HashMap;
//import antlr.ANTLRException;

public class TablaSimbolos {

	private HashMap<String,Linea> tabla;
	public TablaSimbolos(){
		this.tabla  = new HashMap<String,Linea>();
	}
	public Linea getLinea(String key)
	{
		Linea linea;	
		if (this.tabla.containsKey(key))
			linea = this.tabla.get(key);
		else linea=null;
		return linea;
	}
	public String getTipo(String id)
	{
		return this.getLinea(id).getTipo();
	}
	
	public String tipoResultante(String tipo1,String tipo2,int nOpers, String operador)
	{
		String terr="error";
		if ((tipo1==terr)||(tipo2==terr))return terr;
		else
		{
			if(nOpers==1)
			{//operaci�n unaria
				switch(operador)
				{
					case "in":
					case "out":
					case "-":	
					case ")"://en este caso lo que se est� recibiendo por tipo1 el tipo del operando, 
							//que marca el tipo de salida (int) o (real)	
					    return tipo1;
					case "!":
						if (tipo1=="real")return terr;
						else return tipo1;
				}
					
			}
			if(nOpers==2)
			{//operaci�n binaria
				switch(operador)
				{
					case "*":
					case "/":
					case "-":
					case "+":
						if((tipo1=="real")||tipo2=="real")return "real";
						else return "entero"; 
					case "<":
					case ">":	
					case "<=":	
					case ">=":	
					case "==":	
					case "!=":	
						return "entero"; 
										
					case "||":
					case "&&":
					case "%" :
						if((tipo1=="real")||tipo2=="real")return terr;
						else return "entero";
					case "=" :
						if ((tipo1=="entero") && (tipo2=="real"))return terr;
						return tipo1;
				
						
				}
			}	
			return terr;
		}
	}
	
	public boolean isID(String id)
	{
		return tabla.containsKey(id.toLowerCase());
	}
	
	public void insertaDec( Declaracion dec)// throws ANTLRException
	{
		if (!tabla.containsKey(dec.getNombre().toLowerCase()))
		{
			Linea nuevaLinea = new Linea (dec);
			nuevaLinea.setNombre(dec.getNombre().toLowerCase());
			nuevaLinea.setTipo(dec.getTipo());
			nuevaLinea.setValorDouble(0.0);
			nuevaLinea.setValorInt(0);
			nuevaLinea.setDirMemoria(this.tabla.size());
			this.tabla.put(nuevaLinea.getNombre(), nuevaLinea);
			//System.out.println(nuevaLinea.toString());
		}
		/*else
		{
			ANTLRException e = new ANTLRException("Variable ya declarada!!");
			throw e;
		}*/
	}
	public float devuelveValor()
	{
		return 0;
	}
	public String devuelveTipo()
	{
		return "";
	}
}
