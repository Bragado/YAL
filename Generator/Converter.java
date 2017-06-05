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
    Map<String, LinkedList<String>> funcParam;
    boolean isDecsNull = true;
    
    public Converter()
    {
        jvm = new File("JVM.j");
        lines = new StringBuilder();
        funcParam = new HashMap<String, LinkedList<String>>();
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
        LinkedList<String> parameters = new LinkedList<>();
        Map<String, Integer> sp = new HashMap<String, Integer>();
        int spNo = 0;
        
        
        if(nd.getChildAt(0).getValue().equals("main"))
        {
            func.append("\n\n.method public static main([Ljava/lang/String;)V");
        }
        else if(nd.getChildAt(0).getValue().equals("ASSIGN"))
        {
            func.append("\n\n.method public static "+nd.getChildAt(0).getChildAt(1).getValue()+"(");
            funcParam.put(nd.getChildAt(0).getChildAt(1).getValue(), new LinkedList<>());
            
            if(!nd.getChildAt(1).isLeaf())
            {
                for(Node<String> n : nd.getChildAt(1).getChildren())
                {
                    if(n.getValue().equals("ARRAY"))
                    {
                        func.append("[I");
                        parameters.add(n.getChildAt(0).getValue());
                        funcParam.replace(nd.getChildAt(0).getChildAt(1).getValue(), parameters);
                        sp.put(n.getChildAt(0).getValue(), spNo++);
                    }
                    else
                    {
                        func.append("I");
                        parameters.add(n.getValue());
                        funcParam.replace(nd.getChildAt(0).getChildAt(1).getValue(), parameters);
                        sp.put(n.getValue(), spNo++);
                    }
                }
            }
            
            if(nd.getChildAt(0).getChildAt(0).getValue().equals("ARRAY"))
                func.append(")[I");
            else
                func.append(")I");
            
            sp.put(nd.getChildAt(0).getChildAt(0).getValue(), spNo++);
        }
        else
        {
            func.append(nd.getChildAt(0).getValue()+"(");
            if(!nd.getChildAt(1).isLeaf())
            {
                for(Node<String> n : nd.getChildAt(1).getChildren())
                {
                    if(n.getValue().equals("ARRAY"))
                    {
                        func.append("[I");
                        parameters.add(n.getChildAt(0).getValue());
                        funcParam.replace(nd.getChildAt(0).getChildAt(1).getValue(), parameters);
                        sp.put(n.getChildAt(0).getValue(), spNo++);
                    }
                    else
                    {
                        func.append("I");
                        parameters.add(n.getValue());
                        funcParam.replace(nd.getChildAt(0).getChildAt(1).getValue(), parameters);
                        sp.put(n.getValue(), spNo++);
                    }
                }
            }
            func.append(")V");
        }
        func.append("\n.limit locals 10");
        func.append("\n.limit stack 10");
        
        
        /*--------------- SPLIT HERE ---------------------*/
        
        
        int i;
        if(nd.getChildAt(0).getValue().equals("main"))
            i = 1;
        else
            i = 2;
        
        /*Handle the BLOCK section, calling each node of the block*/
        for(Node<String> no : nd.getChildAt(i).getChildren())
        {
            switch(no.getValue())
            {
                case "ASSIGN":
                    func.append(assignHandler(no, sp, spNo));
                    break;
                    
                case "WHILE":
                    func.append(assignHandler(no, sp, spNo));
                    break;
                    
                case "IF":
                    break;
                    
                case "FOR":
                    break;
                    
                case "CALL":
                    func.append(callHandler(no, sp, spNo));
                    break;
                    
                default:
                    break;
            }
        }
        
        if(nd.getChildAt(0).getChildCount() == 2 )
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

    private StringBuilder whileHandler(Node<String> nd, Map<String, Integer> sp, int spNo)
    {
        Node<String> lhsN = nd.getChildAt(0);
        String lhsV = lhsN.getValue();
        
        Node<String> rhsN = nd.getChildAt(1);
        String rhsV = rhsN.getValue();

        StringBuilder jvm = new StringBuilder();
        
        switch (lhsV) {
            case ">":
                jvm.append(RelationalsHandler(">", lhsN.getChildAt(0), lhsN.getChildAt(1), sp));
                break;
            case "<":
                jvm.append(RelationalsHandler("<", lhsN.getChildAt(0), lhsN.getChildAt(1), sp));
                break;
            case ">=":
                jvm.append(RelationalsHandler(">=", lhsN.getChildAt(0), lhsN.getChildAt(1), sp));
                break;
            case "<=":
                jvm.append(RelationalsHandler("<=", lhsN.getChildAt(0), lhsN.getChildAt(1), sp));
                break;
            case "!=":
                jvm.append(RelationalsHandler("!=", lhsN.getChildAt(0), lhsN.getChildAt(1), sp));
                break;
            case "==":
                jvm.append(RelationalsHandler("==", lhsN.getChildAt(0), lhsN.getChildAt(1), sp));
                break;
            default:
                callHandler(lhsN, sp);
                break;
        }

        assignHandler(rhsV, sp, spNo));

        jvm.return;

    }
    
    private StringBuilder assignHandler(Node<String> nd, Map<String, Integer> sp, int spNo)
    {
        Node<String> lhsN = nd.getChildAt(0);
        String lhsV = lhsN.getValue();
        
        Node<String> rhsN = nd.getChildAt(1);
        String rhsV = rhsN.getValue();
        
        StringBuilder jvm = new StringBuilder();
        
        if(lhsV.equals("ARRAY") && lhsN.getChildCount() == 1)
        {
            if(rhsV.equals("ARRAY") && rhsN.getChildCount() == 1)
            {
                jvm.append("\naload_");
                jvm.append(sp.get(rhsN.getChildAt(0).getValue()));
            }
            else if(rhsV.equals("ARRAY") && rhsN.getChildCount() > 1)
            {
                
            }
            else if(rhsV.matches("\\d+"))
            {
            
            }
            else
            {
                jvm.append("\niload_");
                jvm.append(sp.get(rhsV));
            }
            
            jvm.append("\nnewarray int");
            jvm.append("\nastore_");
            jvm.append(sp.get(lhsV));
        }
        else if(lhsV.equals("ARRAY") && lhsN.getChildCount() > 1)
        {
            String Lindex = lhsN.getChildAt(1).getValue();
            String Rindex = rhsN.getChildAt(1).getValue();
            
            jvm.append("\naload_");
            jvm.append(sp.get(lhsN.getChildAt(0).getValue()));
            if(sp.containsKey(Lindex))
            {
                jvm.append("\niload_");
                jvm.append(sp.get(Lindex));
            }
            else
            {
                jvm.append("\niconst_");
                jvm.append(Lindex);
            }
            
            
            if(rhsV.matches("\\d+"))
            {
                jvm.append("\niconst_");
                jvm.append(rhsV);
            }
            else
            {
                jvm.append("\naload_");
                jvm.append(sp.get(rhsN.getChildAt(0).getValue()));
                if(sp.containsKey(Rindex))
                {
                    jvm.append("\niload_");
                    jvm.append(sp.get(Rindex));
                }
                else
                {
                    jvm.append("\niconst_");
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
                    break;
                    
                case "CALL":
                    if(rhsN.getChildAt(1).getValue().equals("size"))
                    {
                        jvm.append("\naload_");
                        jvm.append(sp.get(rhsN.getChildAt(0).getValue()));
                        jvm.append("\narrayLength");
                    }
                    else
                    {
                        for(Node<String> param : rhsN.getChildAt(2).getChildren())
                        {
                            if(param.getValue().matches("\\d+"))
                            {
                                jvm.append("\niconst_");
                                jvm.append(param.getValue());
                            }
                            else if(param.getValue().equals("ARRAY"))
                            {
                                  
                            }
                            else
                            {
                                
                            }
                        }
                        
                        jvm.append(callHandler(rhsN, sp));
                    }
                    break;
                    
                case "ADD":
                    jvm.append(operationsHandler("ADD", rhsN.getChildAt(0), rhsN.getChildAt(1), sp));
                    break;
                    
                case "SUB":
                    jvm.append(operationsHandler("SUB", rhsN.getChildAt(0), rhsN.getChildAt(1), sp));
                    break;
                    
                case "MUL":
                    jvm.append(operationsHandler("MUL", rhsN.getChildAt(0), rhsN.getChildAt(1), sp));
                    break;
                    
                case "DIV":
                    jvm.append(operationsHandler("DIV", rhsN.getChildAt(0), rhsN.getChildAt(1), sp));
                    break;
                    
                default:
                    if(rhsV.matches("\\d+")) //If rhs is an Integer
                    {
                        jvm.append("\niconst_");
                        jvm.append(rhsV);
                        jvm.append("\nistore_");
                        if(sp.containsKey(lhsV))
                            jvm.append(sp.get(lhsV));
                        else
                        {
                            sp.put(lhsV, spNo++);
                            jvm.append(sp.get(lhsV));
                        }
                    }
                    else //If rhs is a variable
                    {
                        jvm.append("\niload_");
                        jvm.append(sp.get(rhsV));
                        jvm.append("\nistore_");
                        if(sp.containsKey(lhsV))
                            jvm.append(sp.get(lhsV));
                        else
                        {
                            sp.put(lhsV, spNo++);
                            jvm.append(sp.get(lhsV));
                        }
                    }
                    break;
            }
        }
        
        if(lhsV.equals("ARRAY"))
        {
            jvm.append("\nastore_");
            if(sp.containsKey(lhsV))
                jvm.append(sp.get(lhsV));
            else
            {
                sp.put(lhsV, spNo++);
                jvm.append(sp.get(lhsV));
            }
        }
        else
        {
            jvm.append("\nistore_");
            if(sp.containsKey(lhsV))
                jvm.append(sp.get(lhsV));
            else
            {
                sp.put(lhsV, spNo++);
                jvm.append(sp.get(lhsV));
            }
        }
        
        return jvm;
    }

    private StringBuilder callHandler(Node<String> nd, Map<String, Integer> sp)
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
        
        /*Load parameters if any*/
        if(!nd.getChildAt(2).isLeaf())
        {
            for(Node<String> n : nd.getChildAt(2).getChildren())
            {
                if(n.getValue().matches("\\d+"))
                    continue;
                
                if(n.getValue().equals("ARRAY"))
                {
                    jvm.append("\naload_");
                    jvm.append(sp.get(n.getChildAt(0).getValue()));
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
                } else if (n.getValue().equals("STRING"))
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
 
    private StringBuilder RelationalsHandler(String operation, Node<String> oper1, Node<String> oper2, Map<String, Integer> sp){

        StringBuilder jvm = new StringBuilder();
    
        jvm.append("\niconst_");
        jvm.append(oper1.getValue());
        jvm.append("\niconst_");
        jvm.append(oper2.getValue());

        switch(operation)
            {
                case ">":
                    jvm.append("\ni>");
                    break;
                    
                case "<":
                    jvm.append("\ni<");
                    break;
                    
                case ">=":
                    jvm.append("\ni>=");
                    break;
                    
                case "<=":
                    jvm.append("\ni<=");
                    break;
                case "!=":
                    jvm.append("\ni!=");
                    break;
                case "==":
                    jvm.append("\ni==");
                    break;
            }
            
            return jvm;
     }
    



    private StringBuilder operationsHandler(String operation, Node<String> oper1, Node<String> oper2, Map<String, Integer> sp)
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
        else if(oper1.getValue().matches("\\d+")) //<Number> <operation> <Parameter>
        {
            jvm.append("\niconst_");
            jvm.append(oper1.getValue());
            jvm.append("\niload_");
            jvm.append(sp.get(oper2.getValue()));
        }
        else if(oper1.getValue().equals("ARRAY")) //<Array> <operation> <Parameter>
        {
            jvm.append("\naload_");
            jvm.append(sp.get(oper1.getChildAt(0).getValue()));
            jvm.append("\nbipush ");
            jvm.append(oper1.getChildAt(1).getValue());
            jvm.append("\niload_");
            jvm.append(sp.get(oper2.getValue()));
        }
        else if(oper2.getValue().matches("\\d+")) //<Parameter> <operation> <Number>
        {
            jvm.append("\niload_");
            jvm.append(sp.get(oper1.getValue()));
            jvm.append("\niconst_");
            jvm.append(oper2.getValue());
        }
        else if(oper2.getValue().equals("ARRAY")) //<Parameter> <operation> <Array>
        {
            jvm.append("\niload_");
            jvm.append(sp.get(oper1.getValue()));
            jvm.append("\naload_");
            jvm.append(sp.get(oper2.getChildAt(0).getValue()));
            jvm.append("\nbipush ");
            jvm.append(oper2.getChildAt(1).getValue());
        }
        else //<Parameter> <operation> <Parameter>
        {
            jvm.append("\niload_");
            jvm.append(sp.get(oper1.getValue()));
            jvm.append("\niload_");
            jvm.append(sp.get(oper2.getValue()));
        }
        
        
        
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
}
