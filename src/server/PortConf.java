package server;

import java.awt.*;
import javax.swing.border.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * ���ɶ˿����öԻ������
 */
public class PortConf extends JDialog {
	JPanel panelPort = new JPanel();
	JButton save = new JButton();
	JButton cancel = new JButton();
	public static JLabel DLGINFO=new JLabel(
		"                              Ĭ�϶˿ں�Ϊ:8888");

	JPanel panelSave = new JPanel();
	JLabel message = new JLabel();

	public static JTextField portNumber ;

	public PortConf(JFrame frame) {
		super(frame, true);
		try {
			jbInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//��������λ�ã�ʹ�Ի������
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation( (int) (screenSize.width - 400) / 2 + 50,
						(int) (screenSize.height - 600) / 2 + 150);
		this.setResizable(false);
	}

	private void jbInit() throws Exception {
		this.setSize(new Dimension(300, 120));
		this.setTitle("�˿�����");
		message.setText("�����������Ķ˿ں�:");
		portNumber = new JTextField(10);
		portNumber.setText(""+ChatServer.port);
		save.setText("����");
		cancel.setText("ȡ��");

		panelPort.setLayout(new FlowLayout());
		panelPort.add(message);
		panelPort.add(portNumber);

		panelSave.add(new Label("              "));
		panelSave.add(save);
		panelSave.add(cancel);
		panelSave.add(new Label("              "));

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(panelPort, BorderLayout.NORTH);
		contentPane.add(DLGINFO, BorderLayout.CENTER);
		contentPane.add(panelSave, BorderLayout.SOUTH);

		//���水ť���¼�����
		save.addActionListener(
			new ActionListener() {
				public void actionPerformed (ActionEvent a) {
					int savePort;
					try{
						
						savePort=Integer.parseInt(PortConf.portNumber.getText());

						if(savePort<1 || savePort>65535){
							PortConf.DLGINFO.setText("               �����˿ڱ�����0-65535֮�������!");
							PortConf.portNumber.setText("");
							return;
						}
						ChatServer.port = savePort;
						dispose();
					}
					catch(NumberFormatException e){
						PortConf.DLGINFO.setText("                ����Ķ˿ں�,�˿ں�����д����!");
						PortConf.portNumber.setText("");
						return;
					}
				}
			}
		);

		//�رնԻ���ʱ�Ĳ���
		this.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					DLGINFO.setText("                              Ĭ�϶˿ں�Ϊ:8888");
				}
			}
		);

		//ȡ����ť���¼�����
		cancel.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					DLGINFO.setText("                              Ĭ�϶˿ں�Ϊ:8888");
					dispose();
				}
			}
		);
	}
}
