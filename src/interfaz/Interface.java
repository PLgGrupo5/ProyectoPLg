package interfaz;

import java.io.*;
//import java.nio.file.FileSystems;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.Scanner;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFileChooser;

import traductor.*;
//import antlr.ANTLRException;
//import antlr.CommonAST;
//import traductor.*;
import maquinaP.*;

public class Interface extends JFrame{
					
	///////////// Objetos importantes
//	private FileInputStream _fis = null;
	
//	private MiLexer _scan = null;
//	private MiParser _par = null;
	private MaquinaP _maqP = null;
		
		
	//////////// Objetos graficos	
		private JButton buttonCargar;
		private JButton buttonGuardar;
		private JButton buttonCompilar;
		private JButton buttonEjecutar;
		private JButton buttonPasoPaso;
		
		public String ficheroCargado;
		String resultadoCompilacion;
		
		String textoArchivo;
		
		JTextArea taSouth;
		JTextArea taWest;
		JTextArea taEast;
		
		boolean cargadoPrograma;
		boolean cargadaTraduccion;
		
////////////////////////////////////////////////////////////////////////////
	private void compilacion() {
	/*
	 	try
		{
	
	 
			String urlProject = getClass().getClassLoader().getResource(".").getPath();
			urlProject = urlProject + "aux.txt";
            File newTextFile = new File(urlProject);
            FileWriter fw = new FileWriter(newTextFile);
            fw.write(s);
            fw.close();
            
			FileInputStream fis = new FileInputStream(urlProject);
			MiLexer scan = new MiLexer(fis);
			MiParser par = new MiParser(scan);
			Traductor trad = par.sprog();
			resultadoCompilacion = trad.getCod();
			
			Path path = FileSystems.getDefault().getPath(urlProject);
			Files.delete(path);
			
			Console c = System.console();
			System.out.println(c);
			cambiarContenidoTxtAreaTraductor(resultadoCompilacion);
			cambiarContenidoTxtAreaConsola(scan.erroresLexicos);
		
		}catch (ANTLRException ae){
			JOptionPane.showMessageDialog(null,ae.getMessage());
		}catch (NullPointerException ae){
			JOptionPane.showMessageDialog(null,ae.getMessage());
		}catch(FileNotFoundException fnfe){
			JOptionPane.showMessageDialog(null,"No se encontro el fichero");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		
	}
	
	private int ejecucion() {
		return _maqP.ejecuta();
	}
	
	private int ejecucionPaso() {
//		return _maqP.ejecutaOperacion();
		return 0;
	}
////////////////////////////////////////////////////////////////////////////

		private JPanel getPanelBotones(){
			
			JPanel p1 = new JPanel();
	        p1.setLayout(null);
	        
	        buttonCargar = new JButton("Cargar");
	        buttonCargar.setBounds(30,100,200,40);
	        Oyente o = new Oyente();
			buttonCargar.addActionListener(o);
	        p1.add(buttonCargar);
	        
	        buttonGuardar = new JButton("Guardar");
	        buttonGuardar.setBounds(30,160,200,40);
	        Oyente o1 = new Oyente();
			buttonGuardar.addActionListener(o1);
	        p1.add(buttonGuardar);
	        
	        buttonCompilar = new JButton("Compilar");
	        buttonCompilar.setBounds(30,220,200,40);
	        Oyente o2 = new Oyente();
			buttonCompilar.addActionListener(o2);
	        p1.add(buttonCompilar);
	        
	        buttonEjecutar = new JButton("Ejecutar");
	        buttonEjecutar.setBounds(30,280,200,40);
	        Oyente o3 = new Oyente();
			buttonEjecutar.addActionListener(o3);
	        p1.add(buttonEjecutar);
	        
	        buttonPasoPaso = new JButton("Ejecutar paso a paso");
	        buttonPasoPaso.setBounds(30,340,200,40);
	        Oyente o4 = new Oyente();
			buttonPasoPaso.addActionListener(o4);
	        p1.add(buttonPasoPaso);
	
			return p1;
		}
		
		public void inicializarInterfaz(){
			cargadoPrograma = false;
			cargadaTraduccion = false;
			
			this.setTitle("Programa PLG");
			
			JPanel eastPanel = new JPanel();
	        JPanel westPanel = new JPanel();
	        JPanel southPanel = new JPanel();
	        
	        this.getContentPane().setLayout(new BorderLayout());
	        
	        this.getContentPane().add(eastPanel, "East");
	        this.getContentPane().add(getPanelBotones(), BorderLayout.CENTER);
	        this.getContentPane().add(westPanel, "West");
	        this.getContentPane().add(southPanel, "South");
	        
	                
	        taWest = new JTextArea(contentPrograma, 35, 30);
	        taWest.setLineWrap(true);
	        westPanel.add(new JScrollPane(taWest));
	        
	        taEast = new JTextArea(contentTraduccion, 35, 30);
	        taEast.setLineWrap(true);
	        taEast.setEnabled(false);
	        eastPanel.add(new JScrollPane(taEast));
	        
	        taSouth = new JTextArea(contentConsola, 10, 82);
	        taSouth.setLineWrap(true);
	        taSouth.setEnabled(false);
	        southPanel.add(new JScrollPane(taSouth));

	        this.pack();
	        this.setVisible(true);
		}
		
		
		public Interface(){
			inicializarInterfaz();
			
			this.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					System.exit(0);
				}
			});
		}	
		    
		    //Metodo main
		    public static void main(String[] args) {
				Interface interfaces = new Interface();
				interfaces.setVisible(true);
				interfaces.setEnabled(true);
				interfaces.setSize(1000,800);
		    	
		    }
		    
		    static String contentPrograma = "Contenedor del codigo de programa\n"
		    	      + "Aqui se cargara el archivo que contiene el programa,\n" + "O también se podrá escribir un programa\n"
		    	      + "Que cuando pulsemos el boton compilar, en caso de ser correcto y estar bien escrito mostrara el resultado en la vista derecha.";

		    static String contentTraduccion = "Contenedor de la traducción\n"
		    	      + "Si hemos pulsado el boton de compilar y el texto era correcto,\n" + "aparecera aqui l traduccion del codigo\n"
		    	      + "traduccion utilizada para ejecutar el codigo, mediante la ejecucion de las diferentes funciones, en nuestro caso dependientes de una pila";
		    
		    static String contentConsola = "Consola\n"
		    	      + "Aqui escribiremos los valores de resultado\n" + "Asi como errores que pueda haber\n"
		    	      + "incluso las funciones que realiza al compilar y ejecutar.";

	public String inicializarPrograma(File f) throws FileNotFoundException{
		Scanner sc = new Scanner(f);
		textoArchivo = "";
		String textoMostrado = "";
		while(sc.hasNext()){
			String newLine = sc.nextLine();
			textoArchivo = textoArchivo + newLine;
			textoMostrado = textoMostrado + "\n" + newLine;
		}
		textoMostrado = textoMostrado + "\n";
		sc.close();
		return textoMostrado;
	}
	
	public void cambiarContenidoTxtAreaPrograma(String result){
		cargadoPrograma = true;
		taWest.setText(result);
		taEast.setText("");
		taSouth.setText("");
	}
	
	public void cambiarContenidoTxtAreaTraductor(String result){
		cargadaTraduccion = true;
		cargadoPrograma = true;
		taEast.setText(result);
		taSouth.setText("");
	}
	
	public void cambiarContenidoTxtAreaConsola(String result){
		cargadoPrograma = true;
		taSouth.setText(result);
	}
		    
	private class Oyente implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if (buttonCargar == e.getSource()){
				JFileChooser seleccFich = new JFileChooser();
				seleccFich.showOpenDialog(Interface.this); //esta forma de ponerlo lo estipula java
				ficheroCargado = seleccFich.getSelectedFile().getAbsolutePath();
				File file = new File(seleccFich.getSelectedFile().getAbsolutePath());
				try{
					String resultado = inicializarPrograma(file);
					cambiarContenidoTxtAreaPrograma(resultado);
				}catch (FileNotFoundException ex){
					taSouth.setText("ex");
				} 
			}
			else if (buttonGuardar == e.getSource()){
				JOptionPane.showMessageDialog(null,"Solo puedes guardar una vez hayas ejecutado el programa");
			}
			else if (buttonCompilar == e.getSource()){
				//if(cargadoPrograma == true){
				/////////////////////////////////////////
					compilacion();
				//}
				//else{
				//	JOptionPane.showMessageDialog(null,"Necesitas cargar un programa para poder compilar");
				//}
			}
			else if (buttonEjecutar == e.getSource()){
				if(cargadaTraduccion == true){
					String s = resultadoCompilacion;
					String urlProject = getClass().getClassLoader().getResource(".").getPath();
					String urlProjectSalida = getClass().getClassLoader().getResource(".").getPath();
					urlProject = urlProject + "aux.txt";
					urlProjectSalida = urlProjectSalida + "sal.txt";
		            File newTextFile = new File(urlProject);
		            FileWriter fw = null;
					try {
						fw = new FileWriter(newTextFile);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		            try {
						fw.write(s);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		            try {
						fw.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					MaquinaP maq = new MaquinaP(urlProject, urlProjectSalida);
			        maq.visualizaPasos(true);
			        maq.hazTodo();
		      /*      taWest = 
		                    System.out.print("Pila: ");
		            -        this.consola.setText(consola.getText() + "Pila: ");
		      */
				}
				else{
					JOptionPane.showMessageDialog(null,"Necesitas cargar un programa y compilarlo para poder ejecutar");
				}
			}
			else if (buttonPasoPaso == e.getSource()){
				if(cargadaTraduccion == true){
					
				}
				else{
					JOptionPane.showMessageDialog(null,"Necesitas cargar un programa y compilarlo para poder ejecutar");
				}
			}
		}	
	}
}