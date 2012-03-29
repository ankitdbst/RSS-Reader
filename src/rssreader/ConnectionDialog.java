/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ConnectionDialog.java
 *
 * Created on Apr 4, 2011, 6:45:25 PM
 */

package rssreader;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 *
 * @author victor
 */
public class ConnectionDialog extends javax.swing.JDialog implements ItemListener{
    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    /** Creates new form ConnectionDialog */
    public ConnectionDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setTitle("Proxy Authentication");
        authenticationCB.setSelected(true);
        authenticationCB.addItemListener(this);
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    public String getProxy(){
        return proxy;
    }

    public String getPort(){
        return port;
    }

    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        proxyAddressLabel = new javax.swing.JLabel();
        proxyAddressTextfiled = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        proxyPortTextfield = new javax.swing.JTextField();
        authenticationCB = new javax.swing.JCheckBox();
        usernameLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        usernameTextfield = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(rssreader.RSSReaderApp.class).getContext().getResourceMap(ConnectionDialog.class);
        okButton.setText(resourceMap.getString("okButton.text")); // NOI18N
        okButton.setName("okButton"); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        proxyAddressLabel.setText(resourceMap.getString("proxyAddressLabel.text")); // NOI18N
        proxyAddressLabel.setName("proxyAddressLabel"); // NOI18N

        proxyAddressTextfiled.setText(resourceMap.getString("proxyAddressTextfiled.text")); // NOI18N
        proxyAddressTextfiled.setName("proxyAddressTextfiled"); // NOI18N
        proxyAddressTextfiled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proxyAddressTextfiledActionPerformed(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        proxyPortTextfield.setText(resourceMap.getString("proxyPortTextfield.text")); // NOI18N
        proxyPortTextfield.setName("proxyPortTextfield"); // NOI18N

        authenticationCB.setText(resourceMap.getString("authenticationCB.text")); // NOI18N
        authenticationCB.setName("authenticationCB"); // NOI18N

        usernameLabel.setText(resourceMap.getString("usernameLabel.text")); // NOI18N
        usernameLabel.setName("usernameLabel"); // NOI18N

        passwordLabel.setText(resourceMap.getString("passwordLabel.text")); // NOI18N
        passwordLabel.setName("passwordLabel"); // NOI18N

        usernameTextfield.setText(resourceMap.getString("usernameTextfield.text")); // NOI18N
        usernameTextfield.setName("usernameTextfield"); // NOI18N

        passwordField.setText(resourceMap.getString("passwordField.text")); // NOI18N
        passwordField.setName("passwordField"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(250, Short.MAX_VALUE)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(authenticationCB)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(proxyAddressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(proxyAddressTextfiled, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(proxyPortTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(176, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(passwordLabel)
                        .addGap(18, 18, 18)
                        .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(usernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(usernameTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(140, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(proxyAddressLabel)
                    .addComponent(proxyAddressTextfiled, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(proxyPortTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(authenticationCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(usernameLabel)
                    .addComponent(usernameTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void proxyAddressTextfiledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proxyAddressTextfiledActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_proxyAddressTextfiledActionPerformed

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        if(returnStatus == 1){
             proxy  = proxyAddressTextfiled.getText();
             port = proxyPortTextfield.getText();
             username = usernameTextfield.getText();
             password = passwordField.getPassword().toString();
        }
        setVisible(false);
        dispose();
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ConnectionDialog dialog = new ConnectionDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox authenticationCB;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton okButton;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JLabel proxyAddressLabel;
    private javax.swing.JTextField proxyAddressTextfiled;
    private javax.swing.JTextField proxyPortTextfield;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JTextField usernameTextfield;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;
    private String proxy = "",
            port = "",
            username = "",
            password = "";

    public void itemStateChanged(ItemEvent e) {
        if(!authenticationCB.isSelected()){
            usernameTextfield.setEnabled(false);
            passwordField.setEnabled(false);
        }
        else {
            usernameTextfield.setEnabled(true);
            passwordField.setEnabled(true);
        }
    }
}