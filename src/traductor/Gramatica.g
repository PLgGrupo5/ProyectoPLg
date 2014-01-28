header {
	package traductor;
}
class MiParser extends Parser;
options {
    k=2;/*buildAST=true;*/
}

tokens{
ENTERO ;
REAL;
ID;
Whitespace;
DELIM_PUNTOCOMA;
DELIM_PAREN_A;
DELIM_PAREN_C;
OP_IGUAL;
OP_AS;
OP_MAS;
OP_MENOS;
OP_MOD;
OP_MUL_DIV;
OP_OR ;
OP_AND;
OP_NOT;
OP_COMP;
OP_IN;
OP_OUT;
FIN ;
ASIG_IGUAL;
INT_O_REAL;
COMP1_2;
NOT_COMP;
}

{String errDecs="";
String errorSemantico="";
String codigoGenerado="";
String errorSintactico="";}

sprog  returns [Traductor cod= new Traductor();]/*{Traductor cod;}*/:
					cod=prog
					{
						errorSemantico=cod.getErr();
						codigoGenerado=cod.getCod();
						//System.out.println(cod.getCod());
						System.out.println(cod.getErr());
					}
					FIN
					
				
					;
prog returns [Traductor cod=new Traductor();]{TablaSimbolos TBS;}:
					TBS=decs
					cod=accs[TBS]
					{cod.setErr(errDecs+cod.getErr());}
					
		
				
					
					;

//====================================================
//DECLARACIONES
//====================================================

decs returns [TablaSimbolos TB = new TablaSimbolos();]
			{Declaracion dec1; TablaSimbolos TBS;}:
					dec1=dec
					TBS=rdecs
					{
						if(!TBS.isID(dec1.getNombre()))
							TBS.insertaDec(dec1);
						else
							errDecs="ERROR Lin:"+
								dec1.getLinea()
								+", Col: "+
								dec1.getColumna()
								+"--Identificador '"
								+
								dec1.getNombre()
								+"' duplicado.\n"+errDecs;							
							
						TB=TBS;
					}
					
					|
					{	
	  						TB= new TablaSimbolos();
	  				}
					
        			;
	 					
rdecs returns [TablaSimbolos TBS = new TablaSimbolos();]:
					SEP
					TBS=rrdecs
					
        			;
rrdecs returns [TablaSimbolos TBS = new TablaSimbolos();]:
					TBS=decs
					
        			;

dec returns [Declaracion deca = new Declaracion();]{Traductor nombreTipo; String nombreVar;}:
					nombreTipo=tipo
					ident:ID
					{
						nombreVar = ident.getText();
						//System.out.println(nombreVar);
						Declaracion decla = new Declaracion (nombreTipo.getTipo(), nombreVar);
						decla.setLinea(ident.getLine());
						decla.setColumna(ident.getColumn());
						deca=decla;
					}
				
        			;




//====================================================
//ACCIONES
//====================================================

accs [TablaSimbolos TBh] returns [Traductor cod=new Traductor();]{Traductor cod1, cod2;}:
					cod1=acc[TBh]
					cod2=racs[TBh] 
					
					
					
					{	
						cod=cod1.clone();
						cod.setCod(cod.getCod() + cod2.getCod() + "\n");
						cod.setErr(cod.getErr()+cod2.getErr());
											
					}
				
					
					
					
        			;

racs [TablaSimbolos TBh] returns [Traductor cod = new Traductor();] {Traductor cod1; String desap;}:
					SEP
					cod =rraccs[TBh]
					{	
						desap="desapila_valor()\n";
						cod.setCod( desap + cod.getCod());
					}
				
					
				
        			;


rraccs [TablaSimbolos TBh]returns [Traductor cod=new Traductor();]:
					cod=accs[TBh]
					
					|{cod=new Traductor();}
				
        			;

acc [TablaSimbolos TBh] returns [Traductor cod=new Traductor();] :
					cod=in[TBh] 
	 				|cod=out[TBh]
     				|cod=exp[TBh]
     				
     				;


in [TablaSimbolos TBh] returns [Traductor cod = new Traductor();]{Linea linea;}:
					OP_IN
					id:ID
					{
									cod=new Traductor();
									if(!TBh.isID(id.getText()))
									{
										cod.setErr(cod.getErr()+"ERROR Lin: "+id.getLine()+", Col: "+id.getColumn()+"--Identificador desconocido\n");
										cod.setTipo("error");
									}
									else
									{
										linea = TBh.getLinea(id.getText().toLowerCase());
										cod.setCod("lectura()\n");
										cod.setCod(cod.getCod()+"desapila_dir("+linea.getDirMemoria()+")\n");
										cod.setCod(cod.getCod()+"apila_dir("+linea.getDirMemoria()+")\n");
									}	
								}
					
        			;
out[TablaSimbolos TBh]returns [Traductor cod = new Traductor();]{Traductor cod1;}:
					OP_OUT
					cod=exp[TBh]
					{
						//cod=cod1.clone();
						cod.setCod(cod.getCod() + "escritura()\n");
					}
					
					;



exp[TablaSimbolos TBh] returns [Traductor cod=new Traductor();]{Traductor cod1;Linea linea=null;String tipoLinea="";int direccion=0;} :
					ident:ID
					OP_AS
					cod1=accasign[TBh]
					
					{
						if (cod1 != null)
						{
								cod=cod1.clone();
							if(!TBh.isID(ident.getText()))
							{
								cod.setErr(cod.getErr()+"ERROR L:"+ident.getLine()+", C:"+ident.getColumn()+"--Identificador desconocido.\n");
								cod.setTipo("error");
																										
							}
							else
							{										
								linea=TBh.getLinea(ident.getText().toLowerCase());
//										
							}	
								if(linea==null)
								{
									tipoLinea="error";
									direccion=0x0;
								}
								else
								{
									tipoLinea=linea.getTipo();
									direccion=linea.getDirMemoria();
									//System.out.println("dir:  ->  "+direccion);
									
								}
								//cod.setCod(cod1.getCod()+"desapila_dir("+direccion+")\n");//antigua
								cod.setCod(cod.getCod()+"desapila_dir("+direccion+")\n");//nueva
								cod.setCod(cod.getCod()+"apila_dir("+direccion+")\n");
								

								
								cod.setTipo(TBh.tipoResultante(tipoLinea,cod1.getTipo(),2,"="));
									
								if(cod.getTipo()=="error")
									cod.setErr(cod.getErr()+"ERROR L:"+ident.getLine()+", C:"+ident.getColumn()+"--Tipo incompatible para la asignación.\n");
							
						}
						
					}

					|cod=acccomp[TBh]

        			;

accasign [TablaSimbolos TBh]returns [Traductor cod=new Traductor();]:
					cod=exp[TBh]
		
        			;

acccomp[TablaSimbolos TBh]returns [Traductor cod=new Traductor();]{Traductor cod1;}:
					cod1=accadit[TBh]
					cod=racccomp[TBh,cod1]
		
        			;

racccomp[TablaSimbolos TBh, Traductor codh] returns [Traductor cod=new Traductor();]{String op,oper=">";Traductor cod1;int linea=0,columna=0;}:
				/*	(opc:OP_COMP|opi:OP_IGUAL|opc1:OP_COMP1|opc2:OP_COMP2)*/
				(opc:OP_COMP|opi:OP_IGUAL|opmayq:OP_MAYQ|opmenq:OP_MENQ|opmayoi:OP_MAYOI|opmenoi:OP_MENOI)
					
					
					cod1=accadit[TBh]
					{
						op="";
						if (opi!=null)
						{
							op="igual()\n";
							linea=opi.getLine();
							columna=opi.getColumn();
						}	
						if (opc!=null)
						{
							op="distinto()\n";
							linea=opc.getLine();
							columna=opc.getColumn();
						}
						if (opmayq!=null)
						{
							op="mayor_que()\n";
							linea=opmayq.getLine();
							columna=opmayq.getColumn();
						}
						if(opmenq!=null)
						{
							op="menor_que()\n";
							linea=opmenq.getLine();
							columna=opmenq.getColumn();
						}
						if(opmayoi!=null)
						{
							op="mayor_o_igual()\n";
							linea=opmayoi.getLine();
							columna=opmayoi.getColumn();
						}
						if(opmenoi!=null)
						{
							op="menor_o_igual()\n";
							linea=opmenoi.getLine();
							columna=opmenoi.getColumn();
						}
					
										
							
						cod=codh.clone();
						
					//	System.out.println("tipo: "+codh.getTipo());
					//	System.out.println("tipo: "+cod1.getTipo());
					//	System.out.println("oper: "+oper);
						
						
						
						cod.setTipo(TBh.tipoResultante(codh.getTipo(),cod1.getTipo(),2,oper));
						if(cod.getTipo()=="error")cod.setErr(cod.getErr()+"ERROR L:"+linea+", C:"+columna+"--Tipo incompatible para la expresión.\n");
						cod.setCod(cod.getCod()+cod1.getCod()+op);
						;
					}
					
					
					
					
					
					
					
					
		  			| {cod=codh;}
			
        			;
			
accadit[TablaSimbolos TBh]returns [Traductor cod=new Traductor();]{Traductor cod1;}:
					cod1=accmult[TBh]
					cod=raccadit[TBh,cod1]
				
        			;
raccadit[TablaSimbolos TBh, Traductor codh]returns [Traductor cod=new Traductor();]{Traductor cod1,cod2;String op="", oper="+";int linea=0,columna=0;}:
					(opma:OP_MAS|opme:OP_MENOS|opor:OP_OR)
					cod1=accmult[TBh]
					{
						if (opma!=null)
						{
							linea=opma.getLine();
							columna=opma.getColumn();
							op="suma()\n";
						}
						if (opme!=null)
						{
							linea=opme.getLine();
							columna=opme.getColumn();
							op="resta()\n";
						}
						if (opor!=null)
						{
							linea=opor.getLine();
							columna=opor.getColumn();
							op="or()\n";
							oper="||";
						}
						cod2=codh.clone();	
						cod2.setTipo(TBh.tipoResultante(codh.getTipo(),cod1.getTipo(),2,oper));
						if(cod2.getTipo()=="error")cod2.setErr(cod2.getErr()+cod1.getErr()+"ERROR L:"+linea+", C:"+columna+"--Tipo incompatible para la operación binaria.\n");
						cod2.setCod(cod2.getCod()+cod1.getCod()+op);
					}
					cod=raccadit[TBh,cod2]
					
				
					
					
					|{cod=codh;}
				
				
			
				
        			;

accmult[TablaSimbolos TBh]returns [Traductor cod=new Traductor();]{Traductor cod1;}:
					cod1=accun[TBh]
					cod=raccmult[TBh,cod1]
					
					
			
					;
raccmult[TablaSimbolos TBh,Traductor codh]returns [Traductor cod=new Traductor();] {Traductor cod1,cod2=new Traductor();String op,oper="";int linea=0,columna=0;}:
					(opmu:OP_MUL| opdiv:OP_DIV|opan:OP_AND|opmo:OP_MOD)
					cod1=accun[TBh]
								{
									op="";
									if (opmu!=null)
									{
										linea=opmu.getLine();
										columna=opmu.getColumn();
										oper="*";
										op="multiplicacion()\n";
									}
									if (opdiv!=null)
									{
										linea=opdiv.getLine();
										columna=opdiv.getColumn();
										op="division()\n";
										oper="/";
									}
									if (opan!=null)
									{
										oper="&&";
										linea=opan.getLine();
										columna=opan.getColumn();
										op="and()\n";
									}
									if (opmo!=null)
									{
										oper="%";
										op="modulo()\n";
										linea=opmo.getLine();
										columna=opmo.getColumn();
									}
									cod2=codh.clone();
									cod2.setTipo(TBh.tipoResultante(codh.getTipo(),cod1.getTipo(),2,oper));
									if(cod2.getTipo()=="error")
										cod2.setErr(cod2.getErr()+cod1.getErr()+"ERROR L:"+linea+", C:"+columna+"--Tipo incompatible para la operación binaria.\n");
									cod2.setCod(codh.getCod()+cod1.getCod()+op);
									
						
								}
					cod=raccmult[TBh,cod2]
					|{cod=codh;}
				
					;

accun[TablaSimbolos TBh] returns [Traductor cod=new Traductor();]{String op,oper="";Traductor cod1,cod2;int linea=0,columna=0;}:
					cod=factor[TBh]
					
					| (opme:OP_MENOS|opno:OP_NOT)
					cod1=factor[TBh]
			 		{
			 			op="";
			 			if (opme!=null)
			 			{
			 				op="invierte()\n";
			 				linea=opme.getLine();
							columna=opme.getColumn();
							oper="-";
			 				
			 			}
			 			else if (opno!=null)
			 				{
			 					op="not()\n";
			 					linea=opno.getLine();
								columna=opno.getColumn();
								oper="!";
			 				}
			 			cod=cod1.clone();	
			 			cod.setCod(cod.getCod() + op);
			 			cod.setTipo(TBh.tipoResultante(cod1.getTipo(),"",1,oper));
						if(cod.getTipo()=="error")cod.setErr(cod.getErr()+"ERROR L:"+linea+", C:"+columna+"--Tipo incompatible para la operación unaria.\n");
			 			
			 		}
					| DELIM_PAREN_A
					cod1=tipo
					DELIM_PAREN_C
					cod2=factor[TBh]
					{
						cod=cod2.clone();
						cod.setCod(cod2.getCod() + "convierte_"+cod1.getCod()+"()\n");
						cod.setTipo(cod1.getTipo());
					}
			
					;
			
factor[TablaSimbolos TBh]returns [Traductor cod=new Traductor();]{Linea linea;}:
					cod=num
					|iden:ID
					{	
						cod=new Traductor();
						if(!TBh.isID(iden.getText()))
						{
							cod.setErr(cod.getErr()+"ERROR L:"+iden.getLine()+", C:"+iden.getColumn()+"--Identificador desconocido.\n");
							cod.setTipo("error");
						}
						else
						{
							linea=TBh.getLinea(iden.getText().toLowerCase());
							cod.setCod("apila_dir("+linea.getDirMemoria()+")\n");
							cod.setTipo(TBh.getTipo(iden.getText()));
						}

					}
					|DELIM_PAREN_A cod=acc[TBh] DELIM_PAREN_C
					
        		
					;

num returns[Traductor cod=new Traductor();]: r:REAL
					{
						cod=new Traductor();
						cod.setCod("apila_valor("+r.getText()+")\n");
						cod.setTipo("real");

					}
					
        			| e:ENTERO
					{
						cod=new Traductor();
						cod.setCod("apila_valor("+e.getText()+")\n");
						cod.setTipo("entero");
					} 
					
					
			
					;

tipo returns [Traductor tipo=new Traductor();]:
					{
						tipo=new Traductor();
						tipo.setTipo("real");
					}TIPOREAL
					|
					{
						tipo=new Traductor();
						tipo.setTipo("entero");
					}TIPOENT
					
				
				
					;



class MiLexer extends Lexer; options {k=2;}
//------------------------- Analizador Lexico ------------------------ 
{
	String erroresLexicos="";
}
protected LETRA :('a'..'z') | ('A'..'Z');
protected DIGITO: '0'..'9';
protected DELIM_PUNTO : '.';
protected ENTERO : ('1'|'2'|'3'|'4'|'5'|'6'|'7'|'8'|'9')(DIGITO)*;
protected REAL : ENTERO (DELIM_PUNTO) (DIGITO)+;
protected TIPOREAL: ('R'|'r')('E'|'e')('A'|'a')('L'|'l');
protected TIPOENT: ('I'|'i')('N'|'n')('T'|'t');
protected OP_IN : ('I'|'i')('N'|'n');
protected OP_OUT : ('O'|'o')('U'|'u')('T'|'t');
protected ID : (LETRA | '_') (((LETRA) | ENTERO | '_' ))*  ( '?')? ;
protected OP_AS : '=';
protected OP_IGUAL: "==";
protected OP_NOT : '!';

protected OP_COMP : "!=";

/*
protected OP_COMP1 : '>'|'<';
protected OP_COMP2 : ">="|"<=";
*/
protected OP_MENQ : '<';
protected OP_MAYQ : '>';
protected OP_MAYOI : ">=";
protected OP_MENOI : "<=";


SALTODELINEA:('\n'|'\r'| "\r\n")
			{
				$setType(Token.SKIP);
				newline();
			}
			;

COMP1: (OP_MAYQ '=')=> OP_MAYOI {$setType(OP_MAYOI);}
               |(OP_MAYQ) => OP_MAYQ {$setType(OP_MAYQ);}
               ;
               
               
COMP2: (OP_MENQ '=')=> OP_MENOI{$setType(OP_MENOI);}
               |(OP_MENQ) => OP_MENQ {$setType(OP_MENQ);}
               ;



ID_TIPO_OPIN_OPOUT:   (OP_IN ('T'|'t') ' ')=> TIPOENT {$setType(TIPOENT);}
				| (OP_IN ('T'|'t') ~' ') => ID {$setType(ID);} 
				| (OP_IN ~('t'|' ')) => ID {$setType(ID);}
				| (OP_IN (' '))=> OP_IN {$setType(OP_IN);}
				| (OP_OUT ' ')=>OP_OUT {$setType(OP_OUT);}
		  		| (OP_OUT ~(' '))=> ID {$setType(ID);}
		  		| (TIPOREAL ' ')=> TIPOREAL {$setType(TIPOREAL);}
				| (ID) => ID {$setType(ID);}
		  ;

COMENTARIO : '@' ( ~('\n'|'\r') )* {$setType(Token.SKIP);};


INT_O_REAL: ( ENTERO (DELIM_PUNTO)) => REAL {$setType(REAL);}
			| ENTERO {$setType (ENTERO);}
			;


//Whitespace :  (' ' | '\r' | '\n' | '\t')+  {$setType(Token.SKIP);};

Whitespace :  (' '  | '\t')+  {$setType(Token.SKIP);};
SEP : ';';

DELIM_PAREN_A : '(';

DELIM_PAREN_C : ')';

OP_MAS : '+' ;

OP_MENOS : '-';

OP_MOD : '%';

//OP_MUL_DIV : '*' | '/';
OP_MUL : '*';
OP_DIV:'/';

OP_OR : "||";

OP_AND : "&&";

ASIG_IGUAL: (OP_AS '=')=> OP_IGUAL {$setType(OP_IGUAL);}
			| (OP_AS) => OP_AS {$setType(OP_AS);}
			; 
			
NOT_COMP: (OP_NOT ('='))=> OP_COMP {$setType(OP_COMP);}
		  | (OP_NOT) => OP_NOT {$setType(OP_NOT);}
		  ;

FIN : "$$";

ERRORES: . {erroresLexicos += "Hemos encontrado un error lexico: " + getText()+ " Linea: " + getLine()+ " Columna: " + getColumn() +"\n";
			$setType(Token.SKIP);};