package traductor;
import java.io.ByteArrayOutputStream;
//import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import antlr.ANTLRException;
//import antlr.CommonAST;
//import antlr.ParseTree;
//import antlr.Token;
//import antlr.TokenStream;
//import antlr.TokenStreamException;
//import antlr.collections.AST;


public class Principal {//solo scanner
//private static String fileName = "prueba1.txt";
private static FileInputStream fis = null;

	public static void main(String args[]) {
		try
		{
			
			
			ByteArrayOutputStream nuestroError = new ByteArrayOutputStream();
			System.setErr(new PrintStream(nuestroError));
			System.out.println("Scanning file...");
			fis = new FileInputStream("Para revisar.txt");
			MiLexer scan = new MiLexer(fis);
			/*
			Token token = scan.nextToken();
			while(token.getType() != Token.EOF_TYPE){
				System.out.println(token);
				token = scan.nextToken();
			}
			*/
			MiParser par = new MiParser(scan);
			par.sprog();
			System.out.println(scan.erroresLexicos);
//			CommonAST a = (CommonAST)par.getAST();
			String errorTrat="";
			if (nuestroError.toString().length()!=0){
				String[] listaString = nuestroError.toString().split(" ");
				String loc = listaString [1];
				String simbolo = listaString [4].split("\n")[0];
				String linea = loc.split(":")[0];
				String columna = loc.split(":")[1];
				errorTrat="Error sintactico en linea: "+ linea+" columna: "+ columna+" simbolo: "+ simbolo+ "\n";
				System.out.println(errorTrat);
			}
			par.errorSintactico=errorTrat;
			
			System.out.println(par.codigoGenerado);
			//System.out.println("Resultado ASA: "+a.toStringList());
		}catch (ANTLRException ae){
			System.err.println(ae.getMessage() + "y aqui intervenimos");
		}
		catch(FileNotFoundException fnfe){
			System.err.println("No se encontró el fichero");
		}

	}
}