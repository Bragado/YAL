package Tree;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import javax.swing.tree.TreeNode;

public class Node<T> implements TreeNode
{
    private Node<T> parent = null;
    private List<Node<T>> children = null;
    private T value = null;
    
    public Node(T value)
    {
        this.value = value;
        this.children = new LinkedList<>();
    }
    
    @Override
    public Node<T> getParent()
    {
        return parent;
    }

    public void setParent(Node<T> parent)
    {
        this.parent = parent;
    }

    public List<Node<T>> getChildren()
    {
        return children;
    }

    public void setChildren(List<Node<T>> children)
    {
        for(Node<T> child : children)
        {
            child.setParent(this);
        }
        
        this.children = children;       
    }

    public T getValue()
    {
        return value;
    }

    public void setValue(T value)
    {
        this.value = value;
    }

    @Override
    public Node<T> getChildAt(int childIndex)
    {
        return children.get(childIndex);
    }
    
    @Override
    public int getChildCount()
    {
        return children.size();
    }

    @Override
    public int getIndex(TreeNode node)
    {
        return children.indexOf(node);
    }

    @Override
    public boolean getAllowsChildren()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isLeaf()
    {
        if(children.isEmpty())
            return true;
        else
            return false;
    }

    @Override
    public Enumeration children()
    {
        return Collections.enumeration(children);
    }
}
