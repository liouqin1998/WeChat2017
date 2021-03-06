package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
import model.User;
import view.ServerFrame.ClientMessageReciveThread;

public class ServerFrame extends JFrame {

	private Map<String, ObjectOutputStream> allClient = new HashMap<>();
	private ServerSocket server;

	private AllButtonListener listener;
	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JLabel lblNewLabel;
	private Object[] tableTitle = new Object[] { "用户IP", "用户ID" };
	private JTable table;
	private TableModel model;
	private JTextArea textArea;
	private JLabel lblNewLabel_1;
	private Panel panel;
	private JButton button;
	private JButton button_1;
	private JButton btnNewButton;

	{
		listener = new AllButtonListener();
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

	public ServerFrame() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(ServerFrame.class.getResource("/com/sun/java/swing/plaf/windows/icons/Computer.gif")));
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

		model = new DefaultTableModel(tableTitle, 0);
		table = new JTable(model);
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(20, 69, 241, 296);
		contentPane.add(scrollPane);

		lblNewLabel_1 = new JLabel("所有聊天消息");
		lblNewLabel_1.setBorder(BorderFactory.createLineBorder(Color.gray));
		lblNewLabel_1.setBounds(282, 43, 126, 15);
		contentPane.add(lblNewLabel_1);

		textArea = new JTextArea();
		textArea.setBounds(280, 69, 292, 294);
		contentPane.add(textArea);
		textArea.setTabSize(5);
		textArea.setEditable(false);

		button = new JButton("start");
		button.setBounds(20, 10, 116, 23);
		button.addActionListener(listener);
		contentPane.add(button);

		button_1 = new JButton("stop");
		button_1.setBounds(176, 10, 114, 23);
		contentPane.add(button_1);
		button_1.setEnabled(false);
		button_1.addActionListener(listener);

		setLocationRelativeTo(null);
	}

	class AllButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==button) {
					try {
						server=new ServerSocket(ServerFrameUIConfig.serverPort);
						button.setEnabled(false);//设置启动按钮为不可用
						button_1.setEnabled(true);
						JOptionPane.showMessageDialog(ServerFrame.this, "服务器启动成功!", "温馨提示", JOptionPane.INFORMATION_MESSAGE);
						//启动按钮除了要创建server对象之外，还要开启对外服务，accpet,
						new Thread() {
							public void run() {
								while(true)
								{
									try {
										Socket  c=server.accept();
										System.out.println(c.getInetAddress());
										ObjectOutputStream  out=new ObjectOutputStream(c.getOutputStream());
										ObjectInputStream  in=new ObjectInputStream(c.getInputStream());
										//应该在有一个客户端链接进来之后，我就开启一个线程，针对他单独和服务器通讯
										
										ClientMessageReciveThread  thisClientThread=new ClientMessageReciveThread(out, in);
										thisClientThread.start();//启动这个线程，让他独立运行
									} catch (Exception e2) {
									}
								}
							}
						}.start();
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(ServerFrame.this, "啊奥，服务器启动失败!", "温馨提示", JOptionPane.ERROR_MESSAGE);
					}
			}else if(e.getSource()==button_1) {
				int n=JOptionPane.showConfirmDialog(ServerFrame.this, "您确认要关闭服务器吗?", "温馨提示", JOptionPane.OK_CANCEL_OPTION);
				if(n==0)
				{
					try {
						server.close();
						button.setEnabled(true);
						button_1.setEnabled(false);
						JOptionPane.showMessageDialog(ServerFrame.this, "服务器已经关闭!", "温馨提示", JOptionPane.WARNING_MESSAGE);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	class ClientMessageReciveThread extends Thread{
		private ObjectOutputStream  out;
		private ObjectInputStream in;
		
		public ClientMessageReciveThread(ObjectOutputStream out, ObjectInputStream in) {
			super();
			this.out = out;
			this.in = in;
		}

		//服务端的代码都在这里了，
		@Override
		public void run() {
			try {
				while(true)//不停的读取客户端发送过来的消息
				{
					MessageBox  m=(MessageBox)in.readObject();//当前这个线程接收到这个客户端发送过来的一个Message对象
					System.out.println(m);
					
					if(m.getType().equals("login")) {
						processLoginMessage(m);
					}else if(m.getType().equals("register")) {
						processRegisterMessage(m);
					}else if(m.getType().equals("textMessage")|m.getType().equals("shakeMessage")) {
						processTextMessage(m);
					}else if(m.getType().equals("update")) {
						
					}
					

				}
			} catch (Exception e1) {
				e1.printStackTrace() ;
			}
		}
		/**
		 * 这是处理群消息的方法
		 * @param m
		 */
		
		/**
		 * 处理普通转发的文本消息的方法
		 * @param m
		 */
		private  void processTextMessage(MessageBox  m) {
			//当服务器接收到这个用户发送过来的文本消息的时候，我们就要遍历那个全局的集合，找到这个消息接收方的对应的输出流，把消息写给他
			for (String username:allClient.keySet()) {
				
				if(username.equals(m.getTo().getUsername())) {
					m.setTime(new Date().toLocaleString());//在即将转发消息之前，将服务器上取到的时间设置到该消息对象里面，方便接收方显式正确的消息
					try {
						allClient.get(username).writeObject(m);
						allClient.get(username).flush();
						System.out.println("zhaodaole .xiechuqu ");
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
			}
			
			/*//服务器再给消息发送方回发一个消息，把事件传递回去，让发送方可以显式正确的事件
			MessageBox  timeMessage=new MessageBox();
			timeMessage.setTime(new Date().toLocaleString());
			try {
				out.writeObject(timeMessage);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
		}
		/**
		 * 这是处理注册消息的代码
		 * @param m
		 */
		private void processRegisterMessage(MessageBox m) {
			
			User  willResgisterUser=m.getFrom();
			Boolean result=DBOperator.register(willResgisterUser);
			
			MessageBox  registerResultMessage=new MessageBox();
			registerResultMessage.setContent(result.toString());
			registerResultMessage.setType("registerResult");
			try {
				out.writeObject(registerResultMessage);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		/**
		 * 定義一個處理登陸消息的方法
		 * @param m
		 */
		private void processLoginMessage(MessageBox  m) {
			//链接数据库判断用户登陆信息是否正确
			User loginedUser=DBOperator.login(m.getFrom().getUsername(), m.getFrom().getPassword());
			
			if(loginedUser!=null) {
				allClient.put(loginedUser.getUsername(), out);//在登陆成功后将该登陆的号码和对应的通讯流存储到服务器的这个全局集合里
			//如果登陆成功，需要更新服务器窗口上显式的用户列表信息
			model=new DefaultTableModel(new Object[][] {{loginedUser.getUsername(),loginedUser.getNickname()}}, tableTitle);
			table.setModel(model);
			}
			//当服务器根据传过来的用户名和密码查询完数据库之后，无论登陆成功还失败都要给用户回一个消息(都要封装成MessageBox)
			MessageBox  loginResult=new MessageBox();
			loginResult.setFrom(loginedUser);
			loginResult.setType("loginResult");
			try {
				out.writeObject(loginResult);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
}