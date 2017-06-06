package Generator;

import Tree.Node;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Converter
{
    File jvm;
    StringBuilder lines;
    boolean isDecsNull = true;
    Map<String, Integer> sp;
    int spNo;
    private int loopNo;
    
    public Converter(String fileName)
    {
        jvm = new File(fileName);
        lines = new StringBuilder();
        loopNo = 0;
    }
    
    public void Convert(Node<String> node)
    {
        switch(node.getValue())
        {
            case "MODULE":
                lines.append(".class public "+node.getChildAt(0).getValue());
                lines.append("\n.super java/lang/Object");
                
                //Test to see if DECLARATIONS node is leaf
                if(!node.getChildAt(1).isLeaf())
                {
                    isDecsNull = false;
                    Convert(node.getChildAt(1));
                }
                
                //Test to see if FUNCTIONS node is leaf
                if(!node.getChildAt(2).isLeaf())
                    Convert(node.getChildAt(2));
        
                try
                {
                    Files.write(Paths.get(jvm.getName()), Arrays.asList(lines), Charset.forName("UTF-8"));
                }
                catch (IOException ex)
                {
                    System.out.println("Could not write file");
                    ex.printStackTrace();
                }
        
                break;
                    
            case "DECLARATIONS":
                Node<String> temp1;
                Node<String> temp2;
                Map<String, String> arrays = new HashMap<>();
                
                for(int i = 0; i < node.getChildCount(); i++)
                {
                    if(node.getChildAt(i).getValue().equals("ASSIGN"))
                    {
                        temp1 = node.getChildAt(i).getChildAt(0);
                        temp2 = node.getChildAt(i).getChildAt(1);
                        
                        if(temp2.getValue().equals("ARRAY"))
                        {
                            lines.append("\n.field static "+temp1.getValue()+" [I");
                            arrays.put(temp1.getValue(), temp2.getChildAt(0).getValue());
                        }
                        else
                            lines.append("\n.field static "+temp1.getValue()+" I = "+temp2.getValue());   
                    }
                    else
                    {
                        temp1 = node.getChildAt(i);
                        
                        if(temp1.getValue().equals("ARRAY"))
                            lines.append("\n.field static "+temp1.getChildAt(0).getValue()+" [I");
                        else
                            lines.append("\n.field static "+temp1.getChildAt(0).getValue()+" I");
                    }
                }
                
                
                if(!arrays.isEmpty())
                {
                    lines.append("\n.method static public <clinit>()V");
                    lines.append("\n\t.limit statck 2");
                    lines.append("\n\t.limit locals 0");
                    
                    for(String key : arrays.keySet())
                    {
                        lines.append("\n\n\tbipush "+arrays.get(key));
                        lines.append("\n\tnewarray int");
                        lines.append("\n\tputstatic "+node.getParent().getChildAt(0).getValue()+"/"+key+" [I\n");
                    }
                    
                    lines.append("\n\n\treturn\n.end method");
                }
                
                break;
                
            case "FUNCTIONS":
                for(Node<String> nd : node.getChildren())
                {
                    lines.append(functionsHandler(nd));
                }
                break;
        }
        if(isDecsNull)
        {
            lines.append("\n\n.method static public <clinit>()V");
            lines.append("\n.limit statck 2");
            lines.append("\n.limit locals 0");        
            lines.append("\n\nreturn\n.end method");
        }
        
    }
    
    private StringBuilder functionsHandler(Node<String> nd)
    {
        StringBuilder func = new StringBuilder();
        sp = new HashMap<String, Integer>();
        spNo = 0;
        
        
        if(nd.getChildAt(0).getValue().equals("main"))
        {
            func.append("\n\n.method public static main([Ljava/lang/String;)V");
        }
        else if(nd.getChildAt(0).getValue().equals("ASSIGN"))
        {
            func.append("\n\n.method public static "+nd.getChildAt(0).getChildAt(1).getValue()+"(");
            
            if(!nd.getChildAt(1).isLeaf())
            {
                for(Node<String> n : nd.getChildAt(1).getChildren())
                {
                    if(n.getValue().equals("ARRAY"))
                    {
                        func.append("[I");
                        sp.put(n.getChildAt(0).getValue(), spNo++);
                    }
                    else if(n.getValue().equals("LDC"))
                    {
                        func.append("[Ljava/lang/String;");
                        sp.put(n.getChildAt(0).getValue(), spNo++);
                    }
                    else
                    {
                        func.append("I");
                        sp.put(n.getValue(), spNo++);
                    }
                }
            }
            
            Node<String> retType = nd.getChildAt(0).getChildAt(0);
            if(retType.getValue().equals("ARRAY"))
                func.append(")[I");
            else
                func.append(")I");   
        }
        else
        {
            func.append("\n.method public static ");
            func.append(nd.getChildAt(0).getValue()+"(");
            if(!nd.getChildAt(1).isLeaf())
            {
                for(Node<String> n : nd.getChildAt(1).getChildren())
                {
                    if(n.getValue().equals("ARRAY"))
                    {
                        func.append("[I");
                        sp.put(n.getChildAt(0).getValue(), spNo++);
                    }
                    else if(n.getValue().equals("LDC"))
                    {
                        func.append("[Ljava/lang/String;");
                        sp.put(n.getChildAt(0).getValue(), spNo++);
                    }
                    else
                    {
                        func.append("I");
                        sp.put(n.getValue(), spNo++);
                    }
                }
            }
            func.append(")V");
        }
        func.append("\n.limit locals 10");
        func.append("\n.limit stack 10");
        
        func.append(blockHandler(nd));
        
        if(nd.getChildAt(0).getValue().equals("ASSIGN"))
        {
            if(nd.getChildAt(0).getChildAt(0).getValue().equals("ARRAY"))
            {
                func.append("\n\naload_");
                func.append(sp.get(nd.getChildAt(0).getChildAt(0).getChildAt(0).getValue()));
                func.append("\nareturn");
            }
            else
            {
                func.append("\n\niload_");
                func.append(sp.get(nd.getChildAt(0).getChildAt(0).getValue()));
                func.append("\nireturn");
            }
        }
        else
            func.append("\n\nreturn");
        
        func.append("\n.end method");
        
        return func;
    }
    
    private StringBuilder blockHandler(Node<String> nd)
    {
        StringBuilder jvm = new StringBuilder();
         
        int i;
        if(nd.getChildAt(0).getValue().equals("main")
            || nd.getValue().equals("WHILE")
            ||(nd.getValue().equals("IF") && nd.getChildCount() == 2))
            i = 1;
        else if(nd.getValue().equals("FOR"))
            i = 3;
        else if(nd.getValue().equals("ELSE"))
            i = 0;
        else
            i = 2;
        
        for(Node<String> no : nd.getChildAt(i).getChildren())
        {
            switch(no.getValue())
            {
                case "ASSIGN":
                    jvm.append(assignHandler(no));
                    break;
                    
                case "WHILE":
                    jvm.append(whileHandler(no));
                    break;
                    
                case "IF":
                    jvm.append(ifHandler(no));
                    break;
                    
                case "FOR":
                    jvm.append(forHandler(no));
                    break;
                    
                case "CALL":
                    jvm.append(callHandler(no));
                    break;
            }
        }
        
        return jvm;
    }
    
    private StringBuilder assignHandler(Node<String> nd)
    {
        Node<String> lhsN = nd.getChildAt(0);
        String lhsV = lhsN.getValue();
        
        Node<String> rhsN = nd.getChildAt(1);
        String rhsV = rhsN.getValue();
        
        StringBuilder jvm = new StringBuilder();
        
        if(lhsV.equals("ARRAY") && lhsN.getChildCount() == 1)
        {
            if(rhsV.equals("SIZE"))
            {
                String size = rhsN.getChildAt(0).getValue();
                if(size.matches("\\d+"))
                {
                    jvm.append("\nbipush ");
                    jvm.append(size);
                }
                else
                {
                    jvm.append("\niload_");
                    jvm.append(sp.get(size));
                }
            }
            else if(rhsV.equals("ARRAY") && rhsN.getChildCount() == 1)
            {
                jvm.append("\naload_");
                jvm.append(sp.get(rhsN.getChildAt(0).getValue()));
            }
            else if(rhsV.equals("ARRAY") && rhsN.getChildCount() > 1)
            {
                jvm.append("\naload_");
                jvm.append(sp.get(rhsN.getChildAt(0).getValue()));
                jvm.append("\niload_");
                jvm.append(sp.get(rhsN.getChildAt(1).getValue()));
                jvm.append("\niaload");
            }
            else if(rhsV.equals("CALL"))
            {
                jvm.append(callHandler(rhsN));
            }
            else
            {
                jvm.append("\niload_");
                jvm.append(sp.get(rhsV));
            }
        }
        else if(lhsV.equals("ARRAY") && lhsN.getChildCount() > 1)
        {
            String Lindex = lhsN.getChildAt(1).getValue();
            
            
            jvm.append("\naload_");
            jvm.append(sp.get(lhsN.getChildAt(0).getValue()));
            if(sp.containsKey(Lindex))
            {
                jvm.append("\niload_");
                jvm.append(sp.get(Lindex));
            }
            else if(Lindex.equals("0"))
            {
                jvm.append("\niconst_0");
            }
            else
            {
                jvm.append("\nbipush ");
                jvm.append(Lindex);
            }
            
            if(rhsV.matches("\\d+"))
            {
                jvm.append("\niconst_");
                jvm.append(rhsV);
            }
            else if(rhsN.getChildCount() > 1)
            {
                String Rindex = rhsN.getChildAt(1).getValue();
                jvm.append("\naload_");
                jvm.append(sp.get(rhsN.getChildAt(0).getValue()));
                if(sp.containsKey(Rindex))
                {
                    jvm.append("\niload_");
                    jvm.append(sp.get(Rindex));
                }
                else if(Rindex.equals("0"))
                {
                    jvm.append("\niconst_0");
                }
                else
                {
                    jvm.append("\nbipush_");
                    jvm.append(Rindex);
                }
                jvm.append("\niaload");
            }
            
            jvm.append("\niastore");
        }
        else
        {
            switch(rhsV)
            {
                case "ARRAY":
                    jvm.append("\naload_");
                    if(rhsN.getChildCount() == 1)
                    {
                        jvm.append(sp.get(rhsN.getChildAt(0).getValue()));
                    }
                    else
                    {
                        jvm.append(sp.get(rhsN.getChildAt(0).getValue()));
                        if(rhsN.getChildAt(1).getValue().matches("\\d+"))
                        {
                            if(Integer.parseInt(rhsN.getChildAt(1).getValue()) > 5)
                                jvm.append("\nbipush ");
                            else
                                jvm.append("\niconst_");
                            
                            jvm.append(rhsN.getChildAt(1).getValue());
                        }
                        else
                        {
                            jvm.append("\niload_");
                            jvm.append(sp.get(rhsN.getChildAt(1).getValue()));
                        }
                        
                        jvm.append("\niaload");
                    }
                    break;
                    
                case "CALL":
                    jvm.append(callHandler(rhsN));
                    break;
                    
                case "ADD":
                    jvm.append(operationsHandler("ADD", rhsN.getChildAt(0), rhsN.getChildAt(1)));
                    break;
                    
                case "SUB":
                    jvm.append(operationsHandler("SUB", rhsN.getChildAt(0), rhsN.getChildAt(1)));
                    break;
                    
                case "MUL":
                    jvm.append(operationsHandler("MUL", rhsN.getChildAt(0), rhsN.getChildAt(1)));
                    break;
                    
                case "DIV":
                    jvm.append(operationsHandler("DIV", rhsN.getChildAt(0), rhsN.getChildAt(1)));
                    break;
                    
                default:
                    if(rhsV.matches("\\d+")) //If rhs is an Integer
                    {
                        jvm.append("\niconst_");
                        jvm.append(rhsV);
                    }
                    else //If rhs is a variable
                    {
                        jvm.append("\niload_");
                        jvm.append(sp.get(rhsV));
                    }
                    break;
            }
        }
        
        if(lhsV.equals("ARRAY"))
        {
            if(lhsN.getChildCount() == 1)
            {
                
                if(sp.containsKey(lhsN.getChildAt(0).getValue()))
                {
                    jvm.append("\nastore_");
                    jvm.append(sp.get(lhsN.getChildAt(0).getValue()));
                }
                else if(rhsV.equals("CALL") && rhsN.getChildAt(3).getValue().equals("ARRAY"))
                {
                    jvm.append("\nastore_");
                    if(sp.containsKey(lhsN.getChildAt(0).getValue()))
                        jvm.append(sp.get(lhsN.getChildAt(0).getValue()));
                    else
                    {
                        sp.put(lhsN.getChildAt(0).getValue(), spNo++);
                        jvm.append(sp.get(lhsN.getChildAt(0).getValue()));
                    }
                }
                else if(rhsV.equals("CALL") && rhsN.getChildAt(3).getValue().equals("INTEGER"))
                {
                    jvm.append("\nistore_");
                    if(sp.containsKey(lhsN.getChildAt(0).getValue()))
                        jvm.append(sp.get(lhsN.getChildAt(0).getValue()));
                    else
                    {
                        sp.put(lhsN.getChildAt(0).getValue(), spNo++);
                        jvm.append(sp.get(lhsN.getChildAt(0).getValue()));
                    }
                }
                else
                {
                    jvm.append("\nnewarray int");
                    jvm.append("\nastore_");
                    sp.put(lhsN.getChildAt(0).getValue(), spNo++);
                    jvm.append(sp.get(lhsN.getChildAt(0).getValue()));
                }
            }
        }
        else
        {
            jvm.append("\nistore_");
            if(sp.containsKey(lhsV))
            {
                jvm.append(sp.get(lhsV));
            }
            else
            {
                sp.put(lhsV, spNo++);
                jvm.append(sp.get(lhsV));
            }
        }
        
        return jvm;
    }

    private StringBuilder callHandler(Node<String> nd)
    {
        /*Stores the module node and value or the variable node and value if the function name is "size"*/
        Node<String> modNode = nd.getChildAt(0);
        String modValue = modNode.getValue();
        
        /*Stores the funcion node and value*/
        Node<String> funcNode = nd.getChildAt(1);
        String funcValue = funcNode.getValue();
        
        /*Stores the return type node and value*/
        Node<String> returnNode = nd.getChildAt(3);
        String returnValue = returnNode.getValue();
        
        /*JVM code builder*/
        StringBuilder jvm = new StringBuilder();
        
        if(funcValue.equals("size"))
        {
            jvm.append("\naload_");
            jvm.append(sp.get(modValue));
            jvm.append("\narrayLength");
            return jvm;
        }
        
        /*Load parameters if any*/
        if(!nd.getChildAt(2).isLeaf())
        {
            for(Node<String> n : nd.getChildAt(2).getChildren())
            {
                if(n.getValue().matches("\\d+"))
                {
                    if(Integer.parseInt(n.getValue()) > 5)
                        jvm.append("\nbipush ");
                    else
                        jvm.append("\niconst_");
                    
                    jvm.append(n.getValue());
                }
                else if(n.getValue().equals("ARRAY"))
                {
                    jvm.append("\naload_");
                    jvm.append(sp.get(n.getChildAt(0).getValue()));
                }
                else if(n.getValue().equals("LDC"))
                {
                    jvm.append("\nldc ");
                    jvm.append(n.getChildAt(0).getValue());
                }
                else
                {
                    jvm.append("\niload_");
                    jvm.append(sp.get(n.getValue()));
                }
            }
        }
        
        
        jvm.append("\ninvokestatic ");
        jvm.append(modValue);
        jvm.append("/");
        jvm.append(funcValue);

        /*Check parameters*/
        if (!nd.getChildAt(2).isLeaf())
        {
            jvm.append("(");
            for (Node<String> n : nd.getChildAt(2).getChildren())
            {
                if (n.getValue().equals("ARRAY"))
                {
                    jvm.append("[I");
                } else if (n.getValue().equals("LDC"))
                {
                    jvm.append("Ljava/lang/String;");
                } else
                {
                    jvm.append("I");
                }
            }
            jvm.append(")");
        } else
        {
            jvm.append("()");
        }

        //Check return type
        if (returnValue.equals("ARRAY"))
        {
            jvm.append("[I");
        } else if (returnValue.equals("INTEGER"))
        {
            jvm.append("I");
        } else
        {
            jvm.append("V");
        }
        
        return jvm;
    }

    private StringBuilder operationsHandler(String operation, Node<String> oper1, Node<String> oper2)
    {
        StringBuilder jvm = new StringBuilder();
        
        jvm.append(operandsHandler(oper1, oper2));
        
        switch(operation)
        {
            case "ADD":
                jvm.append("\niadd");
                break;
                
            case "SUB":
                jvm.append("\nisub");
                break;
                
            case "MUL":
                jvm.append("\nimul");
                break;
                
            case "DIV":
                jvm.append("\nidiv");
                break;
        }
        
        return jvm;
    }
    
    private StringBuilder relationalsHandler(Node<String> nd)
    {
        StringBuilder jvm = new StringBuilder();
        
        String relational = nd.getValue();
        Node<String> lhs = nd.getChildAt(0);
        Node<String> rhs = nd.getChildAt(1);
        
        jvm.append(operandsHandler(lhs, rhs));
        
        switch(relational)
        {
            case ">":
                jvm.append("\nif_icmple");
                break;
                
            case "<":
                jvm.append("\nif_icmpge");
                break;
                
            case ">=":
                jvm.append("\nif_icmplt");
                break;
                
            case "<=":
                jvm.append("\nif_icmpgt");
                break;
                
            case "==":
                jvm.append("\nif_icmpne");
                break;
                
            case "!=":
                jvm.append("\nif_icmpeq");
                break;
        }
        
        return jvm;
    }
    
    private StringBuilder operandsHandler(Node<String> oper1, Node<String> oper2)
    {
        StringBuilder jvm = new StringBuilder();
        
        if(oper1.getValue().matches("\\d+") && oper2.getValue().matches("\\d+")) //<Number> <operation> <Number>
        {
            jvm.append("\niconst_");
            jvm.append(oper1.getValue());
            jvm.append("\niconst_");
            jvm.append(oper2.getValue());
        }
        else if(oper1.getValue().equals("ARRAY") && oper2.getValue().matches("\\d+")) //<Array> <operation> <Number>
        {
            jvm.append("\naload_");
            jvm.append(sp.get(oper1.getChildAt(0).getValue()));
            jvm.append("\nbipush ");
            jvm.append(oper1.getChildAt(1).getValue());
            jvm.append("\niconst_");
            jvm.append(oper2.getValue());
        }
        else if(oper1.getValue().equals("ARRAY") && oper2.getValue().equals("ARRAY")) //<Array> <operation> <Array>
        {
            jvm.append("\naload_");
            jvm.append(sp.get(oper1.getChildAt(0).getValue()));
            jvm.append("\nbipush ");
            jvm.append(oper1.getChildAt(1).getValue());
            jvm.append("\naload_");
            jvm.append(sp.get(oper2.getChildAt(0).getValue()));
            jvm.append("\nbipush ");
            jvm.append(oper2.getChildAt(1).getValue());
        }
        else if(oper1.getValue().matches("\\d+") && oper2.getValue().equals("ARRAY")) //<Number> <operation> <Array>
        {
            jvm.append("\niconst_");
            jvm.append(oper1.getValue());
            jvm.append("\naload_");
            jvm.append(sp.get(oper2.getChildAt(0).getValue()));
            jvm.append("\nbipush ");
            jvm.append(oper2.getChildAt(1).getValue());
        }
        else if(oper1.getValue().equals("CALL") && oper2.getValue().matches("\\d+")) //<Call> <Operation> <Number>
        {
            jvm.append(callHandler(oper1));
            jvm.append("\niconst_");
            jvm.append(oper2.getValue());
        }
        else if(oper1.getValue().equals("CALL") && oper2.getValue().equals("ARRAY")) //<Call> <operation> <Array>
        {
            jvm.append(callHandler(oper1));
            jvm.append("\naload_");
            jvm.append(sp.get(oper2.getChildAt(0).getValue()));
            jvm.append("\nbipush ");
            jvm.append(oper2.getChildAt(1).getValue());
        }
        else if(oper1.getValue().equals("CALL")) //<Call> <operation> <Integer>
        {
            jvm.append(callHandler(oper1));
            jvm.append("\niload_");
            jvm.append(sp.get(oper2.getValue()));
        }
        else if(oper1.getValue().matches("\\d+") && oper2.getValue().equals("CALL")) //<Number> <operation> <Call>
        {
            jvm.append("\niconst_");
            jvm.append(oper1.getValue());
            jvm.append(callHandler(oper2));
        }
        else if(oper1.getValue().equals("ARRAY") && oper2.getValue().equals("CALL")) //<Array> <operation> <Call>
        {
            jvm.append("\naload_");
            jvm.append(sp.get(oper1.getChildAt(0).getValue()));
            jvm.append("\nbipush ");
            jvm.append(oper1.getChildAt(1).getValue());
            jvm.append(callHandler(oper2));
        }
        else if(oper2.getValue().equals("CALL")) //<Integer> <operation> <Call>
        {
            jvm.append("\niload_");
            jvm.append(sp.get(oper1.getValue()));
            jvm.append(callHandler(oper2));
        }
        else if(oper1.getValue().matches("\\d+")) //<Number> <operation> <Integer>
        {
            jvm.append("\niconst_");
            jvm.append(oper1.getValue());
            jvm.append("\niload_");
            jvm.append(sp.get(oper2.getValue()));
        }
        else if(oper1.getValue().equals("ARRAY")) //<Array> <operation> <Integer>
        {
            jvm.append("\naload_");
            jvm.append(sp.get(oper1.getChildAt(0).getValue()));
            jvm.append("\nbipush ");
            jvm.append(oper1.getChildAt(1).getValue());
            jvm.append("\niload_");
            jvm.append(sp.get(oper2.getValue()));
        }
        else if(oper2.getValue().matches("\\d+")) //<Integer> <operation> <Number>
        {
            jvm.append("\niload_");
            jvm.append(sp.get(oper1.getValue()));
            jvm.append("\niconst_");
            jvm.append(oper2.getValue());
        }
        else if(oper2.getValue().equals("ARRAY")) //<Integer> <operation> <Array>
        {
            jvm.append("\niload_");
            jvm.append(sp.get(oper1.getValue()));
            jvm.append("\naload_");
            jvm.append(sp.get(oper2.getChildAt(0).getValue()));
            jvm.append("\nbipush ");
            jvm.append(oper2.getChildAt(1).getValue());
        }
        else //<Integer> <operation> <Integer>
        {
            jvm.append("\niload_");
            jvm.append(sp.get(oper1.getValue()));
            jvm.append("\niload_");
            jvm.append(sp.get(oper2.getValue()));
        }
        
        return jvm;
    }
    
    private StringBuilder whileHandler(Node<String> nd)
    {
        StringBuilder jvm = new StringBuilder();
        
        jvm.append("\n\nloop"+loopNo+":");
        jvm.append(relationalsHandler(nd.getChildAt(0)));
        jvm.append(" loop"+loopNo+"_end");
        jvm.append(blockHandler(nd));
        jvm.append("\ngoto loop"+loopNo);
        jvm.append("\n\nloop"+(loopNo++)+"_end: ");
        
        return jvm;
    }
    
    private StringBuilder ifHandler(Node<String> nd)
    {
        StringBuilder jvm = new StringBuilder();
        
        if(nd.getChildCount() == 2)
        {
            jvm.append(relationalsHandler(nd.getChildAt(0)));
            jvm.append(" loop"+loopNo+"_end");
            jvm.append(blockHandler(nd));
            jvm.append("\n\nloop"+(loopNo++)+"_end: ");
        }
        else
        {
            jvm.append(relationalsHandler(nd.getChildAt(0)));
            jvm.append(" loop"+loopNo);
            jvm.append(blockHandler(nd));
            jvm.append("\ngoto loop"+loopNo+"_end");
            jvm.append("\n\nloop"+loopNo+": ");
            jvm.append(blockHandler(nd.getChildAt(2)));
            jvm.append("\n\nloop"+(loopNo++)+"_end: ");
        }
        
        return jvm;
    }
    
    private StringBuilder forHandler(Node<String> nd)
    {
        StringBuilder jvm = new StringBuilder();
        
        return jvm;
    }
}
