
/*
 * Copyright 2016  Rohit Krishna Marneni  <rohitkrishna094@gmail.com>
 * 
 * Text Compression/Decompression using Huffman Coding
 * 
 * This class contains the GUI part of the encoder and also is the main class for Huffman encoder.
 * 
 * This is the encoder part of the encoder-decoder model that has been implemented.
 * 
 * Encoder GUI takes allows the user to open a file and then the "Encode" button encodes the chosen file
 * to the same directory. The encoding process creates two additional files apart from "encoded.txt" file
 * viz., char.txt and tree.txt. 
 * 
 * The decoder part on the other hand allows the user to select the "encoded.txt" file which was created
 * during the encoding process and allows the user to name the output file and upon clicking 
 * "Decode button", the decoder restores the original file in the same directory to where "encoded.txt"
 * belongs to. However during this process, it's quite important that the three files "encoded.txt",
 * "char.txt" and "tree.txt" be in the same directory. 
 * 
 * This works on any filetype be it .txt, .mp3 etc. However better compression rates are usually seen on 
 * text files as they are the perfect places for Huffman coding to work. Also there are instances where 
 * we may get negative compression percentage. This happens when the files are already highly compressed. 
 * This program was tested on a .rar file and it gave back a -1%, but that is beyond that scope of this 
 * project as it mainly deals with text compression-decompression and the other filetype based compression-
 * decompression analysis was only done as a reference to go with the final conclusion. 
 * 
 * The process however is slow and is advisably not to be tested on files above 1 MB as the program has
 * many recursive calls and it goes bit by bit. Such a program would normally be implemented in a low-level
 * program, but this is only a demonstration of Huffman Coding on text based files.
 * 
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;

public class myFinalEncoder {

    private JFrame frmHuffmanEncoder;
    private JButton button_Encode;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    myFinalEncoder window = new myFinalEncoder();
                    window.frmHuffmanEncoder.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public myFinalEncoder() {
        initialize();
    }

    private void initialize() {
        final JFileChooser fileDialog = new JFileChooser();

        frmHuffmanEncoder = new JFrame();
        frmHuffmanEncoder.setFont(new Font("Garamond", Font.BOLD, 16));
        frmHuffmanEncoder.getContentPane().setBackground(new Color(0, 0, 0));
        frmHuffmanEncoder.getContentPane().setForeground(new Color(255, 255, 255));
        frmHuffmanEncoder.setResizable(false);
        frmHuffmanEncoder.setTitle("Huffman Encoder");
        frmHuffmanEncoder.setBounds(100, 100, 908, 552);
        frmHuffmanEncoder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0, 386, 0, -67, 412, 0, -107, 0, 0, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        frmHuffmanEncoder.getContentPane().setLayout(gridBagLayout);

        JSeparator topSeparator = new JSeparator();
        GridBagConstraints gbc_topSeparator = new GridBagConstraints();
        gbc_topSeparator.insets = new Insets(0, 0, 5, 5);
        gbc_topSeparator.gridx = 2;
        gbc_topSeparator.gridy = 0;
        frmHuffmanEncoder.getContentPane().add(topSeparator, gbc_topSeparator);

        JSeparator leftSeparator = new JSeparator();
        GridBagConstraints gbc_leftSeparator = new GridBagConstraints();
        gbc_leftSeparator.gridheight = 2;
        gbc_leftSeparator.insets = new Insets(0, 0, 5, 5);
        gbc_leftSeparator.gridx = 1;
        gbc_leftSeparator.gridy = 1;
        frmHuffmanEncoder.getContentPane().add(leftSeparator, gbc_leftSeparator);

        JLabel label_FileSelected_1 = new JLabel("");
        label_FileSelected_1.setFont(new Font("Garamond", Font.BOLD, 14));
        label_FileSelected_1.setBackground(new Color(175, 238, 238));
        label_FileSelected_1.setForeground(new Color(255, 255, 255));
        GridBagConstraints gbc_label_FileSelected_1 = new GridBagConstraints();
        gbc_label_FileSelected_1.gridwidth = 3;
        gbc_label_FileSelected_1.anchor = GridBagConstraints.WEST;
        gbc_label_FileSelected_1.insets = new Insets(0, 0, 5, 5);
        gbc_label_FileSelected_1.gridx = 3;
        gbc_label_FileSelected_1.gridy = 1;
        frmHuffmanEncoder.getContentPane().add(label_FileSelected_1, gbc_label_FileSelected_1);

        JSeparator rightSeparator = new JSeparator();
        GridBagConstraints gbc_rightSeparator = new GridBagConstraints();
        gbc_rightSeparator.gridheight = 2;
        gbc_rightSeparator.insets = new Insets(0, 0, 5, 5);
        gbc_rightSeparator.gridx = 8;
        gbc_rightSeparator.gridy = 1;
        frmHuffmanEncoder.getContentPane().add(rightSeparator, gbc_rightSeparator);

        button_Encode = new JButton("Encode");
        button_Encode.setFont(new Font("Garamond", Font.BOLD, 14));
        button_Encode.setForeground(new Color(100, 149, 237));
        button_Encode.setToolTipText(
                "<html>After selecting the file, click Encode to encode the file.<br />The encoded file along with supporting files will be <br />created in the same directory from where the input file <br />was selected.</html>");

        GridBagConstraints gbc_button_Encode = new GridBagConstraints();
        gbc_button_Encode.insets = new Insets(0, 0, 5, 5);
        gbc_button_Encode.gridx = 2;
        gbc_button_Encode.gridy = 2;
        frmHuffmanEncoder.getContentPane().add(button_Encode, gbc_button_Encode);

        JButton showFileDialogButton = new JButton("Open File");
        showFileDialogButton.setFont(new Font("Garamond", Font.BOLD, 14));
        showFileDialogButton.setForeground(new Color(100, 149, 237));
        showFileDialogButton.setToolTipText("Select a file from your system.");
        showFileDialogButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileDialog.showOpenDialog(frmHuffmanEncoder);
                if (returnVal == 0) {
                    java.io.File file = fileDialog.getSelectedFile();
                    if (!file.exists()) {
                        label_FileSelected_1.setText("File not found!");
                    } else {
                        label_FileSelected_1.setText("File Selected :" + file.getName());
                    }
                } else {
                    label_FileSelected_1.setText("Open command cancelled by user.");
                }
            }
        });

        GridBagConstraints gbc_showFileDialogButton = new GridBagConstraints();
        gbc_showFileDialogButton.insets = new Insets(0, 0, 5, 10);
        gbc_showFileDialogButton.gridx = 2;
        gbc_showFileDialogButton.gridy = 1;
        frmHuffmanEncoder.getContentPane().add(showFileDialogButton, gbc_showFileDialogButton);

        JLabel label_FinalAnswer = new JLabel("");
        label_FinalAnswer.setForeground(SystemColor.window);
        label_FinalAnswer.setFont(new Font("Garamond", Font.BOLD, 14));
        GridBagConstraints gbc_label_FinalAnswer = new GridBagConstraints();
        gbc_label_FinalAnswer.gridwidth = 8;
        gbc_label_FinalAnswer.insets = new Insets(0, 0, 5, 5);
        gbc_label_FinalAnswer.gridx = 2;
        gbc_label_FinalAnswer.gridy = 3;
        frmHuffmanEncoder.getContentPane().add(label_FinalAnswer, gbc_label_FinalAnswer);

        JSeparator separator = new JSeparator();
        GridBagConstraints gbc_separator = new GridBagConstraints();
        gbc_separator.gridwidth = 7;
        gbc_separator.insets = new Insets(0, 0, 5, 0);
        gbc_separator.gridx = 4;
        gbc_separator.gridy = 4;
        frmHuffmanEncoder.getContentPane().add(separator, gbc_separator);

        JSeparator lefttextArea_topSeparator = new JSeparator();
        GridBagConstraints gbc_lefttextArea_topSeparator = new GridBagConstraints();
        gbc_lefttextArea_topSeparator.insets = new Insets(0, 0, 5, 5);
        gbc_lefttextArea_topSeparator.gridx = 2;
        gbc_lefttextArea_topSeparator.gridy = 5;
        frmHuffmanEncoder.getContentPane().add(lefttextArea_topSeparator, gbc_lefttextArea_topSeparator);

        JSeparator lefttextArea_leftSeparator = new JSeparator();
        GridBagConstraints gbc_lefttextArea_leftSeparator = new GridBagConstraints();
        gbc_lefttextArea_leftSeparator.insets = new Insets(0, 0, 5, 5);
        gbc_lefttextArea_leftSeparator.gridx = 0;
        gbc_lefttextArea_leftSeparator.gridy = 6;
        frmHuffmanEncoder.getContentPane().add(lefttextArea_leftSeparator, gbc_lefttextArea_leftSeparator);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.gridx = 2;
        gbc_scrollPane.gridy = 6;
        frmHuffmanEncoder.getContentPane().add(scrollPane, gbc_scrollPane);

        JTextArea textArea_CharFreq = new JTextArea();
        textArea_CharFreq.setToolTipText("");
        textArea_CharFreq.setFont(new Font("Garamond", Font.PLAIN, 13));
        scrollPane.setViewportView(textArea_CharFreq);

        JSeparator righttextArea_leftSeparator = new JSeparator();
        GridBagConstraints gbc_righttextArea_leftSeparator = new GridBagConstraints();
        gbc_righttextArea_leftSeparator.insets = new Insets(0, 0, 5, 5);
        gbc_righttextArea_leftSeparator.gridx = 4;
        gbc_righttextArea_leftSeparator.gridy = 6;
        frmHuffmanEncoder.getContentPane().add(righttextArea_leftSeparator, gbc_righttextArea_leftSeparator);

        JScrollPane scrollPane_1 = new JScrollPane();
        GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
        gbc_scrollPane_1.gridwidth = 2;
        gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
        gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane_1.gridx = 5;
        gbc_scrollPane_1.gridy = 6;
        frmHuffmanEncoder.getContentPane().add(scrollPane_1, gbc_scrollPane_1);

        JTextArea textArea_encodeMap = new JTextArea();
        textArea_encodeMap.setToolTipText("\r\n");
        textArea_encodeMap.setFont(new Font("Garamond", Font.PLAIN, 13));
        scrollPane_1.setViewportView(textArea_encodeMap);

        JSeparator lefttextArea_rightSeparator = new JSeparator();
        GridBagConstraints gbc_lefttextArea_rightSeparator = new GridBagConstraints();
        gbc_lefttextArea_rightSeparator.anchor = GridBagConstraints.WEST;
        gbc_lefttextArea_rightSeparator.insets = new Insets(0, 0, 5, 5);
        gbc_lefttextArea_rightSeparator.gridx = 7;
        gbc_lefttextArea_rightSeparator.gridy = 6;
        frmHuffmanEncoder.getContentPane().add(lefttextArea_rightSeparator, gbc_lefttextArea_rightSeparator);

        JSeparator righttextArea_rightSeparator = new JSeparator();
        GridBagConstraints gbc_righttextArea_rightSeparator = new GridBagConstraints();
        gbc_righttextArea_rightSeparator.insets = new Insets(0, 0, 5, 0);
        gbc_righttextArea_rightSeparator.gridx = 10;
        gbc_righttextArea_rightSeparator.gridy = 6;
        frmHuffmanEncoder.getContentPane().add(righttextArea_rightSeparator, gbc_righttextArea_rightSeparator);

        JSeparator righttextArea_bottomSeparator = new JSeparator();
        GridBagConstraints gbc_righttextArea_bottomSeparator = new GridBagConstraints();
        gbc_righttextArea_bottomSeparator.gridwidth = 6;
        gbc_righttextArea_bottomSeparator.insets = new Insets(0, 0, 5, 0);
        gbc_righttextArea_bottomSeparator.gridx = 5;
        gbc_righttextArea_bottomSeparator.gridy = 7;
        frmHuffmanEncoder.getContentPane().add(righttextArea_bottomSeparator, gbc_righttextArea_bottomSeparator);

        JLabel label_CharFreqMap = new JLabel("Character Frequency Map");
        label_CharFreqMap.setBackground(SystemColor.inactiveCaption);
        label_CharFreqMap.setForeground(SystemColor.activeCaption);
        label_CharFreqMap.setFont(new Font("Garamond", Font.BOLD, 14));
        label_CharFreqMap.setHorizontalAlignment(SwingConstants.LEFT);
        label_CharFreqMap.setVerticalAlignment(SwingConstants.TOP);
        GridBagConstraints gbc_label_CharFreqMap = new GridBagConstraints();
        gbc_label_CharFreqMap.fill = GridBagConstraints.VERTICAL;
        gbc_label_CharFreqMap.insets = new Insets(0, 0, 5, 5);
        gbc_label_CharFreqMap.gridx = 2;
        gbc_label_CharFreqMap.gridy = 8;
        frmHuffmanEncoder.getContentPane().add(label_CharFreqMap, gbc_label_CharFreqMap);

        JLabel lblCharacterToCodeword = new JLabel("Character to CodeWord Map");
        lblCharacterToCodeword.setForeground(SystemColor.activeCaption);
        lblCharacterToCodeword.setFont(new Font("Garamond", Font.BOLD, 14));
        lblCharacterToCodeword.setVerticalAlignment(SwingConstants.TOP);
        lblCharacterToCodeword.setHorizontalAlignment(SwingConstants.LEFT);
        GridBagConstraints gbc_lblCharacterToCodeword = new GridBagConstraints();
        gbc_lblCharacterToCodeword.insets = new Insets(0, 0, 5, 5);
        gbc_lblCharacterToCodeword.gridx = 5;
        gbc_lblCharacterToCodeword.gridy = 8;
        frmHuffmanEncoder.getContentPane().add(lblCharacterToCodeword, gbc_lblCharacterToCodeword);

        JSeparator lefttextArea_bottomSeparator = new JSeparator();
        GridBagConstraints gbc_lefttextArea_bottomSeparator = new GridBagConstraints();
        gbc_lefttextArea_bottomSeparator.insets = new Insets(0, 0, 0, 5);
        gbc_lefttextArea_bottomSeparator.gridx = 2;
        gbc_lefttextArea_bottomSeparator.gridy = 9;
        frmHuffmanEncoder.getContentPane().add(lefttextArea_bottomSeparator, gbc_lefttextArea_bottomSeparator);

        button_Encode.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textArea_CharFreq.setText(null);
                textArea_encodeMap.setText(null);
                if (label_FileSelected_1.getText() == "Open command cancelled by user.") {

                } else if (!fileDialog.getSelectedFile().exists()) {

                } else {
                    // Create the instance of HuffmanEncoder. This is where our compression begins.
                    HuffmanEncoder huff = new HuffmanEncoder();
                    try {
                        huff.encode(fileDialog.getSelectedFile().getAbsolutePath(), fileDialog.getSelectedFile().getParent());
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    double a = (double) fileDialog.getSelectedFile().length();
                    File encodedFile = new File(fileDialog.getSelectedFile().getParent() + "\\encoded.txt");
                    double b = (double) encodedFile.length();
                    double c = ((a - b) / a) * 100;
                    label_FinalAnswer.setText("Original File : " + a + " bytes" + "   ||   Encoded File : " + b + " bytes"
                            + "   ||   Compression Percentage : " + c);
                    textArea_CharFreq.append(huff.charFreq);
                    textArea_encodeMap.append(huff.finalMap);
                }
            }
        });
        frmHuffmanEncoder.setVisible(true);
    }
}
