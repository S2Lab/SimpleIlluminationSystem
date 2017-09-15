package firok.sis.gui;

import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

import firok.sis.main.*;

public class WinMain {
	// 各种数据
	protected LinkedList<BaseAU> baseAUs;
	protected LinkedList<String> nameItems;
	protected HashMap<String,SubAU> subAUs;
	// 动态 窗口组件 组
	LinkedList<JComboBox> searchBoxes;
	// 各种窗口组件
	protected JFrame frame;
	Graphics frame_graphics;
	
	String[][] treeContent;
	JTree tree;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WinMain window = new WinMain();
					window.frame.setVisible(true);
					window.testFunction();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	public WinMain() {
		initialize();
	}

	private void initialize()
	{
		searchBoxes=new LinkedList<JComboBox>();
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		frame.setSize(1100, 700);
		
		JButton btnNewButton = new JButton("重置条件");
		btnNewButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		btnNewButton.setBounds(7, 612, 186, 40);
		frame.getContentPane().add(btnNewButton);
		frame_graphics = frame.getGraphics();
		
		Component verticalStrut = Box.createVerticalStrut(20);
		verticalStrut.setBounds(200, 0, 1, 665);
		frame.getContentPane().add(verticalStrut);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		verticalStrut_1.setBounds(400, 0, 1, 665);
		frame.getContentPane().add(verticalStrut_1);
		
		tree = new JTree();
		tree.setBackground(SystemColor.inactiveCaptionBorder);
		tree.setBounds(200, 13, 201, 639);
		tree.setScrollsOnExpand(true);
		frame.getContentPane().add(tree);
		
		JLabel label = new JLabel("筛选条件");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 24));
		label.setBounds(50, 13, 96, 39);
		frame.getContentPane().add(label);
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setBounds(412, 203, 668, 449);
		frame.getContentPane().add(editorPane);
		
		JButton btnNewButton_1 = new JButton("搜索");
		btnNewButton_1.setBounds(7, 567, 186, 40);
		btnNewButton_1.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		frame.getContentPane().add(btnNewButton_1);
		/*
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(14, 83, 187, 60);
		frame.getContentPane().add(comboBox);*/
		
		new SearchItem(frame,20,60,1,"测试1");
		new SearchItem(frame,20,60,2,"测试2");
		new SearchItem(frame,20,60,3,"测试3");
		
		frame.setSize(1100, 700);
	}

	public void setDatas(LinkedList<BaseAU> baseAUsIn,LinkedList<String> nameItemsIn,HashMap<String,SubAU> subAUsIn)
	{
		baseAUs=baseAUsIn;
		nameItems=nameItemsIn;
		subAUs=subAUsIn;
	}
	private void testFunction()
	{
		// TreeModel root=new TreeModel();
		// tree.setModel(root);
	}
	
	// 设定搜索列表里的东西
	public void setTreeContent(String[][] contentIn)
	{
		if(contentIn==null)
			return;
		JTree tempTree;
		tempTree=new JTree(contentIn);
		
		treeContent=contentIn;
		tree.setModel(tempTree.getModel());
	}
	static String[][] getContentFromLinkedListBaseAU(LinkedList<BaseAU> llbaseAUIn)
	{
		String[][] result;
		
		BaseAU[] arrayBaseAU=(BaseAU[])llbaseAUIn.toArray();
		
		return null;
	}
	class SearchItem // 包含一个文本和一个下拉菜单
	{
		public JFrame frame;
		public JLabel text;
		public JComboBox searchBox;
		
		// 根据timeIn来向下确定坐标
		SearchItem(JFrame frameIn,int xIn,int yIn,int timeIn,String textIn)
		{
			_init(frameIn,xIn,yIn,timeIn,textIn);
		}
		
		private void _init(JFrame frameIn,int xIn,int yIn,int timeIn,String textIn)
		{
			frame=frameIn;
			text=new JLabel(textIn);
			// 横坐标 纵坐标 长 宽
			text.setBounds(14, yIn+10+50*timeIn, 172, 20);
			text.setFont(new Font("微软雅黑", Font.PLAIN, 16));
			
			frame.getContentPane().add(text);
			searchBox=new JComboBox();
			searchBox.setBounds(14,yIn+30+50*timeIn,172,20);
			searchBox.setFont(new Font("微软雅黑", Font.PLAIN, 16));
			frame.getContentPane().add(searchBox);
		}
	}
	
	public void show()
	{
		this.frame.setVisible(true);
	}
	public void hide()
	{
		this.frame.setVisible(false);
	}
}
