package Tree;


import Generator.Converter;
import java.util.LinkedList;
import java.util.List;

public class Tree
{
    public static void main(String[] args)
    {
        Node<String> root = new Node<>("MODULE");
        {
            List<Node<String>> children = new LinkedList<>();
            
            Node<String> child0 = new Node<>("aval1");
            Node<String> child1 = new Node<>("DECLARATIONS");
            Node<String> child2 = new Node<>("FUNCTIONS");
            {
                List<Node<String>> children2 = new LinkedList<>();
                
                Node<String> child20 = new Node<>("function");
                {
                    List<Node<String>> children20 = new LinkedList<>();
                    
                    Node<String> child200 = new Node<>("main");
                    Node<String> child201 = new Node<>("BLOCK");
                    {
                        List<Node<String>> children201 = new LinkedList<>();
                        
                        Node<String> child2010 = new Node<>("ASSIGN");
                        {
                            List<Node<String>> children2010 = new LinkedList<>();

                            Node<String> child20100 = new Node<>("r");
                            Node<String> child20101 = new Node<>("CALL");
                            {
                                List<Node<String>> children20101 = new LinkedList<>();

                                Node<String> child201000 = new Node<>("aval1");
                                Node<String> child201001 = new Node<>("f");
                                Node<String> child201002 = new Node<>("PARAMETERS");
                                {
                                    List<Node<String>> children201001 = new LinkedList<>();

                                    Node<String> param1 = new Node<>("2");
                                    Node<String> param2 = new Node<>("3");

                                    children201001.add(param1);
                                    children201001.add(param2);
                                    child201002.setChildren(children201001);
                                }
                                Node<String> child201003 = new Node<>("INTEGER");

                                children20101.add(child201000);
                                children20101.add(child201001);
                                children20101.add(child201002);
                                children20101.add(child201003);
                                child20101.setChildren(children20101);
                            }

                            children2010.add(child20100);
                            children2010.add(child20101);
                            child2010.setChildren(children2010);
                        }

                        Node<String> child2011 = new Node<>("CALL");
                        {
                            List<Node<String>> children2011 = new LinkedList<>();

                            Node<String> child20110 = new Node<>("io");
                            Node<String> child20111 = new Node<>("println");
                            Node<String> child20112 = new Node<>("PARAMETERS");
                            {
                                List<Node<String>> children2022 = new LinkedList<>();

                                Node<String> param1 = new Node<>("r");

                                children2022.add(param1);
                                child20112.setChildren(children2022);
                            }
                            Node<String> child20113 = new Node<>("void");

                            children2011.add(child20110);
                            children2011.add(child20111);
                            children2011.add(child20112);
                            children2011.add(child20113);
                            child2011.setChildren(children2011);
                        }
                        children201.add(child2010);
                        children201.add(child2011);
                        child201.setChildren(children201);
                        
                    }
                    
                    children20.add(child200);
                    children20.add(child201);
                    child20.setChildren(children20);
                }
                
                Node<String> child21 = new Node<>("function");
                {
                    List<Node<String>> children21 = new LinkedList<>();
                    
                    Node<String> child210 = new Node<>("ASSIGN");
                    {
                        List<Node<String>> children210 = new LinkedList<>();
                        
                        Node<String> child2100 = new Node<>("a");
                        Node<String> child2101 = new Node<>("f");
                        
                        children210.add(child2100);
                        children210.add(child2101);
                        child210.setChildren(children210);
                    }
                    Node<String> child211 = new Node<>("PARAMETERS");
                    {
                        List<Node<String>> children211 = new LinkedList<>();
                        
                        Node<String> param1 = new Node<>("b");
                        Node<String> param2 = new Node<>("c");
                        
                        children211.add(param1);
                        children211.add(param2);
                        child211.setChildren(children211);
                    }
                    Node<String> child212 = new Node<>("BLOCK");
                    {
                        List<Node<String>> children212 = new LinkedList<>();
                        
                        Node<String> child2120 = new Node<>("ASSIGN");
                        {
                            List<Node<String>> children2120 = new LinkedList<>();
                            
                            Node<String> child21200 = new Node<>("a");
                            Node<String> child21201 = new Node<>("MUL");
                            {
                                List<Node<String>> children21201 = new LinkedList<>();
                                
                                Node<String> child212010 = new Node<>("b");
                                Node<String> child212011 = new Node<>("c");
                                
                                children21201.add(child212010);
                                children21201.add(child212011);
                                child21201.setChildren(children21201);
                            }
                            
                            children2120.add(child21200);
                            children2120.add(child21201);
                            child2120.setChildren(children2120);
                        }
                        
                        children212.add(child2120);
                        child212.setChildren(children212);
                    }
                    
                    
                    children21.add(child210);
                    children21.add(child211);
                    children21.add(child212);
                    child21.setChildren(children21);
                }
                
                children2.add(child20);
                children2.add(child21);
                child2.setChildren(children2);
            }

            children.add(child0);
            children.add(child1);
            children.add(child2);

            root.setChildren(children);
        }
        
        Converter cv = new Converter();
        cv.Convert(root);
    }
}
