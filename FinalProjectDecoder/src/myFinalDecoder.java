
/*
 * Copyright 2016  Rohit Krishna Marneni  <rohitkrishna094@gmail.com>
 * 
 * Text Compression/Decompression using Huffman Coding
 * 
 * This class contains the GUI part of the decoder and also is the main class for Huffman decoder.
 * 
 * Decoder GUI allows the user to decompress or get back the original file that was created in the encoding
 * process. The user has to navigate to the filesystem where their "encoded.txt" file is located and select
 * that file and also the user must make sure that the supporting files(char.txt and tree.txt) necessary
 * for decoding are in the same directory. 

 * The implementation has been split into various methods to achieve modularity.
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU 
 * General Public License as published by the Free Software Foundation, either version 3 of the License, 
 * or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even 
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General 
 * Public License for more details. You should have received a copy of the GNU General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class myFinalDecoder {

    private JFrame frame;
    private JTextField textField_enterOutputFileName;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    myFinalDecoder window = new myFinalDecoder();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public myFinalDecoder() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Huffman Decoder");
        frame.getContentPane().setForeground(SystemColor.inactiveCaptionBorder);
        frame.getContentPane().setBackground(SystemColor.desktop);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 11, 444, 176, 136, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 35, 46, 52, 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        frame.getContentPane().setLayout(gridBagLayout);

        JLabel label_selectMessage = new JLabel("<html>Select a file called \"encoded.txt\" and make sure that the<br /> supporting files are in the same folder for decoding.</html>");
        label_selectMessage.setFont(new Font("Garamond", Font.BOLD, 14));
        label_selectMessage.setForeground(SystemColor.inactiveCaption);
        GridBagConstraints gbc_label_selectMessage = new GridBagConstraints();
        gbc_label_selectMessage.insets = new Insets(0, 0, 5, 5);
        gbc_label_selectMessage.anchor = GridBagConstraints.WEST;
        gbc_label_selectMessage.gridx = 1;
        gbc_label_selectMessage.gridy = 1;
        frame.getContentPane().add(label_selectMessage, gbc_label_selectMessage);

        JLabel label_displaySelectedFileName = new JLabel("");
        label_displaySelectedFileName.setForeground(SystemColor.inactiveCaption);
        label_displaySelectedFileName.setFont(new Font("Garamond", Font.BOLD, 14));
        GridBagConstraints gbc_label_displaySelectedFileName = new GridBagConstraints();
        gbc_label_displaySelectedFileName.anchor = GridBagConstraints.WEST;
        gbc_label_displaySelectedFileName.insets = new Insets(0, 0, 5, 0);
        gbc_label_displaySelectedFileName.gridx = 3;
        gbc_label_displaySelectedFileName.gridy = 1;
        frame.getContentPane().add(label_displaySelectedFileName, gbc_label_displaySelectedFileName);

        final JFileChooser fileDialog = new JFileChooser();
        JButton button_openFile = new JButton("Open File");
        button_openFile.setToolTipText("Open a file called \"encoded.txt\"");
        button_openFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fileDialog.showOpenDialog(frame) == 0) {
                    java.io.File file = fileDialog.getSelectedFile();
                    label_displaySelectedFileName.setText("File Selected");
                    System.out.println(file.getName());
                }
            }
        });
        button_openFile.setForeground(SystemColor.textHighlight);
        button_openFile.setFont(new Font("Garamond", Font.BOLD, 14));
        GridBagConstraints gbc_button_openFile = new GridBagConstraints();
        gbc_button_openFile.anchor = GridBagConstraints.WEST;
        gbc_button_openFile.insets = new Insets(0, 0, 5, 5);
        gbc_button_openFile.gridx = 2;
        gbc_button_openFile.gridy = 1;
        frame.getContentPane().add(button_openFile, gbc_button_openFile);

        JLabel lblEnterTheName = new JLabel("Enter the name of the output file along with the extension :");
        lblEnterTheName.setFont(new Font("Garamond", Font.BOLD, 14));
        lblEnterTheName.setForeground(SystemColor.inactiveCaption);
        GridBagConstraints gbc_lblEnterTheName = new GridBagConstraints();
        gbc_lblEnterTheName.anchor = GridBagConstraints.WEST;
        gbc_lblEnterTheName.insets = new Insets(0, 0, 5, 5);
        gbc_lblEnterTheName.gridx = 1;
        gbc_lblEnterTheName.gridy = 5;
        frame.getContentPane().add(lblEnterTheName, gbc_lblEnterTheName);

        textField_enterOutputFileName = new JTextField();
        textField_enterOutputFileName.setToolTipText("Enter the output name of your decoded file.\r\n");
        textField_enterOutputFileName.setText("Enter here!!!");
        textField_enterOutputFileName.setFont(new Font("Garamond", Font.BOLD, 14));
        textField_enterOutputFileName.setForeground(SystemColor.inactiveCaption);
        GridBagConstraints gbc_textField_enterOutputFileName = new GridBagConstraints();
        gbc_textField_enterOutputFileName.insets = new Insets(0, 0, 5, 5);
        gbc_textField_enterOutputFileName.anchor = GridBagConstraints.WEST;
        gbc_textField_enterOutputFileName.gridx = 2;
        gbc_textField_enterOutputFileName.gridy = 5;
        frame.getContentPane().add(textField_enterOutputFileName, gbc_textField_enterOutputFileName);
        textField_enterOutputFileName.setColumns(10);

        JLabel lblTest = new JLabel("");
        lblTest.setFont(new Font("Garamond", Font.BOLD, 14));
        lblTest.setForeground(SystemColor.inactiveCaption);
        GridBagConstraints gbc_lblTest = new GridBagConstraints();
        gbc_lblTest.anchor = GridBagConstraints.WEST;
        gbc_lblTest.insets = new Insets(0, 0, 0, 5);
        gbc_lblTest.gridx = 1;
        gbc_lblTest.gridy = 9;
        frame.getContentPane().add(lblTest, gbc_lblTest);
        frame.setBounds(100, 100, 824, 409);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton btnDecode = new JButton("Decode");
        btnDecode.setToolTipText("Click to decode.");
        btnDecode.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String outputName = textField_enterOutputFileName.getText();
                HuffmanDecoder huff = new HuffmanDecoder();
                try {
                    String outputLocation = fileDialog.getSelectedFile().getParent() + "\\" + textField_enterOutputFileName.getText();
                    huff.decode(outputLocation, fileDialog.getSelectedFile().getParent());
                } catch (ClassNotFoundException | IOException e1) {
                    e1.printStackTrace();
                }

                File outputFile = new File(fileDialog.getSelectedFile().getParent() + "\\" + outputName);
                long a = outputFile.length();
                lblTest.setText("Decode successful with file restored to it's original size of " + a + " bytes.");
            }
        });
        btnDecode.setFont(new Font("Garamond", Font.BOLD, 14));
        btnDecode.setForeground(SystemColor.textHighlight);
        GridBagConstraints gbc_btnDecode = new GridBagConstraints();
        gbc_btnDecode.anchor = GridBagConstraints.WEST;
        gbc_btnDecode.insets = new Insets(0, 0, 5, 5);
        gbc_btnDecode.gridx = 1;
        gbc_btnDecode.gridy = 7;
        frame.getContentPane().add(btnDecode, gbc_btnDecode);
    }

}
