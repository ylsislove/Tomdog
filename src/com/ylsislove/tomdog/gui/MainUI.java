package com.ylsislove.tomdog.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.ylsislove.tomdog.connector.http.Constants;
import com.ylsislove.tomdog.connector.http.HttpConnector;
import com.ylsislove.tomdog.log.Logger;


public class MainUI {

	private static Logger log = Logger.getLogger(MainUI.class);
	
	private JFrame mainFrame;
	
	// ������
    private JPanel pnlEast;
	private JPanel pnlSouth;
    private JPanel pnlWest;
    private JPanel pnlNorth;
    private JPanel pnlCenter;
    private JPanel pnlNorthEast;
    private JPanel pnlNorthWest;
    
    // ��ť���
    private JButton btnStartUp;
    private JButton btnBrowseSource;
    private JButton btnShupDown;
    
    // Label���
    private JLabel lblSource;
    private JLabel lblPort;
    
    // Text�������ʾ��վԴĿ¼�Ͷ˿ڱ༭��
    private JTextField txtSource;
    private JTextField txtPort;
//    private static JTextArea txtArea;
    private static JTextPane txtArea;
    
    // �����������
    private JScrollPane scroll;
    
    // �̶�ͬ����ť�Ĵ�С
    private Integer[] BUTTON_SIZE = {200, 40};
    
    private String sourcePath;
    private String port;
    
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				try {
					MainUI myGUI = new MainUI();
					myGUI.mainFrame.setVisible(true);
				} catch (Exception e){
					log.error("MainUI run error!");
				}
			}
		});
	}
	
	// ���캯��
	public MainUI() {
		createGUI();
	}
	
	// ����ͼ�λ�����
	private void createGUI() {
		mainFrame = new JFrame("Tomdog");
		mainFrame.setMinimumSize(new Dimension(900, 600));
		mainFrame.getContentPane().setLayout(new BorderLayout(0, 0));
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);       
        
        // ����ܵ��ϲ����������������ť�͹رհ�ť
        pnlSouth = new JPanel();
        mainFrame.getContentPane().add(pnlSouth, BorderLayout.SOUTH);
        pnlSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlSouth.setPreferredSize(new Dimension(900, 70));
        
        btnStartUp = new JButton("������");
        btnStartUp.setFont(btnStartUp.getFont().deriveFont(Font.BOLD));
        btnStartUp.setToolTipText("���з���");
        btnStartUp.setPreferredSize(new Dimension(BUTTON_SIZE[0], BUTTON_SIZE[1]));

        btnShupDown = new JButton("�رգ�");
        btnShupDown.setFont(btnShupDown.getFont().deriveFont(Font.BOLD));
        btnShupDown.setToolTipText("�رշ���");
        btnShupDown.setPreferredSize(new Dimension(BUTTON_SIZE[0], BUTTON_SIZE[1]));
        
        pnlSouth.add(btnStartUp);
        pnlSouth.add(btnShupDown);
        
        // ����ܵ������������Ϊ�ļ��б�������֮��ļ��
        pnlWest = new JPanel();
        mainFrame.getContentPane().add(pnlWest, BorderLayout.WEST);
        pnlWest.setPreferredSize(new Dimension(20, 300));
        
        // ����ܵĶ����������ͬ��
        pnlEast = new JPanel();
        mainFrame.getContentPane().add(pnlEast, BorderLayout.EAST);
        pnlEast.setPreferredSize(new Dimension(20, 300));
        
        // ����������������ļ����λ��
        pnlCenter = new JPanel();
        mainFrame.getContentPane().add(pnlCenter, BorderLayout.CENTER);
        pnlCenter.setLayout(new BorderLayout(0, 0));
        
        // ��txtArea�����������������
        txtArea = new JTextPane();
        txtArea.setBackground(new Color(232, 232, 232));
        // �����ڱ߾�
        txtArea.setMargin(new Insets(5, 10, 10, 5));
        pnlCenter.add(txtArea, BorderLayout.CENTER);
        // ���������������������������
        scroll = new JScrollPane();
        pnlCenter.add(scroll, BorderLayout.CENTER);
        scroll.setViewportView(txtArea);
        
        
        // ����ܵı����������Ϊ��Ŀ¼·�����Լ��˿ڱ༭��
        pnlNorth = new JPanel();
        mainFrame.getContentPane().add(pnlNorth, BorderLayout.NORTH);
        pnlNorth.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        // ������������ΪĿ¼(Source directory)
        pnlNorthWest = new JPanel();
        pnlNorth.add(pnlNorthWest, BorderLayout.WEST);
        pnlNorthWest.setPreferredSize(new Dimension(400, 70));
        pnlNorthWest.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));

        lblSource = new JLabel("��վԴĿ¼��");
        txtSource = new JTextField(17);
        btnBrowseSource = new JButton("��");
        btnBrowseSource.setToolTipText("ѡ��һ��ԴĿ¼");

        pnlNorthWest.add(lblSource);
        pnlNorthWest.add(txtSource);
        pnlNorthWest.add(btnBrowseSource);
        
        // ������������Ϊ�˿ڱ༭��(Port Edit)
        pnlNorthEast = new JPanel();
        pnlNorth.add(pnlNorthEast, BorderLayout.EAST);
        pnlNorthEast.setPreferredSize(new Dimension(400, 70));
        pnlNorthEast.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));

        lblPort = new JLabel("�������˿ڣ�");
        txtPort = new JTextField(7);
        txtPort.setText("80");

        pnlNorthEast.add(lblPort);
        pnlNorthEast.add(txtPort);
        
        // ������������Ϊ��հ�ť
//        pnlNorthCenter = new JPanel();
//        pnlNorth.add(pnlNorthCenter, BorderLayout.CENTER);
//        pnlNorthCenter.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        
        // ������������
        btnStartUp.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
        		
        		port = txtPort.getText();
                if ((sourcePath != null) || (port != null)) {
                	
                	Constants.setWebRoot(sourcePath);
                	Constants.setPort(Integer.parseInt(port));
                	HttpConnector connector = new HttpConnector();
            		connector.start();
                }
            }
        });
        
        // �رշ�������ť�¼�
        btnShupDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
        
        // ����Browse��ť�¼�
        btnBrowseSource.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int response = fc.showOpenDialog(mainFrame);
                if (response == JFileChooser.APPROVE_OPTION) {
                    sourcePath = fc.getSelectedFile().toString();
                    txtSource.setText(sourcePath);
                }
            }
        });
        
        
        // ���ü����¼����û�����ͨ����������·�����س���ʾȷ��·��
        txtSource.addKeyListener(new KeyAdapter() {
        	@Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sourcePath = txtSource.getText();
                }
            }
        });
        
        txtPort.addKeyListener(new KeyAdapter() {
        	@Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    port = txtPort.getText();
                }
            }
        });
	}
	
	private static Document doc;
	public static void setMsg(String msg, Color col) {
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		// ������ɫ
		StyleConstants.setForeground(attrSet, col);
		// ���������С
		StyleConstants.setFontSize(attrSet, 22);
		// �õ�txtpane��document����
		doc = txtArea.getDocument();
		try {
			doc.insertString(doc.getLength(), msg + "\n", attrSet);
		} catch (BadLocationException e) {
			log.error("setMsg error!");
		}
        // ���ù������Զ�����
        txtArea.setCaretPosition(txtArea.getStyledDocument().getLength());
    }
}
