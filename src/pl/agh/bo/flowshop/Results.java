package pl.agh.bo.flowshop;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Results extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextArea defaultCombinationResults;
    private JTextArea outCombinationResults;

    public Results(String defaultCombinationResult, String outCombinationResult) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        defaultCombinationResults.setText(defaultCombinationResult);
        outCombinationResults.setText(outCombinationResult);
        pack();
        setVisible(true);
        System.exit(0);
    }

    private void onOK() {
// add your code here
        dispose();
    }

}
