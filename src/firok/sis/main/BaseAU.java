package firok.sis.main;

import java.io.*;
import java.util.*;

public class BaseAU
{
	final String name;
	LinkedList<AU> childrenAU;
	
	public BaseAU(String nameIn)
	{
		name=nameIn;
		childrenAU=new LinkedList<AU>();
		
		// 内部属性 __des // description
		addChild(new AU("__des",null));
	}
	/*
	public void addChild(String nameIn)
	{
		boolean _if_add=true;
		for(AU temp_AU:childrenAU)
		{
			if(temp_AU.name.equals(nameIn))
				_if_add=false;
		}
		
		if(_if_add)
			childrenAU.add(new AU(nameIn));
	}*/
	public void addChild(AU auIn)
	{
		boolean _if_add=true;
		for(AU temp_AU:childrenAU)
		{
			if(temp_AU==auIn)
				_if_add=false;
		}
		
		if(_if_add)
			childrenAU.add(auIn);
	}
	public boolean hasChild(String nameIn)
	{
		for(AU tempAU:childrenAU)
		{
			if(tempAU.name==nameIn)
				return true;
		}
		return false;
	}
	
	public AU getAU(String nameAUIn)
	{
		for(AU temp_AU:childrenAU)
		{
			if(temp_AU.name.equals(nameAUIn))
				return temp_AU;
		}
		return null;
	}
}
