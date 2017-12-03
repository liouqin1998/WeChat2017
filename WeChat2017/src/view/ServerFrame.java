package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import control.DBOperator;
import control.ServerFrameUIConfig;
import model.MessageBox;

public class ServerFrame extends JFrame{
	
	private ServerSocket server;

	private AllButtonListener  listener;
	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JLabel lblNewLabel;
	private Object[] tableTitle=new Object[]{"登陆IP","用户昵称"};
	private JTable table;
	private TableModel  model;
	private JTextArea textArea;
	private JLabel lblNewLabel_1;
	private Panel panel;
	private JButton button;
	private JButton button_1;
	private JButton btnNewButton;
	
	{
		listener=new AllButtonListener();
	}
	
	public ServerFrame() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ServerFrame.class.getResource("/com/sun/java/swing/plaf/windows/icons/Computer.gif")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(ServerFrameUIConfig.serverFrameTitle);
		setSize(588, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblNewLabel = new JLabel("在线用户");
		lblNewLabel.setBorder(BorderFactory.createLineBorder(Color.gray));
		lblNewLabel.setBounds(20, 43, 71, 15);
		contentPane.add(lblNewLabel);
		
		model=new DefaultTableModel(tableTitle,0) ;
		table = new JTable(model);
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(20, 69, 241, 296);
		contentPane.add(scrollPane);
		
		lblNewLabel_1 = new JLabel("所有用户发送的消息");
		lblNewLabel_1.setBorder(BorderFactory.createLineBorder(Color.gray));
		lblNewLabel_1.setBounds(282, 43, 126, 15);
		contentPane.add(lblNewLabel_1);
		
		textArea = new JTextArea();
		textArea.setBounds(280, 69, 292, 294);
		contentPane.add(textArea);
		textArea.setTabSize(5);
		textArea.setEditable(false);
		
		button = new JButton("启动服务器");
		button.setBounds(20, 10, 116, 23);
		button.addActionListener(listener);
		contentPane.add(button);
		
		button_1 = new JButton("停止服务器");
		button_1.setBounds(176, 10, 114, 23);
		contentPane.add(button_1);
		button_1.setEnabled(false);
		button_1.addActionListener(listener);

		
		
		setLocationRelativeTo(null);
	}
	class AllButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==button){
				System.out.println("start");
				try {
					server=new ServerSocket(ServerFrameUIConfig.serverPort);
					button.setEnabled(false);
					button_1.setEnabled(true);
					while(true){
						Socket c=server.accept();
						System.out.println(c.getInetAddress());
						ObjectInputStream in=new ObjectInputStream(c.getInputStream());
						try {
							MessageBox m=(MessageBox)in.readObject();
							if(m.getType().equals("login")){
								DBOperator.login(m.getFrom().getUsername(), m.getFrom().getPassword());
							}
							System.out.println(m);
						} catch (Exception e2) {
							// TODO: handle exception
						}
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(ServerFrame.this, "服务器启动失败","错误",JOptionPane.ERROR_MESSAGE);
				}
			}
			else if (e.getSource()==button_1) {
				System.out.println("stop");
				try {
					server.close();
					button.setEnabled(true);
					button_1.setEnabled(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} {
				
			}
		}
		
	}

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerFrame frame = new ServerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
