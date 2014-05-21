package server;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
import java.io.*;

/*
 * �������˵��������
 */
public class ChatServer extends JFrame implements ActionListener{

	public static int port = 8888;//����˵������˿�

	ServerSocket serverSocket;//�����Socket
	Image icon;//����ͼ��
	JComboBox combobox;//ѡ������Ϣ�Ľ�����
	JTextArea messageShow;//����˵���Ϣ��ʾ
	JScrollPane messageScrollPane;//��Ϣ��ʾ�Ĺ�����
	JTextField showStatus;//��ʾ�û�����״̬
	JLabel sendToLabel,messageLabel;
	JTextField sysMessage;//�������Ϣ�ķ���
	JButton sysMessageButton;//�������Ϣ�ķ��Ͱ�ť
	UserLinkList userLinkList;//�û�����

	//�����˵���
	JMenuBar jMenuBar = new JMenuBar(); 
	//�����˵���
	JMenu serviceMenu = new JMenu ("����(V)"); 
	//�����˵���
	JMenuItem portItem = new JMenuItem ("�˿�����(P)");
	JMenuItem startItem = new JMenuItem ("��������(S)");
	JMenuItem stopItem=new JMenuItem ("ֹͣ����(T)");
	JMenuItem exitItem=new JMenuItem ("�˳�(X)");
	
	JMenu helpMenu=new JMenu ("����(H)");
	JMenuItem helpItem=new JMenuItem ("����(H)");

	//����������
	JToolBar toolBar = new JToolBar();

	//�����������еİ�ť���
	JButton portSet;//�������������
	JButton startServer;//�������������
	JButton stopServer;//�رշ��������
	JButton exitButton;//�˳���ť
	
	//��ܵĴ�С
	Dimension faceSize = new Dimension(400, 600);
	
	ServerListen listenThread;

	JPanel downPanel ;
	GridBagLayout girdBag;
	GridBagConstraints girdBagCon;


	/**
	 * ����˹��캯��
	 */
	public ChatServer(){
		init();//��ʼ������

		//��ӿ�ܵĹر��¼�����
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		//���ÿ�ܵĴ�С
		this.setSize(faceSize);
		
		//��������ʱ���ڵ�λ��
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation( (int) (screenSize.width - faceSize.getWidth()) / 2,
						 (int) (screenSize.height - faceSize.getHeight()) / 2);
		this.setResizable(false);

		this.setTitle("�����ҷ����"); //���ñ���

		//����ͼ��
		icon = getImage("icon.gif");
		this.setIconImage(icon); //���ó���ͼ��
		show();

		//Ϊ����˵��������ȼ�'V'
		serviceMenu.setMnemonic('V');

		//Ϊ�˿����ÿ�ݼ�Ϊctrl+p
		portItem.setMnemonic ('P'); 
		portItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_P,InputEvent.CTRL_MASK));

		//Ϊ���������ݼ�Ϊctrl+s
		startItem.setMnemonic ('S'); 
		startItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_S,InputEvent.CTRL_MASK));

		//Ϊ�˿����ÿ�ݼ�Ϊctrl+T
		stopItem.setMnemonic ('T'); 
		stopItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_T,InputEvent.CTRL_MASK));

		//Ϊ�˳����ÿ�ݼ�Ϊctrl+x
		exitItem.setMnemonic ('X'); 
		exitItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_X,InputEvent.CTRL_MASK));

		//Ϊ�����˵��������ȼ�'H'
		helpMenu.setMnemonic('H');

		//Ϊ�������ÿ�ݼ�Ϊctrl+p
		helpItem.setMnemonic ('H'); 
		helpItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_H,InputEvent.CTRL_MASK));

	}
	
	/**
	 * �����ʼ������
	 */
	public void init(){

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//��Ӳ˵���
		serviceMenu.add (portItem);
		serviceMenu.add (startItem);
		serviceMenu.add (stopItem);
		serviceMenu.add (exitItem);
		jMenuBar.add (serviceMenu); 
		helpMenu.add (helpItem);
		jMenuBar.add (helpMenu); 
		setJMenuBar (jMenuBar);

		//��ʼ����ť
		portSet = new JButton("�˿�����");
		startServer = new JButton("��������");
		stopServer = new JButton("ֹͣ����" );
		exitButton = new JButton("�˳�" );
		//����ť��ӵ�������
		toolBar.add(portSet);
		toolBar.addSeparator();//��ӷָ���
		toolBar.add(startServer);
		toolBar.add(stopServer);
		toolBar.addSeparator();//��ӷָ���
		toolBar.add(exitButton);
		contentPane.add(toolBar,BorderLayout.NORTH);

		//��ʼʱ����ֹͣ����ť������
		stopServer.setEnabled(false);
		stopItem .setEnabled(false);

		//Ϊ�˵�������¼�����
		portItem.addActionListener(this);
		startItem.addActionListener(this);
		stopItem.addActionListener(this);
		exitItem.addActionListener(this);
		helpItem.addActionListener(this);
		
		//��Ӱ�ť���¼�����
		portSet.addActionListener(this);
		startServer.addActionListener(this);
		stopServer.addActionListener(this);
		exitButton.addActionListener(this);
		
		combobox = new JComboBox();
		combobox.insertItemAt("������",0);
		combobox.setSelectedIndex(0);
		
		messageShow = new JTextArea();
		messageShow.setEditable(false);
		//��ӹ�����
		messageScrollPane = new JScrollPane(messageShow,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		messageScrollPane.setPreferredSize(new Dimension(400,400));
		messageScrollPane.revalidate();
		
		showStatus = new JTextField(35);
		showStatus.setEditable(false);
		
		sysMessage = new JTextField(24);
		sysMessage.setEnabled(false);
		sysMessageButton = new JButton();
		sysMessageButton.setText("����");

		//���ϵͳ��Ϣ���¼�����
		sysMessage.addActionListener(this);
		sysMessageButton.addActionListener(this);

		sendToLabel = new JLabel("������:");
		messageLabel = new JLabel("������Ϣ:");
		downPanel = new JPanel();
		girdBag = new GridBagLayout();
		downPanel.setLayout(girdBag);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 0;
		girdBagCon.gridwidth = 3;
		girdBagCon.gridheight = 2;
		girdBagCon.ipadx = 5;
		girdBagCon.ipady = 5;
		JLabel none = new JLabel("    ");
		girdBag.setConstraints(none,girdBagCon);
		downPanel.add(none);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 2;
		girdBagCon.insets = new Insets(1,0,0,0);
		girdBagCon.ipadx = 5;
		girdBagCon.ipady = 5;
		girdBag.setConstraints(sendToLabel,girdBagCon);
		downPanel.add(sendToLabel);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx =1;
		girdBagCon.gridy = 2;
		girdBagCon.anchor = GridBagConstraints.LINE_START;
		girdBag.setConstraints(combobox,girdBagCon);
		downPanel.add(combobox);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 3;
		girdBag.setConstraints(messageLabel,girdBagCon);
		downPanel.add(messageLabel);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 1;
		girdBagCon.gridy = 3;
		girdBag.setConstraints(sysMessage,girdBagCon);
		downPanel.add(sysMessage);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 2;
		girdBagCon.gridy = 3;
		girdBag.setConstraints(sysMessageButton,girdBagCon);
		downPanel.add(sysMessageButton);

		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 4;
		girdBagCon.gridwidth = 3;
		girdBag.setConstraints(showStatus,girdBagCon);
		downPanel.add(showStatus);

		contentPane.add(messageScrollPane,BorderLayout.CENTER);
		contentPane.add(downPanel,BorderLayout.SOUTH);
		
		//�رճ���ʱ�Ĳ���
		this.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					stopService();
					System.exit(0);
				}
			}
		);
	}

	/**
	 * �¼�����
	 */
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == startServer || obj == startItem) { //���������
			startService();
		}
		else if (obj == stopServer || obj == stopItem) { //ֹͣ�����
			int j=JOptionPane.showConfirmDialog(
				this,"���ֹͣ������?","ֹͣ����",
				JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE);
			
			if (j == JOptionPane.YES_OPTION){
				stopService();
			}
		}
		else if (obj == portSet || obj == portItem) { //�˿�����
			//�����˿����õĶԻ���
			PortConf portConf = new PortConf(this);
			portConf.show();
		}
		else if (obj == exitButton || obj == exitItem) { //�˳�����
			int j=JOptionPane.showConfirmDialog(
				this,"���Ҫ�˳���?","�˳�",
				JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE);
			
			if (j == JOptionPane.YES_OPTION){
				stopService();
				System.exit(0);
			}
		}
		else if (obj == helpItem) { //�˵����еİ���
			//���������Ի���
			Help helpDialog = new Help(this);
			helpDialog.show();
		}
		else if (obj == sysMessage || obj == sysMessageButton) { //����ϵͳ��Ϣ
			sendSystemMessage();
		}
	}
	
	/**
	 * ���������
	 */
	public void startService(){
		try{
			serverSocket = new ServerSocket(port,10);
			messageShow.append("������Ѿ���������"+port+"�˿�����...\n");
			
			startServer.setEnabled(false);
			startItem.setEnabled(false);
			portSet.setEnabled(false);
			portItem.setEnabled(false);

			stopServer .setEnabled(true);
			stopItem .setEnabled(true);
			sysMessage.setEnabled(true);
		}
		catch (Exception e){
			//System.out.println(e);
		}
		userLinkList = new UserLinkList();
		
		listenThread = new ServerListen(serverSocket,combobox,
			messageShow,showStatus,userLinkList);
		listenThread.start();
	}
	
	/**
	 * �رշ����
	 */
	public void stopService(){
		try{
			//�������˷��ͷ������رյ���Ϣ
			sendStopToAll();
			listenThread.isStop = true;
			serverSocket.close();
			
			int count = userLinkList.getCount();
			
			int i =0;
			while( i < count){
				Node node = userLinkList.findUser(i);
				
				node.input .close();
				node.output.close();
				node.socket.close();
				
				i ++;
			}

			stopServer .setEnabled(false);
			stopItem .setEnabled(false);
			startServer.setEnabled(true);
			startItem.setEnabled(true);
			portSet.setEnabled(true);
			portItem.setEnabled(true);
			sysMessage.setEnabled(false);

			messageShow.append("������Ѿ��ر�\n");

			combobox.removeAllItems();
			combobox.addItem("������");
		}
		catch(Exception e){
			//System.out.println(e);
		}
	}
	
	/**
	 * �������˷��ͷ������رյ���Ϣ
	 */
	public void sendStopToAll(){
		int count = userLinkList.getCount();
		
		int i = 0;
		while(i < count){
			Node node = userLinkList.findUser(i);
			if(node == null) {
				i ++;
				continue;
			}
			
			try{
				node.output.writeObject("����ر�");
				node.output.flush();
			}
			catch (Exception e){
				//System.out.println("$$$"+e);
			}
			
			i++;
		}
	}
	
	/**
	 * �������˷�����Ϣ
	 */
	public void sendMsgToAll(String msg){
		int count = userLinkList.getCount();//�û�����
		
		int i = 0;
		while(i < count){
			Node node = userLinkList.findUser(i);
			if(node == null) {
				i ++;
				continue;
			}
			
			try{
				node.output.writeObject("ϵͳ��Ϣ");
				node.output.flush();
				node.output.writeObject(msg);
				node.output.flush();
			}
			catch (Exception e){
				//System.out.println("@@@"+e);
			}
			
			i++;
		}

		sysMessage.setText("");
	}

	/**
	 * ��ͻ����û�������Ϣ
	 */
	public void sendSystemMessage(){
		String toSomebody = combobox.getSelectedItem().toString();
		String message = sysMessage.getText() + "\n";
		
		messageShow.append(message);
		
		//�������˷�����Ϣ
		if(toSomebody.equalsIgnoreCase("������")){
			sendMsgToAll(message);
		}
		else{
			//��ĳ���û�������Ϣ
			Node node = userLinkList.findUser(toSomebody);
			
			try{
				node.output.writeObject("ϵͳ��Ϣ");
				node.output.flush();
				node.output.writeObject(message);
				node.output.flush();
			}
			catch(Exception e){
				//System.out.println("!!!"+e);
			}
			sysMessage.setText("");//��������Ϣ������Ϣ���
		}
	}

	/**
	 * ͨ���������ļ������ͼ��
	 */
	Image getImage(String filename) {
		URLClassLoader urlLoader = (URLClassLoader)this.getClass().
			getClassLoader();
		URL url = null;
		Image image = null;
		url = urlLoader.findResource(filename);
		image = Toolkit.getDefaultToolkit().getImage(url);
		MediaTracker mediatracker = new MediaTracker(this);
		try {
			mediatracker.addImage(image, 0);
			mediatracker.waitForID(0);
		}
		catch (InterruptedException _ex) {
			image = null;
		}
		if (mediatracker.isErrorID(0)) {
			image = null;
		}

		return image;
	}

	public static void main(String[] args) {
		ChatServer app = new ChatServer();
	}
}
