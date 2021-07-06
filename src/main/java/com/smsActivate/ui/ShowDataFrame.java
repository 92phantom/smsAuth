package com.smsActivate.ui;

import com.smsActivate.ui.model.Country;
import com.smsActivate.ui.model.ServiceCode;
import com.smsActivate.ui.sms.SmsImpl;
import com.smsActivate.ui.user.UserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;

@Component
public class ShowDataFrame extends JFrame {

    @Autowired
    UserImpl user;
    @Autowired
    SmsImpl sms;

    private ArrayList<Country> countryArrayList;
    private JTable table;
    private JScrollPane scroll_table;
    private DefaultTableModel model_table;
    private JTextField phoneNumberTextArea;
    private JLabel balanceValue_lbl;
    private JLabel apiKeyValue_lbl;
    private JButton getSMS_Btn;
    private JTextArea smsTextArea;
    private JButton getNumber_btn;
    private JButton getCancel_Btn;
    private JLabel timer_lbl;
    private int left_min;
    private int left_sec;
    private Timer timer;
    private JLabel error_lbl;
    private Future<String> futureResult;
    
    /**
     * Create the application.
     */
    public ShowDataFrame() {
        initialize();
    }

    public void initValueSetting() {
        apiKeyValue_lbl.setText(user.getAPI_KEY());
        balanceValue_lbl.setText((user.getUser_balance()) + "");

        sms.loadData();
    }


    /**
     * Initialize the contents of the
     */
    private void initialize() {
        setTitle("SMS 문의 카카오톡 wlsdud787");
        setBounds(100, 100, 574, 379);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(6, 35, 119, 278);
        getContentPane().add(scrollPane);

        scroll_table = new JScrollPane();            // add table to scroll panel
        scroll_table.setBounds(137, 35, 175, 278);
        scroll_table.setVisible(true);
        getContentPane().add(scroll_table);

        DefaultListModel serviceListModel = new DefaultListModel();
        serviceListModel.addElement("GOOGLE");
        serviceListModel.addElement("TELEGRAM");
        serviceListModel.addElement("KAKAOTALK");
        serviceListModel.addElement("ANYOTHER");

        JList serviceList = new JList(serviceListModel);
        scrollPane.setViewportView(serviceList);

        getNumber_btn = new JButton("Get Number");
        getNumber_btn.setBounds(324, 35, 117, 29);
        getContentPane().add(getNumber_btn);

        phoneNumberTextArea = new JTextField();
        phoneNumberTextArea.setEditable(false);
        phoneNumberTextArea.setBounds(324, 93, 130, 26);
        getContentPane().add(phoneNumberTextArea);
        phoneNumberTextArea.setColumns(10);

        JLabel phonenumber_lbl = new JLabel("Phone number");
        phonenumber_lbl.setBounds(324, 76, 105, 16);
        getContentPane().add(phonenumber_lbl);

        getSMS_Btn = new JButton("Get SMS");
        getSMS_Btn.setBounds(462, 70, 84, 29);
        getSMS_Btn.setEnabled(false);
        getContentPane().add(getSMS_Btn);

        JLabel receivedSms_lbl = new JLabel("Received SMS");
        receivedSms_lbl.setBounds(324, 143, 105, 16);
        getContentPane().add(receivedSms_lbl);

        smsTextArea = new JTextArea();
        smsTextArea.setEditable(false);
        smsTextArea.setBounds(324, 162, 222, 151);
        smsTextArea.setLineWrap(true);
        getContentPane().add(smsTextArea);

        apiKeyValue_lbl = new JLabel("");
        apiKeyValue_lbl.setBounds(79, 7, 225, 16);
        getContentPane().add(apiKeyValue_lbl);

        JLabel apiKey_lbl = new JLabel("API KEY: ");
        apiKey_lbl.setBounds(6, 7, 61, 16);
        getContentPane().add(apiKey_lbl);

        JLabel balance_lbl = new JLabel("Balance: ");
        balance_lbl.setBounds(324, 7, 61, 16);
        getContentPane().add(balance_lbl);

        balanceValue_lbl = new JLabel("");
        balanceValue_lbl.setBounds(397, 7, 142, 16);
        getContentPane().add(balanceValue_lbl);
        
        getCancel_Btn = new JButton("Cancel");
        getCancel_Btn.setEnabled(false);
        getCancel_Btn.setBounds(462, 109, 84, 29);
        getContentPane().add(getCancel_Btn);
        
        timer_lbl = new JLabel("");
        timer_lbl.setForeground(Color.RED);
        timer_lbl.setBounds(472, 143, 74, 16);
        getContentPane().add(timer_lbl);
        
        error_lbl = new JLabel("");
        error_lbl.setForeground(Color.RED);
        error_lbl.setBounds(324, 125, 130, 15);
        getContentPane().add(error_lbl);


        // GetNumber Button
        getNumber_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ServiceCode serviceCode = ServiceCode.valueOf((String) serviceList.getSelectedValue());
                String countryName = table.getValueAt(table.getSelectedRow(), 0).toString().toUpperCase();

                Country curCountry = countryArrayList.get(table.getSelectedRow());

                long phoneNumber = sms.getPhoneNumber(
                        curCountry.getCountry_code()
                        ,serviceCode.getServiceCode());

                phoneNumberTextArea.setText(phoneNumber+"");

            }
        });


        // Value Setting
        serviceList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                // TODO Auto-generated method stub
            	if(timer != null && timer.isRunning()) {
            		timer.stop();	
            	}
            	timer_lbl.setText("");
            	getSMS_Btn.setEnabled(false);
                getCancel_Btn.setEnabled(false);
                phoneNumberTextArea.setText("");
                getSMS_Btn.setText("Get SMS");
            	error_lbl.setText("");

                ServiceCode serviceCode = ServiceCode.valueOf((String) serviceList.getSelectedValue());
                countryArrayList = sms.getCountryServiceInfo((String) serviceList.getSelectedValue());

                table = new JTable();
                model_table = new DefaultTableModel() {

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        //all cells false
                        return false;
                    }

                };
                model_table.addColumn("Country");
                model_table.addColumn("Phones");
                model_table.addColumn("Price");
                table.setModel(model_table);


                for (Country country : countryArrayList) {

                    Vector<String> r = new Vector<String>();
                    r.addElement(country.getCountry_name());
                    r.addElement(country.getAvailable_count() + "");
                    r.addElement(country.getPrice() + "");
                    model_table.addRow(r);

                }

                scroll_table.setVisible(false);
                scroll_table = new JScrollPane(table);            // add table to scroll panel
                scroll_table.setBounds(137, 35, 175, 278);
                scroll_table.setVisible(true);
                getContentPane().add(scroll_table);


                table.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        // TODO Auto-generated method stub
                    	if(timer != null && timer.isRunning()) {
                    		timer.stop();	
                    	}
                    	timer_lbl.setText("");
                        getSMS_Btn.setText("Get SMS");
                        getSMS_Btn.setEnabled(false);
                        getCancel_Btn.setEnabled(false);
                        phoneNumberTextArea.setText("");
                    	error_lbl.setText("");

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        // TODO Auto-generated method stub

                    }

                });

            }
        });


        getSMS_Btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            	
            	left_min = 20;
            	left_sec = 0;
            	
            	timer = new Timer(1000, new ActionListener()
            	{   
	            	@Override
	            	public void actionPerformed(ActionEvent e) {
	            		// TODO Auto-generated method stub

	            		if(left_min <= 0) {
	            			timer_lbl.setText("expired");
	            			return;
	            		}
	            		
	            		timer_lbl.setText(left_min + " : " + left_sec);
	            		
	            		if(left_sec <= 0) {
	            			left_min -= 1;
	            			left_sec = 59;
	            		} else {
	            			left_sec -= 1;
	            		}
	            		
	            	}
            	});
            	
            	timer.start();
            	
            	
                getCancel_Btn.setEnabled(true);
                
                getSMS_Btn.setText("Waiting..");
            	getSMS_Btn.setEnabled(false);
            	
            	sms.getSmsMessage().addCallback(
                		(result) -> {
                            smsTextArea.setText(sms.getSmsActivateGetFullSmsResponse().getText());
                            getSMS_Btn.setText("Get SMS");
                        	getSMS_Btn.setEnabled(true);
                                                  
                		}, (ee) -> {
                        	error_lbl.setText("Try Get Number Again");
//                            getSMS_Btn.setText("Get SMS");
//                        	getSMS_Btn.setEnabled(true);
                		}
                );
                
//            	futureResult = sms.getSmsMessage();
//            	
//            	if(futureResult.isDone()) {
//            		try {
////                		smsTextArea.setText(futureResult.get().toString());
////						System.out.println(futureResult.get().toString());
//						String aa = futureResult.get().toString();
//						smsTextArea.setText(aa);
//						System.out.println(aa);
//						
//					} catch (InterruptedException e1) {
//						// TODO Auto-generated catch block
//						error_lbl.setText("Try Get Number Again");
//						e1.printStackTrace();
//					} catch (ExecutionException e1) {
//						// TODO Auto-generated catch block
//						error_lbl.setText("Try Get Number Again");
//						e1.printStackTrace();
//					}
//            	}

                user.reloadBlanace();
                balanceValue_lbl.setText((user.getUser_balance()) + "");                
//                
            }

        });

        

        
        getCancel_Btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
//				futureResult.cancel(true);
//				System.out.println("cancel = " + futureResult.isCancelled());
//				Thread.currentThread().isInterrupted();
				sms.cancelSMS();
            	if(timer != null && timer.isRunning()) {
            		timer.stop();	
            	}
            	timer_lbl.setText("");
            	getSMS_Btn.setText("Get SMS");
            	error_lbl.setText("Try Get Number Again");
            	
//            	getSMS_Btn.setEnabled(true);
				
			}
		});
        
        getNumber_btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
            	if(timer != null && timer.isRunning()) {
            		timer.stop();	
            	}
            	timer_lbl.setText("");
                getSMS_Btn.setText("Get SMS");
                getCancel_Btn.setEnabled(false);
                getSMS_Btn.setEnabled(true);
                phoneNumberTextArea.setText("");
                smsTextArea.setText("");
            	error_lbl.setText("");

            }

        });

    }
}
