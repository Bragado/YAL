/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
 
package analisador;
import static analisador.Token.*;

import java.io.StringReader;
import javax.swing.JTextField;

/**
 *
 * @author Felipe
 */
public class Interface  {

    /**
     * Creates new form Inteface
     */
    public static void main(String[] args){
        
	Interface iinterface = new Interface();
	try {
		iinterface.executar("for(int i = 0; i < N; i++){}");
	}catch (Exception e) {
		System.out.println(e.getMessage());
	}
    }
   
    
    public void executar(String expr) throws Exception{
        
       
       
     
      
     
       Lexer lexer = new Lexer(new StringReader(expr));
       
       String resultado="";
       
       while(true){
           Token token = lexer.yylex();
           if(token == null){
               
               System.out.println(resultado);
                return;
           }
                
                
       
               switch(token.getTokenID()){
                
                case ADDSUB_OP:
                    resultado = resultado + "<ADDSUB_OP> " + token.getDate() + "\n";
                    break;
                case RELA_OP:
                    resultado = resultado + "<RELA_OP> "  + token.getDate() + "\n";
                    break;
                case ARITH_OP:
                    resultado = resultado + "<ARITH_OP> "  + token.getDate() + "\n";
                    break;
                case BITWISE_OP:
                    resultado = resultado + "<BITWISE_OP> " + token.getDate() + "\n";
                    break;
                case NOT_OP:
                    resultado = resultado + "<NOT_OP> " + token.getDate() + "\n";
                    break;
                case WHILE:
                    resultado = resultado + "<WHILE> " + token.getDate() + "\n";
                    break;
                case IF:
                    resultado = resultado + "<IF> " + token.getDate() + "\n";
                    break;
                case ELSE:
                    resultado = resultado + "<ELSE> " + token.getDate() + "\n";
                    break;
                case ASSIGN:
                    resultado = resultado + "<ASSIGN> " + token.getDate() + "\n";
                    break;
		case ASPA:
                    resultado = resultado + "<ASPA> " + token.getDate() + "\n";
                    break;
		case LPAR:
                    resultado = resultado + "<LPAR> " + token.getDate() + "\n";
                    break;
		case RPAR:
                    resultado = resultado + "<RPAR> " + token.getDate() + "\n";
                    break;
		case VIRG:
                    resultado = resultado + "<VIRG> " + token.getDate() + "\n";
                    break;
		case PVIRG:
                    resultado = resultado + "<PVIRG> " + token.getDate() + "\n";
                    break;
		case LCHAVETA:
                    resultado = resultado + "<LCHAVETA> " + token.getDate() + "\n";
                    break;
		case RCHAVETA:
                    resultado = resultado + "<RCHAVETA> " + token.getDate() + "\n";
                    break;
		case FUNCTION:
                    resultado = resultado + "<FUNCTION> " + token.getDate() + "\n";
                    break;
		case SIZE:
                    resultado = resultado + "<SIZE> " + token.getDate() + "\n";
                    break;
		case MODULE:
                    resultado = resultado + "<MODULE> " + token.getDate() + "\n";
                    break;
		case STRING:
                    resultado = resultado + "<STRING> " + token.getDate() + "\n";
                    break;		
	        case ERROR:
                    resultado = resultado + "<Erro, símbolo não reconhecido> \n";
                     break;
                case ID:
                    resultado = resultado + "<ID> " + token.getDate() +"\n";
                    break;
                case INTEGER:
                    resultado = resultado + "<INTEGER> " + token.getDate() + "\n";
                    break;
                default:
                    resultado = resultado + "<" + token.getDate() +">" ;
                   
            
           
           }
       }



    }
    public Interface() {
        
    }


    
   
}
