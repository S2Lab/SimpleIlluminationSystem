package firok.sis.main;

import java.io.*;
import java.util.*;
import firok.sis.gui.*;

public class SIS
{
	protected LinkedList<BaseAU> baseAUs;
	protected LinkedList<String> nameItems;
	protected HashMap<String,SubAU> subAUs;
	final public String workspace;
	
	public final static String path_cfg="/cfg";
	public final static String path_file="/src";
	public final static String path_cfg_attrs="/attrs.lst";
	public final static String path_file_items="/items";
	
	protected WinCMD winCMD;
	protected WinMain winMain;
	
	public SIS(String workspaceIn) // workspaceIn 要像这样: 以'/'开头 
	{
		winCMD=new WinCMD();
		winCMD.setProcessStatus("初始化状态", 0);
		workspace=workspaceIn.trim();
		winCMD.setProcessStatus("设定工作空间为 "+workspace, 5);
		
		try{
			winCMD.setProcessStatus("初始化工作空间", 10);
			_initPaths();
		}
		catch(Exception e) {winCMD.setProcessStatus("初始化工作空间失败", 100);return;}finally {;}
		
		try {
			winCMD.setProcessStatus("创建储存空间", 15);
			baseAUs=new LinkedList<BaseAU>();
			nameItems=new LinkedList<String>();
			subAUs=new HashMap<String,SubAU>();
		}
		catch(Exception e) {winCMD.setProcessStatus("创建储存空间失败", 100);return;}finally {;}
		
		try {
			winCMD.setProcessStatus("读取属性文件", 20);
			_loadAttribute();
		}
		catch (FileNotFoundException e) {winCMD.setProcessStatus("读取属性文件失败", 100);return;}finally {;}
		
		try {
			winCMD.setProcessStatus("读取图鉴条目", 30);
			_loadItem();
		} catch (FileNotFoundException e) {winCMD.setProcessStatus("读取图鉴条目失败", 100);return;}
		
		winCMD.setProcessStatus("初始化用户图形界面", 80);
		winMain=new WinMain();
		winMain.setDatas(baseAUs, nameItems, subAUs);
		winMain.show();
		winMain.setTreeContent(getContentFromLinkedListBaseAU(baseAUs));
		
		winCMD.setProcessStatus("初始化完成", 99);
		winCMD.hide();
		
		String[][] tempstrs=getContentFromLinkedListBaseAU(baseAUs);
		for(String[] line:tempstrs)
		{
			for(String word:line)
				System.out.println(word);
		}
	}
	
	public void _initPaths() throws IOException
	{
		File[] pf=new File[3]; // 创建目录
		pf[0]=new File(workspace+path_cfg);
		pf[1]=new File(workspace+path_file);
		pf[2]=new File(workspace+path_file+path_file_items);
		for(File tempF:pf)
		{
			if(!tempF.exists()||tempF.isFile())
				tempF.mkdirs();
		}
		File[] pff=new File[1]; // 创建文件
		pff[0]=new File(workspace+path_cfg+path_cfg_attrs); // 配置文件
		for(File tempF:pff)
		{
			System.out.println(tempF.getPath()+'/'+tempF.getName());
			if(!tempF.exists()||tempF.isDirectory())
				tempF.createNewFile();
		}
	}

	public static void main(String[] args) throws IOException
	{
		SIS sis=new SIS("./");
		System.out.println("new sis finished");
	}
	
	protected void _loadAttribute() throws FileNotFoundException
	{
		File workfile=new File(workspace+path_cfg+path_cfg_attrs);
		Scanner in=new Scanner(workfile);
		while(in.hasNextLine())
		{
			String line=new String(in.nextLine());
			
			if(line.indexOf('.')>=0) // 不是基属性
			{
				// 获取向哪个基属性添加AU
				String nameBaseAU= getNameBaseAU(line);
				// 获取BaseAU引用
				BaseAU baseAU= getBaseAU(nameBaseAU);
				// 获取预设属性值 (AU的名字)
				String nameAU= getNameAU(line);
				// 获取子属性值
				String[] defaultValues= getDefaultValues(line);
				// 创建AU
				AU au;
				if(baseAU!=null)
				{
					au=new AU(nameAU,defaultValues);
					baseAU.addChild(au);
				}
				else
					continue;
			}
			else // 基属性
			{
				boolean _if_add=true;
				for(BaseAU temp_baseAU:baseAUs) // 查重 有重名的就不加了
				{
					if(temp_baseAU.name.equals(line))
						_if_add=false;
				}
				if(_if_add)
					baseAUs.add(new BaseAU(line));
			}
		}
	}
	
	protected void _loadItem() throws FileNotFoundException
	{
		File workfiles=new File(workspace+path_file+path_file_items); // 工作空间/items目录
		
		
		for(File workfile:workfiles.listFiles())
		{
			Scanner in=new Scanner(workfile);
			String nameItem=new String(workfile.getName());
			
			String nameBaseAU=new String("");
			String nameAU=new String("指向 nameAU");
			
			BaseAU baseAU;
			AU au;
			SubAU subAU= getSubAU(nameItem);
			
			nameItems.add(nameItem); // 保存这个项目的名称
			
			while(in.hasNextLine())
			{
				String line=new String(in.nextLine());
				/*
				 * ##__type#=植物
				 * ##__des#=0
				 * ##生长环境#=树林
				 * ##生长时间#=全天
				 * 这是一种植物
				 * */
				if(line.startsWith("##__des")) // 是一个描述节点 跳过
				{
					;
				}
				else if(line.startsWith("//")) // 注释行 跳过
				{
					;
				}
				else if(line.startsWith("##_type")) // base au 值
				{
					nameBaseAU=getNameAU(line);
					baseAU= getBaseAU(nameBaseAU);
				}
				else if(line.startsWith("##")) // 是一个属性节点
				{
					// 获取是什么属性
					nameAU=getNameAU(line);
					au=getAU(nameBaseAU,nameAU);
					
					if(au.hasDefaultValue(nameAU)) // 如果是一种默认属性 那就添加到au里
						au.addChild(nameItem, getValueAU(line));
					else // 如果是一种额外属性 那就添加到subau里管理
						subAU.addValue(nameAU, getValueAU(line));
				}
				else // 添加一个描述内容
				{
					nameAU="__des";
					au=getAU(nameBaseAU,nameAU);
					
					au.addChild(nameItem, au.getValue(nameItem)+'\n'+getValueAU(line));
				}
				
				in.close();
			}
		}
	}
	
	public BaseAU getBaseAU(String nameIn) // 寻找基属性节点
	{
		for(BaseAU temp_baseAU:baseAUs)
		{
			if(temp_baseAU.name.equals(nameIn))
				return temp_baseAU;
		}
		
		return null;
	}

	public AU getAU(String nameBaseAUIn,String nameAUIn)
	{
		BaseAU baseAU=getBaseAU(nameBaseAUIn);
		if(baseAU!=null)
		{
			return baseAU.getAU(nameAUIn);
		}
		return null;
	}
	
	public SubAU getSubAU(String nameItemIn) // 如果找不到 那就新建一个再返回
	{
		SubAU result=subAUs.get(nameItemIn);
		if(result==null)
		{
			subAUs.put(nameItemIn, new SubAU());
			return subAUs.get(nameItemIn);
		}
		else
			return result;
	}
	
	// test1.test2=1,2,3 // nameBaseAU==test1, nameAU==test2, dvs=1,2,3
	public static String getNameBaseAU(String lineIn)
	{
		StringBuffer result=new StringBuffer("");
		int loc_dot=lineIn.indexOf('.');
		for(int i=0;i<loc_dot;i++)
		{
			result.append(lineIn.charAt(i));
		}
		
		return result.toString();
	}
	public static String getNameAU(String lineIn)
	{
		StringBuffer result=new StringBuffer("");
		int loc_equal= lineIn.indexOf('=')>=0? lineIn.indexOf('=') :lineIn.length() ;
		for(int i=lineIn.indexOf('.')+1;i<loc_equal;i++)
		{
			result.append(lineIn.charAt(i));
		}
		
		return result.toString();
	}
	public static String[] getDefaultValues(String lineIn)
	{
		LinkedList<String> dvs=new LinkedList<String>();
		
		int loc_equal=lineIn.indexOf('=')>=0 ? lineIn.indexOf('='):lineIn.length();
		
		int i=loc_equal+1;
		
		StringBuffer tempItem=new StringBuffer("");
		
		while(i<lineIn.length())
		{
			if(lineIn.charAt(i)==',')
			{
				dvs.add(tempItem.toString());
				tempItem=new StringBuffer("");
			}
			else
			{
				tempItem.append(lineIn.charAt(i));
			}
			
			i++;
		}
		
		String[] result=new String[dvs.size()];
		for(i=0;i<dvs.size();i++)
		{
			result[i]=dvs.get(i);
		}
		return result;
	}

	// ##__type#=植物 // nameAU==_type, valueAU==植物
	public static String getNameBaseAU(String lineIn,boolean botUsed)
	{
		return "";
	}
	public static String getNameAU(String lineIn,boolean notUsed)
	{
		StringBuffer result=new StringBuffer("");
		int loc_begin=lineIn.indexOf("##")>=0? lineIn.indexOf("##")+2 : lineIn.length();
		int loc_end=lineIn.indexOf("#=")>=0? lineIn.indexOf("#=") : lineIn.length();
		while(loc_begin<loc_end)
		{
			result.append(lineIn.charAt(loc_begin));
			loc_begin++;
		}
		return result.toString();
	}
	public static String getValueAU(String lineIn)
	{
		StringBuffer result=new StringBuffer("");
		
		int loc_begin=lineIn.indexOf("#=")>=0?lineIn.indexOf("#=")+2: lineIn.length();
		while(loc_begin<lineIn.length())
		{
			result.append(lineIn.charAt(loc_begin));
			loc_begin++;
		}
		
		return result.toString();
	}

	static String[][] getContentFromLinkedListBaseAU(LinkedList<BaseAU> llbaseAUIn)
	{
		if(llbaseAUIn==null)
			return null;
		
		String[][] result;
		
		BaseAU[] arrayBaseAU=new BaseAU[llbaseAUIn.size()];
		int i=0;
		for(BaseAU tempbaseAU:arrayBaseAU)
		{
			tempbaseAU=llbaseAUIn.get(i);
			i++;
		}
		result=new String[arrayBaseAU.length][];
		
		for(String[] tempstr:result)
		{
			tempstr=new String[llbaseAUIn.get(i).childrenAU.size()];
			i++;
		}
		
		return result;
	}
}
