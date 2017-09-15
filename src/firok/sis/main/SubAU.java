package firok.sis.main;

import java.util.*;

public class SubAU
{
	public HashMap<String,String> values;
	
	public SubAU()
	{
		values=new HashMap<String,String>();
	}
	
	public void addValue(String nameIn,String valueIn)
	{
		values.put(nameIn,valueIn);
	}
	public String getValue(String nameIn)
	{
		return values.get(nameIn);
	}
}
