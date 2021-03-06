package views;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Alex Durso <ad901965@wcupa.edu>
 */
public class MyFrame extends javax.swing.JFrame {
	
    //Organizing visual components and making them available to controller
	public JButton changePword(){
		return changeButton;
	}
	
	public JButton dumpButton(){
		return dumpButton;
	}
	
	public JTextField getLoginField() {
		return loginField;
	}
	
	public JTextField getcPwordField() {
		return cPwordField;
	}
	
	public JTextField getnPwordField(){
		return nPwordField;
	}
	
	public JTextArea getMessagesField(){
		return Messages;
	}
	/**
	 * Creates new form MyFrame
	 */
	public MyFrame() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        login = new javax.swing.JLabel();
        cPword = new javax.swing.JLabel();
        nPword = new javax.swing.JLabel();
        msgs = new javax.swing.JLabel();
        loginField = new javax.swing.JTextField();
        cPwordField = new javax.swing.JTextField();
        nPwordField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        Messages = new javax.swing.JTextArea();
        changeButton = new javax.swing.JButton();
        dumpButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        login.setText("Login:");

        cPword.setText("Current password:");

        nPword.setText("New password:");

        msgs.setText("Messages:");

        nPwordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nPwordFieldActionPerformed(evt);
            }
        });

        Messages.setEditable(false);
        Messages.setColumns(20);
        Messages.setRows(5);
        jScrollPane1.setViewportView(Messages);

        changeButton.setText("Change Password");
        changeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeButtonActionPerformed(evt);
            }
        });

        dumpButton.setText("Dump Database");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(changeButton)
                    .addComponent(msgs)
                    .addComponent(nPword)
                    .addComponent(cPword)
                    .addComponent(login))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                    .addComponent(loginField)
                    .addComponent(cPwordField)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dumpButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(nPwordField))
                .addGap(217, 217, 217))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(login)
                    .addComponent(loginField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cPword)
                    .addComponent(cPwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nPword)
                    .addComponent(nPwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changeButton)
                    .addComponent(dumpButton))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(msgs)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(68, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void changeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_changeButtonActionPerformed

    private void nPwordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nPwordFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nPwordFieldActionPerformed

	/**
	 * @param args the command line arguments
	 */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea Messages;
    private javax.swing.JLabel cPword;
    private javax.swing.JTextField cPwordField;
    private javax.swing.JButton changeButton;
    private javax.swing.JButton dumpButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel login;
    private javax.swing.JTextField loginField;
    private javax.swing.JLabel msgs;
    private javax.swing.JLabel nPword;
    private javax.swing.JTextField nPwordField;
    // End of variables declaration//GEN-END:variables
}
