
import Estruturas.*;
import AnalisadorLexical.*;
import AnalisadorSemantico.SemanticAnalyses;
import AnalisadorSintático.AST;
import AnalisadorSintático.Parser;

import java.io.StringReader;
import java.util.ArrayList;

import javax.swing.JTextField;



public class Interface  {

    /**
     * Creates new form Inteface
     */
    public static void main(String[] args){
        
	Interface iinterface = new Interface();
	
	try {
		
		
		ArrayList<Token> tokens = iinterface.executar("module program1 { N; data[] =[N]; mx; mn; function det(d[]) { i=0; M=d.size-1; while(i<M) { a=d[i]; i=i+1; b=d[i]; mx= library1.max(a,b); mn= library1.min(a,b); } } function main() { det(data); io.println(\"max: \",mx); io.println(\"min: \",mn); }}");
		TreeNode<Symbol> CST = iinterface.parse(tokens);
		
		AST abstractSyntaxTree = new AST(CST);
		TreeNode<Symbol> AST = abstractSyntaxTree.run();
		
		SemanticAnalyses sem = new SemanticAnalyses(AST);
		sem.run();
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
    }
    
    public TreeNode<Symbol> parse(ArrayList<Token> tokens) {
    	Parser parse = new Parser(tokens);
    	return parse.parse();
    }
    
   
    
    public ArrayList<Token> executar(String expr) throws Exception{
        
           
       Lexer lexer = new Lexer(new StringReader(expr));
       ArrayList<Token> tokens = new ArrayList<Token>();
       String resultado="";
       
       while(true){
           Token token = lexer.yylex();
           if(token == null){
        	   tokens.add(new Token(TokenID.END, "", 0, 0));
                
        	   System.out.println(resultado);
                return tokens;
           }
                
              tokens.add(token);  
       
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
	    /*case ERROR:
                    resultado = resultado + "<Erro, símbolo não reconhecido> \n";
                     break; */
		        case ID:
		                    resultado = resultado + "<ID> " + token.getDate() +"\n";
		                    break;
		        case RPARR:
		        	  resultado = resultado + "<RPARR> " + token.getDate() +"\n";
		        	break;
		        case LPARR:
		        	  resultado = resultado + "<LPARR> " + token.getDate() +"\n"; 
		        	break;
		        case PONTO:
		        	  resultado = resultado + "<PONTO> " + token.getDate() +"\n";
		        	break;
                case INTEGER:
                    resultado = resultado + "<INTEGER> " + token.getDate() + "\n";
                    break;
                case ERROR:
                	resultado = resultado + "<ERROR> " + token.getDate() + "\n";
                    break;
                default:
                    resultado = resultado + "<" + token.getDate() +">" ;
                   
            
           
           }
       }
       
      



    }
    public Interface() {
        
    }


    
   
}