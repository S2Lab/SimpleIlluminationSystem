package firok.sis.main;

import java.io.*;
import java.util.*;

public class AU
{
	public final String name;
	protected HashMap<String,String> datas;
	final public String[] defaultValues;
	
	public AU(String nameIn,String[] dvsIn)
	{
		name=nameIn;
		datas=new HashMap<String,String>();
		defaultValues=dvsIn==null?new String[0]:dvsIn;
	}
	
	public void addChild(String name,String value)
	{
		datas.put(name, value);
	}
	public String getValue(String nameIn)
	{
		return datas.get(nameIn);
	}
	
	public String getStringDefaultValues()
	{
		StringBuffer result=new StringBuffer("");
		
		int i;
		for(i=0;i<defaultValues.length-1;i++)
		{
			result.append(defaultValues[i]+',');
		}
		result.append(defaultValues[i]);
		
		return result.toString();
	}
	public boolean hasDefaultValue(String valueIn)
	{
		for(String temp:defaultValues)
		{
			if(temp.equals(valueIn))
				return true;
		}
		return false;
	}
}
