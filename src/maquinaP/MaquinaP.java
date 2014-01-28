package maquinaP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
//import java.util.StringTokenizer;

public class MaquinaP {

    private String _dirFichEntrada = null;
    private String _dirFichSalida = null;

    private List<String> _lineasEntrada = null;
    private List<String> _lineasSalida = null;

    private int _PC = 0;
    private LinkedList<Number> _pila = null;
    private Number[] _memoriaDatos = null;

    private boolean _finPrograma = false;
    private String _ultimaOperacion = "";
    private String _buferSalida = "";
    private int _errorEjecucion = 0;
    private String _buferError = "";

    private boolean _visualizaPasos = false;

    public MaquinaP(String dirFichEntrada, String dirFichSalida) {
        this._dirFichEntrada = dirFichEntrada;
        this._dirFichSalida = dirFichSalida;
    }

    public void visualizaPasos(boolean b) {
        _visualizaPasos = b;
    }

    public int damePC() {
        return _PC;
    }
    
    public String imprimePC() {
        return "PC: " + _PC;
    }

    public Number[] damePila() {
        Number[] pila = new Number[_pila.size()];
        pila = _pila.toArray(pila);
        return pila;
    }

    public String imprimePila() {
    	return _pila.toString();
       /* String str = "";
        str += "Pila: ";
        for (int i = 0; i < _pila.length; i++) {
            str += _pila[i] + "    ";
        }
        str += "\n";
        return str;*/
    }

    public Number[] dameMemoriaDatos() {
        Number[] memoria = _memoriaDatos;
        return memoria;
    }

    public String imprimeMemoriaDatos() {
        String str = "";
        str += "Mem: ";
        for (int i = 0; i < _memoriaDatos.length; i++) {
            str += _memoriaDatos[i] + "    ";
        }
        str += "\n";
        return str;
    }

    public boolean dimeSiFin(){
        System.out.print("Programa finalizado!!");
        return _finPrograma;
    }

    public String dimeUltimaOperacion() {
        System.out.print("Oper: ");
        System.out.println(_ultimaOperacion);
        return _ultimaOperacion;
    }

    public String imprimeBuferSalida() {
        System.out.println(_buferSalida);
        return _buferSalida;
    }

    public int dimeErrorEjecucion() {
        System.out.println(_errorEjecucion);
        return _errorEjecucion;
    }

    public String imprimeBuferError() {
        System.out.println(_buferError);
        return _buferError;
    }

    private void inicializa() {
        _PC = 0;
        if (_pila != null) {
            _pila.clear();
        } else {
            _pila = new LinkedList<Number>();
        }
        if (_memoriaDatos == null) {
            _memoriaDatos = new Number[33];
        } else {
            for (int i = 0; i < _memoriaDatos.length; i++) {
                _memoriaDatos[i] = null;
            }
        }
        _finPrograma = false;
        _ultimaOperacion = "";
        _buferSalida = "";
        _buferError = "";
        _errorEjecucion = 0;
    }

    public int hazTodo() {
        File archivo = null;
        FileReader ficheroLectura = null;
        BufferedReader bufferLectura = null;
        FileWriter ficheroEscritura = null;
        PrintWriter pw = null;
        try {

            System.out.println("Leyendo de " + _dirFichEntrada + " . . .");
            archivo = new File (_dirFichEntrada);
            ficheroLectura = new FileReader (archivo);
            bufferLectura = new BufferedReader(ficheroLectura);
            String linea;
            _lineasEntrada = new ArrayList<String>();
            while((linea = bufferLectura.readLine()) != null) {
                _lineasEntrada.add(linea);
            }
        } catch(Exception e1) {
            e1.printStackTrace();
        } finally{
            try{
                if(ficheroLectura != null) {
                    ficheroLectura.close();
                }
           }catch (Exception e2) {
                e2.printStackTrace();
           }
        }

        inicializa();
        System.out.println("EJECUTANDO . . .");
        int error = ejecuta();
        if (error != 0) {
            return error;
        }

        try {
            System.out.println("Escribiendo en " + _dirFichSalida + " . . .");
            ficheroEscritura = new FileWriter(_dirFichSalida);
            pw = new PrintWriter(ficheroEscritura);
            for (int i = 0; i < _lineasSalida.size(); i++) {
                pw.println(_lineasSalida.get(i));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (ficheroEscritura != null)
                    ficheroEscritura.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return 0;
     }

    public int ejecuta() {
        _lineasSalida = new ArrayList<String>();

        System.out.println(" -------------------------------");
        System.out.println("| Estado inicial de la maquinaP |");
        System.out.println(" -------------------------------");
        dimeUltimaOperacion();
        imprimePC();
        dameMemoriaDatos();
        imprimePila();

        for (int i = 0; i < _lineasEntrada.size(); i++) {
            if (_finPrograma) {
                break;
            }
            int error = ejecutaOperacion(_lineasEntrada.get(i));
            if (error != 0){
                return error;
            }
            if (_visualizaPasos == true) {
                System.out.println("##########################################################################");
                dimeUltimaOperacion();
                imprimePC();
                dameMemoriaDatos();
                imprimePila();
            }
        }
        System.out.println("##########################################################################");

        System.out.println(" -----------------------------");
        System.out.println("| Estado final de la maquinaP |");
        System.out.println(" -----------------------------");
        dimeUltimaOperacion();
        imprimePC();
        dameMemoriaDatos();
        imprimePila();

        return 0;
    }

    public int ejecutaOperacion(String linea) {
        if (_errorEjecucion != 0) {
            return _errorEjecucion;
        }
        if (_finPrograma) {
            return 0;
        }
        _PC++;
        String lineaMod = linea.replaceAll("\\(|\\)", " ");
        String[] tokens = lineaMod.split(" ");
        String operacion = tokens[0];

        if (operacion.equals("desapila_entrada")) {
            op_desapila_entrada();
        } else if (operacion.equals("apila_entrada")) {
            if (tokens[1].toString().contains(".")) {
                Number numDec = Double.valueOf(tokens[1]);
                op_apila_entrada(numDec);
            } else {
                Number numEnt = Integer.valueOf(tokens[1]);
                op_apila_entrada(numEnt);
            }
        } else if (operacion.equals("desapila_dir")) {
            int dir = Integer.parseInt(tokens[1]);
            op_desapila_dir(dir);
        } else if (operacion.equals("apila_dir")) {
            int dir = Integer.parseInt(tokens[1]);
            op_apila_dir(dir);
        } else if (operacion.equals("menor_que")) {
            op_menor_que();
        } else if (operacion.equals("mayor_que")) {
            op_mayor_que();
        } else if (operacion.equals("menor_o_igual")) {
            op_menor_o_igual();
        } else if (operacion.equals("mayor_o_igual")) {
            op_mayor_o_igual();
        } else if (operacion.equals("igual")) {
            op_igual();
        } else if (operacion.equals("distinto")) {
            op_distinto();
        } else if (operacion.equals("suma")) {
            op_suma();
        } else if (operacion.equals("resta")) {
            op_resta();
        } else if (operacion.equals("or")) {
            op_or();
        } else if (operacion.equals("multiplicacion")) {
            op_multiplicacion();
        } else if (operacion.equals("division")) {
            int error = op_division();
            if (error != 0){
                return error;
            }
        } else if (operacion.equals("and")) {
            op_and();
        } else if (operacion.equals("modulo")) {
            int error = op_modulo();
            if (error != 0){
                return error;
            }
        } else if (operacion.equals("invierte")) {
            op_invierte();
        } else if (operacion.equals("not")) {
            op_not();
        } else if (operacion.equals("lectura")) {
            op_lectura();
        } else if (operacion.equals("escritura")) {
            op_escritura();
        } else if (operacion.equals("stop")) {
            op_stop();
        } else {
            _errorEjecucion = 3;
            _buferError = "[Error de ejecucion] :: Operacion Desconocida";
            System.out.println(_buferError);
            return _errorEjecucion;
        }
        _ultimaOperacion = operacion;
        return 0;
    }

    private void op_desapila_entrada() {
        _pila.pop();
    }

    private void op_apila_entrada(Number n) {
        _pila.push(n);
    }

    private void op_desapila_dir(int dir) {
        _memoriaDatos[dir] = _pila.pop();
    }

    private void op_apila_dir(int dir) {
        _pila.push(_memoriaDatos[dir]);
    }

    private void op_menor_que() {
        Number r;
        Number n2 =  _pila.pop();
        Number n1 = _pila.pop();
        if (n1 instanceof Integer && n2 instanceof Integer) {
            r = (n1.intValue() < n2.intValue()) ? new Integer(1) : new Integer(0);
        } else {
            r = (n1.doubleValue() < n2.doubleValue()) ? new Integer(1) : new Integer(0);
        }
        _pila.push(r);
    }

    private void op_mayor_que() {
        Number r;
        Number n2 =  _pila.pop();
        Number n1 = _pila.pop();
        if (n1 instanceof Integer && n2 instanceof Integer) {
            r = (n1.intValue() > n2.intValue()) ? new Integer(1) : new Integer(0);
        } else {
            r = (n1.doubleValue() > n2.doubleValue()) ? new Integer(1) : new Integer(0);
        }
        _pila.push(r);
    }

    private void op_menor_o_igual() {
        Number r;
        Number n2 =  _pila.pop();
        Number n1 = _pila.pop();
        if (n1 instanceof Integer && n2 instanceof Integer) {
            r = (n1.intValue() <= n2.intValue()) ? new Integer(1) : new Integer(0);
        } else {
            r = (n1.doubleValue() <= n2.doubleValue()) ? new Integer(1) : new Integer(0);
        }
        _pila.push(r);
    }

    private void op_mayor_o_igual() {
        Number r;
        Number n2 =  _pila.pop();
        Number n1 = _pila.pop();
        if (n1 instanceof Integer && n2 instanceof Integer) {
            r = (n1.intValue() >= n2.intValue()) ? new Integer(1) : new Integer(0);
        } else {
            r = (n1.doubleValue() >= n2.doubleValue()) ? new Integer(1) : new Integer(0);
        }
        _pila.push(r);
    }

    private void op_igual() {
        Number r;
        Number n2 =  _pila.pop();
        Number n1 = _pila.pop();
        if (n1 instanceof Integer && n2 instanceof Integer) {
            r = (n1.intValue() == n2.intValue()) ? new Integer(1) : new Integer(0);
        } else {
            r = (n1.doubleValue() == n2.doubleValue()) ? new Integer(1) : new Integer(0);
        }
        _pila.push(r);
    }

    private void op_distinto() {
        Number r;
        Number n2 =  _pila.pop();
        Number n1 = _pila.pop();
        if (n1 instanceof Integer && n2 instanceof Integer) {
            r = (n1.intValue() != n2.intValue()) ? new Integer(1) : new Integer(0);
        } else {
            r = (n1.doubleValue() != n2.doubleValue()) ? new Integer(1) : new Integer(0);
        }
        _pila.push(r);
    }

    private void op_suma() {
        Number r;
        Number n2 = _pila.pop();
        Number n1 = _pila.pop();
       if (n1 instanceof Integer && n2 instanceof Integer) {
            r = new Integer(n1.intValue() + n2.intValue());
        } else {
            r = new Double(n1.doubleValue() + n2.doubleValue());
        }
        _pila.push(r);
    }

    private void op_resta() {
        Number r;
        Number n2 =  _pila.pop();
        Number n1 = _pila.pop();
        if (n1 instanceof Integer && n2 instanceof Integer) {
            r = new Integer(n1.intValue() - n2.intValue());
        } else {
            r = new Double(n1.doubleValue() - n2.doubleValue());
        }
        _pila.push(r);
    }

    private void op_or() {
        Number r;
        Number n2 =  _pila.pop();
        Number n1 = _pila.pop();
        r = (n1.intValue() != 0 || n2.intValue() != 0) ? new Integer(1) : new Integer(0);
        _pila.push(r);
    }

    private void op_multiplicacion() {
        Number r;
        Number n2 =  _pila.pop();
        Number n1 = _pila.pop();
        if (n1 instanceof Integer && n2 instanceof Integer) {
            r = new Integer(n1.intValue() * n2.intValue());
        } else {
            r = new Double(n1.doubleValue() * n2.doubleValue());
        }
        _pila.push(r);
    }

    private int op_division() {
        Number r;
        Number n2 =  _pila.pop();
        Number n1 = _pila.pop();
        if (n2.doubleValue() == 0.0) {
            _errorEjecucion = 1;
            _buferError = "[Error de ejecucion] :: División por cero";
            System.out.println(_buferError);
            return _errorEjecucion;
        }
        if (n1 instanceof Integer && n2 instanceof Integer) {
            r = new Integer(n1.intValue() / n2.intValue());
        } else {
            r = new Double(n1.doubleValue() / n2.doubleValue());
        }
        _pila.push(r);
        return 0;
    }

    private void op_and() {
        Number r;
        Number n2 =  _pila.pop();
        Number n1 = _pila.pop();
        r = (n1.intValue() != 0 && n2.intValue() != 0) ? new Integer(1) : new Integer(0);
        _pila.push(r);
    }

    private int op_modulo() {
        Number r;
        Number n2 =  _pila.pop();
        Number n1 = _pila.pop();
         if (n2.intValue() <= 0) {
            _errorEjecucion = 2;
            _buferError = "[Error de ejecucion] :: Modulo 'n', con 'n' menor o igual a cero";
            System.out.println(_buferError);
            return _errorEjecucion;
        }
        r = new Integer(n1.intValue() % n2.intValue());
        _pila.push(r);
        return 0;
    }

    private void op_invierte() {
        Number r;
        Number n =  _pila.pop();
        if (n instanceof Integer) {
            r = new Integer(n.intValue() * (-1));
        } else {
            r = new Double(n.doubleValue() * (-1));
        }
        _pila.push(r);
    }

    private void op_not() {
        Number r;
        Number n = _pila.pop();
        r = (n.intValue() != 0) ? new Integer(0) : new Integer(1);
        _pila.push(r);
    }

    private void op_lectura() {
        try{
            Number r;

            BufferedReader bufLec;
            String cad = null;
            bufLec = new BufferedReader(new InputStreamReader(System.in));
            boolean entVal = false;
            while (!entVal) {
                System.out.print("Introduzca un numero Entero o Real: ");
                cad = bufLec.readLine();
                if (cad.matches("0") | cad.matches("[1-9][0-9]*") | cad.matches("0\\.0") | cad.matches("0\\.[0-9]*[1-9]") | cad.matches("[1-9][0-9]*\\.0") | cad.matches("[1-9][0-9]*\\.[0-9]*[1-9]")) {
                    entVal = true;
                } else {
                    System.out.println("Entrada no valida!");
                }
            }
            System.out.println(cad);
            if (cad.toString().contains(".")) {
                r = Double.valueOf(cad);
            } else {
                r = Integer.valueOf(cad);
            }
            _pila.push(r);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void op_escritura() {
        Number n = _pila.pop();
        System.out.println(n);
        _buferSalida = n.toString();
    }

    private void op_stop() {
        _finPrograma = true;
    }

    public static void main(String args[]) {
        MaquinaP maq = new MaquinaP("ent.txt", "sal.txt");
        maq.visualizaPasos(true);
        maq.hazTodo();

    }
}
