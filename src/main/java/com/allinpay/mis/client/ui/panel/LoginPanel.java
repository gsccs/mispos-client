package com.allinpay.mis.client.ui.panel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.allinpay.mis.client.ui.AppMainWindow;
import com.allinpay.mis.client.ui.ConstantsUI;
import com.allinpay.mis.client.ui.MyIconButton;
import com.allinpay.mis.logic.ConstantsLogic;
import com.allinpay.mis.tools.ConstantsTools;
import com.allinpay.mis.tools.PropUtil;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 登录面板
 *
 * @author x.d zhang
 */
public class LoginPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static MyIconButton buttonSave;

    private static JCheckBox checkBoxAutoBak;

    private static JCheckBox checkBoxDebug;

    private static JCheckBox checkBoxStrict;

    private static JTextField textFieldUserName;
    private static JTextField textFieldPassword;

    private static final Logger logger = LoggerFactory.getLogger(LoginPanel.class);

    /**
     * 构造
     */
    public LoginPanel() {
        initialize();
        addComponent();
        setCurrentOption();
        addListener();
    }

    /**
     * 初始化
     */
    private void initialize() {
        this.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        this.setLayout(new BorderLayout());
    }

    /**
     * 添加组件
     */
    private void addComponent() {
    	
        this.add(getCenterPanel(), BorderLayout.CENTER);
        this.add(getDownPanel(), BorderLayout.SOUTH);

    }

    /**
     * 中部面板
     *
     * @return
     */
    private JPanel getCenterPanel() {
        // 中间面板
        JPanel panelCenter = new JPanel();
        panelCenter.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelCenter.setLayout(new GridLayout(1, 1));
        panelCenter.setBorder(BorderFactory.createLineBorder(Color.red));
        
        // 设置Grid
        JPanel panelGridOption = new JPanel();
        panelGridOption.setBorder(BorderFactory.createLineBorder(Color.green));
        panelGridOption.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelGridOption.setLayout(new FlowLayout(FlowLayout.CENTER, ConstantsUI.MAIN_H_GAP, 15));

        // 初始化组件
        JPanel panelItem1 = new JPanel(new FlowLayout(FlowLayout.CENTER, ConstantsUI.MAIN_H_GAP, 0));
        JPanel panelItem2 = new JPanel(new FlowLayout(FlowLayout.CENTER, ConstantsUI.MAIN_H_GAP, 0));
        JPanel panelItem3 = new JPanel(new FlowLayout(FlowLayout.CENTER, ConstantsUI.MAIN_H_GAP, 0));
        JPanel panelItem4 = new JPanel(new FlowLayout(FlowLayout.CENTER, ConstantsUI.MAIN_H_GAP, 0));
        JPanel panelItem5 = new JPanel(new FlowLayout(FlowLayout.CENTER, ConstantsUI.MAIN_H_GAP, 0));

        panelItem1.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelItem2.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelItem3.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelItem4.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelItem5.setBackground(ConstantsUI.MAIN_BACK_COLOR);

        //panelItem1.setPreferredSize(ConstantsUI.PANEL_ITEM_SIZE);
        Dimension LOGO_ITEM_SIZE = new Dimension(1300, 200);
        panelItem1.setPreferredSize(LOGO_ITEM_SIZE);
        panelItem2.setPreferredSize(ConstantsUI.PANEL_ITEM_SIZE);
        panelItem3.setPreferredSize(ConstantsUI.PANEL_ITEM_SIZE);
        panelItem4.setPreferredSize(ConstantsUI.PANEL_ITEM_SIZE);
        panelItem5.setPreferredSize(ConstantsUI.PANEL_ITEM_SIZE);

        // 各Item
        Dimension dmLabel = new Dimension(100, 26);
        JLabel labelUserName = new JLabel(PropUtil.getProperty("ds.ui.login.username"));
        labelUserName.setPreferredSize(dmLabel);
        textFieldUserName = new JTextField();
        labelUserName.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        labelUserName.setFont(ConstantsUI.FONT_RADIO);
        textFieldUserName.setFont(ConstantsUI.FONT_RADIO);
        Dimension dm = new Dimension(334, 26);
        textFieldUserName.setPreferredSize(dm);
        panelItem2.add(labelUserName);
        panelItem2.add(textFieldUserName);
        
        JLabel labelPassword = new JLabel(PropUtil.getProperty("ds.ui.login.password"));
        labelPassword.setPreferredSize(dmLabel);
        textFieldPassword = new JTextField();
        labelPassword.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        labelPassword.setFont(ConstantsUI.FONT_RADIO);
        textFieldPassword.setFont(ConstantsUI.FONT_RADIO);
        Dimension dmPassword = new Dimension(334, 26);
        textFieldPassword.setPreferredSize(dmPassword);
        panelItem3.add(labelPassword);
        panelItem3.add(textFieldPassword);

        checkBoxStrict = new JCheckBox(PropUtil.getProperty("ds.ui.setting.strict"));
        checkBoxStrict.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        checkBoxStrict.setFont(ConstantsUI.FONT_RADIO);
        panelItem4.add(checkBoxStrict);

        checkBoxDebug = new JCheckBox(PropUtil.getProperty("ds.ui.setting.debugMode"));
        checkBoxDebug.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        checkBoxDebug.setFont(ConstantsUI.FONT_RADIO);
        panelItem5.add(checkBoxDebug);

        // 组合元素
        panelGridOption.add(panelItem1);
        panelGridOption.add(panelItem2);
        panelGridOption.add(panelItem3);
        panelGridOption.add(panelItem4);
        panelGridOption.add(panelItem5);
        panelGridOption.setSize(600, 400);
        panelCenter.add(panelGridOption);
        
        return panelCenter;
    }

    /**
     * 底部面板
     *
     * @return
     */
    private JPanel getDownPanel() {
        JPanel panelDown = new JPanel();
        panelDown.setBackground(ConstantsUI.MAIN_BACK_COLOR);
        panelDown.setLayout(new FlowLayout(FlowLayout.RIGHT, ConstantsUI.MAIN_H_GAP, 15));

        buttonSave = new MyIconButton(ConstantsUI.ICON_SAVE, ConstantsUI.ICON_SAVE_ENABLE,
                ConstantsUI.ICON_SAVE_DISABLE, "");
        panelDown.add(buttonSave);

        return panelDown;
    }

    /**
     * 设置当前combox选项状态
     */
    public static void setCurrentOption() {
        //checkBoxAutoBak.setSelected(Boolean.parseBoolean(ConstantsTools.CONFIGER.getAutoBak()));
        //checkBoxDebug.setSelected(Boolean.parseBoolean(ConstantsTools.CONFIGER.getDebugMode()));
        //checkBoxStrict.setSelected(Boolean.parseBoolean(ConstantsTools.CONFIGER.getStrictMode()));
        //textFieldUserName.setText(ConstantsTools.CONFIGER.getMysqlPath());
    }

    /**
     * 为相关组件添加事件监听
     */
    private void addListener() {
        buttonSave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    //ConstantsTools.CONFIGER.setAutoBak(String.valueOf(checkBoxAutoBak.isSelected()));
                    //ConstantsTools.CONFIGER.setDebugMode(String.valueOf(checkBoxDebug.isSelected()));
                    //ConstantsTools.CONFIGER.setStrictMode(String.valueOf(checkBoxStrict.isSelected()));
                    //ConstantsTools.CONFIGER.setMysqlPath(textFieldUserName.getText());
                    JOptionPane.showMessageDialog(AppMainWindow.settingPanel, PropUtil.getProperty("ds.ui.save.success"),
                            PropUtil.getProperty("ds.ui.tips"), JOptionPane.PLAIN_MESSAGE);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(AppMainWindow.settingPanel, PropUtil.getProperty("ds.ui.save.fail") + e1.getMessage(),
                            PropUtil.getProperty("ds.ui.tips"),
                            JOptionPane.ERROR_MESSAGE);
                    logger.error("Write to xml file error" + e1.toString());
                }

            }
        });


        
    }
}
