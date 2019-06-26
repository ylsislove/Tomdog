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
	
	// 面板组件
    private JPanel pnlEast;
	private JPanel pnlSouth;
    private JPanel pnlWest;
    private JPanel pnlNorth;
    private JPanel pnlCenter;
    private JPanel pnlNorthEast;
    private JPanel pnlNorthWest;
    
    // 按钮组件
    private JButton btnStartUp;
    private JButton btnBrowseSource;
    private JButton btnShupDown;
    
    // Label组件
    private JLabel lblSource;
    private JLabel lblPort;
    
    // Text组件，显示网站源目录和端口编辑框
    private JTextField txtSource;
    private JTextField txtPort;
//    private static JTextArea txtArea;
    private static JTextPane txtArea;
    
    // 滚动窗格组件
    private JScrollPane scroll;
    
    // 固定同步按钮的大小
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
	
	// 构造函数
	public MainUI() {
		createGUI();
	}
	
	// 创建图形化界面
	private void createGUI() {
		mainFrame = new JFrame("Tomdog");
		mainFrame.setMinimumSize(new Dimension(900, 600));
		mainFrame.getContentPane().setLayout(new BorderLayout(0, 0));
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);       
        
        // 主框架的南部的面板用于启动按钮和关闭按钮
        pnlSouth = new JPanel();
        mainFrame.getContentPane().add(pnlSouth, BorderLayout.SOUTH);
        pnlSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlSouth.setPreferredSize(new Dimension(900, 70));
        
        btnStartUp = new JButton("开启！");
        btnStartUp.setFont(btnStartUp.getFont().deriveFont(Font.BOLD));
        btnStartUp.setToolTipText("运行服务！");
        btnStartUp.setPreferredSize(new Dimension(BUTTON_SIZE[0], BUTTON_SIZE[1]));

        btnShupDown = new JButton("关闭！");
        btnShupDown.setFont(btnShupDown.getFont().deriveFont(Font.BOLD));
        btnShupDown.setToolTipText("关闭服务");
        btnShupDown.setPreferredSize(new Dimension(BUTTON_SIZE[0], BUTTON_SIZE[1]));
        
        pnlSouth.add(btnStartUp);
        pnlSouth.add(btnShupDown);
        
        // 主框架的西部面板设置为文件列表和主框架之间的间隔
        pnlWest = new JPanel();
        mainFrame.getContentPane().add(pnlWest, BorderLayout.WEST);
        pnlWest.setPreferredSize(new Dimension(20, 300));
        
        // 主框架的东部面板设置同上
        pnlEast = new JPanel();
        mainFrame.getContentPane().add(pnlEast, BorderLayout.EAST);
        pnlEast.setPreferredSize(new Dimension(20, 300));
        
        // 中心区域面板则是文件输出位置
        pnlCenter = new JPanel();
        mainFrame.getContentPane().add(pnlCenter, BorderLayout.CENTER);
        pnlCenter.setLayout(new BorderLayout(0, 0));
        
        // 将txtArea设置在中心面板区域
        txtArea = new JTextPane();
        txtArea.setBackground(new Color(232, 232, 232));
        // 设置内边距
        txtArea.setMargin(new Insets(5, 10, 10, 5));
        pnlCenter.add(txtArea, BorderLayout.CENTER);
        // 将滚动条放置在中心区域面板中
        scroll = new JScrollPane();
        pnlCenter.add(scroll, BorderLayout.CENTER);
        scroll.setViewportView(txtArea);
        
        
        // 主框架的北部面板设置为，目录路径，以及端口编辑框
        pnlNorth = new JPanel();
        mainFrame.getContentPane().add(pnlNorth, BorderLayout.NORTH);
        pnlNorth.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        // 西北方向设置为目录(Source directory)
        pnlNorthWest = new JPanel();
        pnlNorth.add(pnlNorthWest, BorderLayout.WEST);
        pnlNorthWest.setPreferredSize(new Dimension(400, 70));
        pnlNorthWest.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));

        lblSource = new JLabel("网站源目录：");
        txtSource = new JTextField(17);
        btnBrowseSource = new JButton("打开");
        btnBrowseSource.setToolTipText("选择一个源目录");

        pnlNorthWest.add(lblSource);
        pnlNorthWest.add(txtSource);
        pnlNorthWest.add(btnBrowseSource);
        
        // 东北方向设置为端口编辑框(Port Edit)
        pnlNorthEast = new JPanel();
        pnlNorth.add(pnlNorthEast, BorderLayout.EAST);
        pnlNorthEast.setPreferredSize(new Dimension(400, 70));
        pnlNorthEast.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));

        lblPort = new JLabel("服务器端口：");
        txtPort = new JTextField(7);
        txtPort.setText("80");

        pnlNorthEast.add(lblPort);
        pnlNorthEast.add(txtPort);
        
        // 正北方向设置为清空按钮
//        pnlNorthCenter = new JPanel();
//        pnlNorth.add(pnlNorthCenter, BorderLayout.CENTER);
//        pnlNorthCenter.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        
        // 开启服务器！
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
        
        // 关闭服务器按钮事件
        btnShupDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
        
        // 设置Browse按钮事件
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
        
        
        // 设置键盘事件，用户可以通过键盘输入路径，回车表示确定路径
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
		// 设置颜色
		StyleConstants.setForeground(attrSet, col);
		// 设置字体大小
		StyleConstants.setFontSize(attrSet, 22);
		// 得到txtpane的document对象
		doc = txtArea.getDocument();
		try {
			doc.insertString(doc.getLength(), msg + "\n", attrSet);
		} catch (BadLocationException e) {
			log.error("setMsg error!");
		}
        // 设置滚动条自动滚动
        txtArea.setCaretPosition(txtArea.getStyledDocument().getLength());
    }
}
