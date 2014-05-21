import java.awt.*;
import javax.swing.border.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;


/**
 * �������öԻ������
 */
public class Help extends JDialog {

	JPanel titlePanel = new JPanel();
	JPanel contentPanel = new JPanel();
	JPanel closePanel = new JPanel();

	JButton close = new JButton();
	JLabel title = new JLabel("�����ҿͻ��˰���");
	JTextArea help = new JTextArea(); 

	Color bg = new Color(255,255,255);

	public Help(JFrame frame) {
		super(frame, true);
		try {
			jbInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//��������λ�ã�ʹ�Ի������
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation( (int) (screenSize.width - 400) / 2 + 25,
						(int) (screenSize.height - 320) / 2);
		this.setResizable(false);
	}

	private void jbInit() throws Exception {
		this.setSize(new Dimension(350, 270));
		this.setTitle("����");
		
		titlePanel.setBackground(bg);;
		contentPanel.setBackground(bg);
		closePanel.setBackground(bg);
		
		help.setText("1��������Ҫ���ӷ���˵�IP��ַ�Ͷ˿�"+
			"��Ĭ������Ϊ\n      127.0.0.1:8888����\n"+
			"2����������û�����Ĭ������Ϊ:�Ҵҹ��ͣ���\n"+
			"3���������¼����������ӵ�ָ���ķ�������\n"+
			"      �����ע�������Ժͷ������˿����ӡ�\n"+
			"4��ѡ����Ҫ������Ϣ���û�������Ϣ����д����Ϣ��\n"+
			"      ͬʱѡ����飬֮���ɷ�����Ϣ��\n");
		help.setEditable(false);

		titlePanel.add(new Label("              "));
		titlePanel.add(title);
		titlePanel.add(new Label("              "));

		contentPanel.add(help);

		closePanel.add(new Label("              "));
		closePanel.add(close);
		closePanel.add(new Label("              "));

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(titlePanel, BorderLayout.NORTH);
		contentPane.add(contentPanel, BorderLayout.CENTER);
		contentPane.add(closePanel, BorderLayout.SOUTH);

		close.setText("�ر�");
		//�¼�����
		close.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			}
		);
	}
}