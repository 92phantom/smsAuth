package com.smsActivate.ui;

import com.smsActivate.ui.user.UserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import ru.sms_activate.error.base.SMSActivateBaseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.charset.StandardCharsets;

@Component
@SpringBootApplication
public class ApiAuthFrame extends JFrame {

    @Autowired
    UserImpl user;

    @Autowired
    ShowDataFrame showDataFrame;

    private JTextField apiKeyTextField;
    private JLabel authResult_lbl;

    private String apiKey = "";


    public ApiAuthFrame() {

        initUI();

    }

    private void initUI() {

        apiKey_load();

        setBounds(100, 100, 386, 99);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("API Key");
        lblNewLabel.setBounds(16, 6, 57, 38);
        getContentPane().add(lblNewLabel);

        apiKeyTextField = new JTextField();
        apiKeyTextField.setBounds(75, 6, 210, 38);
        getContentPane().add(apiKeyTextField);
        apiKeyTextField.setColumns(10);
        apiKeyTextField.setText(apiKey);

        authResult_lbl = new JLabel("");
        authResult_lbl.setForeground(Color.RED);
        authResult_lbl.setBounds(26, 56, 259, 16);
        getContentPane().add(authResult_lbl);

        JButton ok_btn = new JButton("OK");
        ok_btn.setBounds(292, 11, 69, 29);
        getContentPane().add(ok_btn);

        apiKeyTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if(key == KeyEvent.VK_ENTER){
                    actionLogin();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        ok_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                actionLogin();
//                String API_KEY = apiKeyTextField.getText();
//                user.setAPI_KEY(API_KEY);
//
//                boolean authCheck = user.login(user.getAPI_KEY());
//
//                if(authCheck) {
//
//                    setVisible(false);
//                    showDataFrame.setVisible(true);
//                    showDataFrame.initValueSetting();
//
//                }
//                else {
//                    authResult_lbl.setText("Invalid API KEY, Check this again");
//                }

            }
        });

    }

    void apiKey_modify() {

        try {

            ClassPathResource cpr = new ClassPathResource("apiKey");

            byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
            apiKey = new String(bdata, StandardCharsets.UTF_8);

        }
        catch (Exception e){

        }

    }


    void apiKey_load() {

        try {

            ClassPathResource cpr = new ClassPathResource("apiKey");
            byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
            apiKey = new String(bdata, StandardCharsets.UTF_8);

        }
        catch (Exception e){

        }

    }


    void actionLogin() {
        String API_KEY = apiKeyTextField.getText();
        user.setAPI_KEY(API_KEY);

        boolean authCheck = user.login(user.getAPI_KEY());

        if(authCheck) {

            setVisible(false);
            showDataFrame.setVisible(true);
            showDataFrame.initValueSetting();

        }
        else {
            authResult_lbl.setText("Invalid API KEY, Check this again");
        }
    }



    public static void main(String[] args) {

        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(ApiAuthFrame.class)
                .headless(false).run(args);

        EventQueue.invokeLater(() -> {

            ApiAuthFrame apiAuthFrame = ctx.getBean(ApiAuthFrame.class);
            apiAuthFrame.setVisible(true);

        });
    }



}