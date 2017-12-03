package view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import control.ClientFrameUIConfig;
import control.ServerFrameUIConfig;
import model.MessageBox;
import model.User;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

public class LoginFrame extends JFrame {

	private Socket client;

	private ObjectOutputStream out;
	private ObjectInputStream in;
	private JPanel contentPane;
	private JPasswordField passwordField;
	private JComboBox comboBox;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private Button button_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Window w = new LoginFrame();
				w.setVisible(true);

			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		setBackground(SystemColor.inactiveCaptionText);
		setResizable(false);
		// TODO Auto-generated constructor stub

		{
			setIconImage(Toolkit.getDefaultToolkit().getImage(LoginFrame.class.getResource("")));
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(389, 233);
			setTitle("\u767B\u5F55");
			setLocation(50, 50);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(null);

			comboBox = new JComboBox(new Object[] { "1111", "2222"});
			comboBox.setEditable(true);
			comboBox.setBounds(113, 72, 178, 21);
			contentPane.add(comboBox);

			passwordField = new JPasswordField("");
			passwordField.setBounds(113, 103, 178, 21);
			contentPane.add(passwordField);

			Button button = new Button("登录");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String InputUsername = comboBox.getSelectedItem().toString().trim();
					String InputPassword = passwordField.getText().toString().trim();
					if (InputUsername.length() < 4 || InputPassword.length() < 4) {
						JOptionPane.showMessageDialog(LoginFrame.this, "用户名与密码长度应不少于4位", "提示",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						try {
							if (client == null) {
								client = new Socket(ServerFrameUIConfig.serverIP, ServerFrameUIConfig.serverPort);
								
							}
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							JOptionPane.showMessageDialog(LoginFrame.this, "登录失败：无法连接服务器", "错误",
									JOptionPane.ERROR_MESSAGE);
						}
						try {
							ObjectOutputStream out=new ObjectOutputStream(client.getOutputStream());
							
							MessageBox loginMessage=new MessageBox();
							User willLoginUser=new User(InputUsername,InputPassword);
							loginMessage.setFrom(willLoginUser);
							loginMessage.setType("login");
							out.writeObject(loginMessage);
							out.flush();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			});
			button.setBounds(51, 149, 76, 23);
			contentPane.add(button);

			button_1 = new Button("注册");
			button_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
			button_1.setBounds(225, 149, 76, 23);
			contentPane.add(button_1);

		}
	}
}