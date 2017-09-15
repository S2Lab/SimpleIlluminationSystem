package firok.sis.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import java.awt.BorderLayout;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.Font;
import javax.swing.SwingConstants;

public class WinCMD {

	private JFrame frame;
	private JLabel lblNewLabel;
	private JProgressBar progressBar;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WinCMD window = new WinCMD();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public WinCMD() {
		initialize();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 99);
		
		progressBar = new JProgressBar();
		progressBar.setValue(50);
		frame.getContentPane().add(progressBar, BorderLayout.SOUTH);
		
		lblNewLabel = new JLabel("New label");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		frame.getContentPane().add(lblNewLabel, BorderLayout.CENTER);
	}

	public void setProcessStatus(String textIn,int valueIn)
	{
		setText(textIn);
		setProcessValue(valueIn);
	}
	public void setText(String textIn)
	{
		if(textIn!=null)
		{
			lblNewLabel.setText(textIn);
		}
	}
	public void setProcessValue(int valueIn)
	{
		if(valueIn<0)
			progressBar.setValue(valueIn);
		else if(valueIn>100)
			progressBar.setValue(100);
		else
			progressBar.setValue(valueIn);
	}
	public void show()
	{
		frame.setVisible(true);
	}
	public void hide()
	{
		frame.setVisible(false);
	}
}
