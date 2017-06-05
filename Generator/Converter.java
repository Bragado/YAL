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
                    Convert(node.getChildAt(1));
                
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
                
                System.out.println("Entrei aqui: DECLARATIONS");
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
                
                System.out.println("Sai do primeiro loop de declarations");
                
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
                
                System.out.println("Terminei declarations");
                break;
                
            case "FUNCTIONS":
                System.out.println("Entrei em functions");
                for(Node<String> nd : node.getChildren())
                {
                    System.out.println("iteracao do loop de functions");
                    lines.append(functionsHandler(nd));
                }
                System.out.println("terminei o loop de functions");
                break;
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
                    System.out.println("entrei no laco de functionsHandler");
                    
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
        }
        else
        {
            func.append(nd.getChildAt(0).getValue()+"(");
            if(!nd.getChildAt(1).isLeaf())
            {
                for(Node<String> n : nd.getChildAt(1).getChildren())
                {
                    System.out.println("entrei no segundo laco de functionsHandler");
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
        
        for(Node<String> no : nd.getChildAt(i).getChildren())
        {
            System.out.println("entrei no terceiro laco de functions handler");
            
            switch(no.getValue())
            {
                case "ASSIGN":
                    Node<String> LHS = no.getChildAt(0);
                    Node<String> RHS = no.getChildAt(1);
                    
                    if(parameters.contains(LHS.getValue())) //If LeftHand value is a parameter
                    {
                    
                    }
                    else if(parameters.contains(RHS.getValue())) //If RightHand value is a parameter
                    {
                    
                    }
                    else if(RHS.getValue().equals("CALL")) //If leftHand value is a function call
                    {
                        if(RHS.getChildAt(1).getValue().equals("size"))
                        {
                            func.append("\naload_");
                            func.append(sp.get(RHS.getChildAt(0).getValue()));
                            func.append("\narrayLength");
                            
                            if(LHS.getValue().equals("ARRAY"))
                            {
                                func.append("\nastore_");
                                if(sp.containsKey(LHS.getValue()))
                                    func.append(sp.get(LHS.getValue()));
                                else
                                {
                                    sp.put(LHS.getValue(), spNo++);
                                    func.append(sp.get(LHS.getValue()));
                                }
                            }
                            else
                            {
                                func.append("\nistore_");
                                if(sp.containsKey(LHS.getValue()))
                                    func.append(sp.get(LHS.getValue()));
                                else
                                {
                                    sp.put(LHS.getValue(), spNo++);
                                    func.append(sp.get(LHS.getValue()));
                                }
                            }
                        }
                        else
                        {
                            for(Node<String> param : LHS.getChildAt(2).getChildren())
                            {
                                if(param.getValue().matches("\\d+"))
                                {
                                    func.append("\niconst_");
                                    func.append(param.getValue());
                                }
                                else if(param.getValue().equals("ARRAY"))
                                {
                                    
                                }
                                else
                                {
                                
                                }
                            }
                        }
                    }
                    else if(no.getChildAt(0).getValue().equals("ARRAY")) //If leftHand value is an Array
                    {
                    
                    }
                    else if(no.getChildAt(1).getValue().equals("ARRAY")) //If rightHand value is an Array
                    {
                        
                    }
                    else if(no.getChildAt(1).getValue().matches("\\d+")) //If RightHand value is number
                    {
                        func.append("\niconst_");
                        func.append(no.getChildAt(1).getValue());
                        func.append("\nistore_");
                        if(sp.containsKey(no.getChildAt(0).getValue()))
                            func.append(sp.get(no.getChildAt(0).getValue()));
                        else
                        {
                            sp.put(no.getChildAt(0).getValue(), spNo++);
                            func.append(sp.get(no.getChildAt(0).getValue()));
                        }
                    }
                    else //If rightHand value is a variable
                    {
                        func.append("\niload_");
                        func.append(sp.get(no.getChildAt(1).getValue()));
                        func.append("\nistore_");
                        if(sp.containsKey(no.getChildAt(0).getValue()))
                            func.append(sp.get(no.getChildAt(0).getValue()));
                        else
                        {
                            sp.put(no.getChildAt(0).getValue(), spNo++);
                            func.append(sp.get(no.getChildAt(0).getValue()));
                        }
                    }
                    break;
                    
                case "WHILE":
                    break;
                    
                case "IF":
                    break;
                    
                case "FOR":
                    break;
                    
                case "CALL":
                    func.append("\ninvokestatic ");
                    func.append(no.getChildAt(0).getValue());
                    func.append("/");
                    func.append(no.getChildAt(1).getValue());
                    
                    //Check parameters
                    if(!no.getChildAt(2).isLeaf())
                    {
                        func.append("(");
                        for(Node<String> n : no.getChildAt(2).getChildren())
                        {
                            if(n.getValue().equals("ARRAY"))
                                func.append("[I");
                            else if(n.getValue().equals("STRING"))
                                func.append("Ljava/lang/String;");
                            else
                                func.append("I");
                        }
                        func.append(")");
                    }
                    else
                    {
                        func.append("()");
                    }
                    
                    //Check return type
                    if(no.getChildAt(3).getValue().equals("ARRAY"))
                        func.append("[I");
                    else if(no.getChildAt(3).getValue().equals("INTEGER"))
                        func.append("I");
                    else
                        func.append("V");
                    
                    break;
                    
                default:
                    break;
            }
        }
        
        func.append("\n\nreturn");
        func.append("\n.end method");
        
        return func;
    }
}
