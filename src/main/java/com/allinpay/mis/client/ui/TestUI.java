package com.allinpay.mis.client.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.allinpay.mis.client.ui.panel.LoginPanel;

public class TestUI {
	
	private JFrame frame;
	
	
	public TestUI() {
		initialize();
	}
	
	/**
     * 程序入口main
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	TestUI window = new TestUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    
    /**
     * 初始化frame内容
     */
    private void initialize() {
        // 设置系统默认样式
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // 初始化主窗口
        frame = new JFrame();
        frame.setBounds(ConstantsUI.MAIN_WINDOW_X, ConstantsUI.MAIN_WINDOW_Y, ConstantsUI.MAIN_WINDOW_WIDTH,
                ConstantsUI.MAIN_WINDOW_HEIGHT);
        frame.setTitle(ConstantsUI.APP_NAME);
        frame.setIconImage(ConstantsUI.IMAGE_ICON);
        frame.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        LoginPanel mainPanel = new LoginPanel();
        frame.add(mainPanel);
    }
    
}
