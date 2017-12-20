
/*
 * Copyright 2016  Rohit Krishna Marneni  <rohitkrishna094@gmail.com>
 * 
 * Text Compression/Decompression using Huffman Coding
 * 
 * This is the encoder part of the encoder-decoder model that has been implemented.
 * 
 * Encoder GUI allows the user to open a file and then the "Encode" button encodes the chosen file
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
 * This program was tested on a .rar file and it gave back a -1%, but that is beyond the scope of this 
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
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanEncoder {

    // We are creating a Node blueprint which will be later used in a tree.
    private class Node implements Comparable<Node> {
        char ch;
        int freq;
        Node left;
        Node right;

        // Constructor for Node
        Node(char c, int f, Node l, Node r) {
            this.ch = c;
            this.freq = f;
            this.left = l;
            this.right = r;
        }// end constructor

        // compareTo method implementation as part of Comparable class
        public int compareTo(Node next) {
            if (this.freq > next.freq)
                return 1;
            else if (this.freq == next.freq)
                return 0;
            else
                return -1;
        }// end compareTo method
    }// end class Node

    // This will store the character frequencies to be later displayed in JTextArea
    // in GUI
    public String charFreq;
    public String finalMap; // This will store the codeword frequencies to be later displayed in JTextArea

    /*
     * The encode method takes in a string s: which is the absolute location of the
     * file selected to encode and the second parameter String encodeLocation: which
     * is the parent directory in which the selected file is located. This parent
     * directory is where we will output our final "encoded.txt" (the encoded file)
     * and the supporting files "char.txt" and "tree.txt".
     */
    @SuppressWarnings("resource")
    public void encode(String s, String encodeLocation) throws IOException, FileNotFoundException {
        RandomAccessFile fr_in = new RandomAccessFile(s, "r");
        if (fr_in.length() == 0) {
            throw new IllegalArgumentException("The string should atleast have 1 character.");
        }
        HashMap<Character, Integer> charFrequencyMap = getCharFrequencies(fr_in);
        Node root = buildTree(charFrequencyMap);
        HashMap<Character, String> charCodeWordMap = getCodeWords(root);
        String encodedMessage = encodeMessage(charCodeWordMap, fr_in);
        exportTree(root, encodeLocation);
        exportEncodedMessage(encodedMessage, encodeLocation);
        fr_in.close();
        charFreq = printCharMap(charFrequencyMap);
        finalMap = printMap(charCodeWordMap);
    }// end encode method

    /*
     * This method gives the character frequencies. It reads from the file and makes
     * a map about how many times a particular character has appeared and returns
     * the map.
     */
    private HashMap<Character, Integer> getCharFrequencies(RandomAccessFile fr_in) throws IOException, FileNotFoundException {
        HashMap<Character, Integer> charFreqMap = new HashMap<Character, Integer>();
        int ch;
        while ((ch = fr_in.read()) != -1) {
            char c = (char) ch;
            charFreqMap.put(c, charFreqMap.containsKey(c) ? charFreqMap.get(c) + 1 : 1);
        } // end while
        fr_in.seek(0);
        return charFreqMap;
    }// end getCharFrequencies method

    /*
     * This is where we build the tree using a priority queue.
     */
    private Node buildTree(HashMap<Character, Integer> charFrequencies) {
        PriorityQueue<Node> myQueue = new PriorityQueue<Node>();
        for (Map.Entry<Character, Integer> kv : charFrequencies.entrySet()) {
            myQueue.add(new Node(kv.getKey(), kv.getValue(), null, null));
        } // end for
        while (myQueue.size() > 1) {
            Node n1 = myQueue.remove();
            Node n2 = myQueue.remove();
            Node newNode = new Node('\0', n1.freq + n2.freq, n1, n2);
            myQueue.add(newNode);
        } // end while
        return myQueue.remove();
    }// end buildTree method

    /*
     * This method gives us the codewordmap between character and codewords.
     */
    private HashMap<Character, String> getCodeWords(Node n) {
        HashMap<Character, String> codeMap = new HashMap<Character, String>();
        createCodeWords(n, codeMap, "");
        return codeMap;
    }// end getCodeWords method

    /*
     * This is where we actually create our codewords(Strings of 0's and 1's to
     * encode each byte character in our original text).
     */
    private void createCodeWords(Node node, HashMap<Character, String> map, String s) {
        if (node.left == null && node.right == null) {
            map.put(node.ch, s);
            return;
        }
        createCodeWords(node.left, map, s + '0');
        createCodeWords(node.right, map, s + '1');
    }// end createCodeWords method

    /*
     * This is where we encode our message. This is the part of the encode method
     * above.
     */
    private String encodeMessage(HashMap<Character, String> map, RandomAccessFile fr) throws IOException {
        String s = "";
        int ch;
        while ((ch = fr.read()) != -1) {
            s = s + map.get((char) ch);
        } // end while
        fr.seek(0);
        return s;
    }// end encodeMessage method

    /*
     * The method exportTree takes in a node: which is basically a root node of the
     * tree that we have created. We use a pre-order traversal of the tree to note
     * down the values of it's left and right branches i.e., 0's and 1's.
     */
    private void exportTree(Node node, String encodeLocation) throws IOException, FileNotFoundException {
        BitSet bitset = new BitSet();
        ObjectOutputStream oosTree = new ObjectOutputStream(new FileOutputStream(encodeLocation + "\\tree.txt"));
        ObjectOutputStream oosChar = new ObjectOutputStream(new FileOutputStream(encodeLocation + "\\char.txt"));
        Iterator o = new Iterator();
        preOrder(node, oosChar, bitset, o);
        oosChar.close();
        bitset.set(o.bitPosition, true);
        oosTree.writeObject(bitset);
        oosTree.close();
    }// end exportTree method

    /*
     * This method gets the pre-order traversal of the tree and sets the next bit to
     * 0 in order to make it easier for us to differentiate when we try to decode it
     * later. This makes it easier for us to rebuild the tree later on in another
     * instance of the program with just the pre-order.
     */
    private void preOrder(Node node, ObjectOutputStream oosChar, BitSet bitset, Iterator o) throws IOException {
        if (node.left == null && node.right == null) {
            bitset.set(o.bitPosition++, false);
            oosChar.writeChar(node.ch);
            return;
        }
        bitset.set(o.bitPosition++, true);
        preOrder(node.left, oosChar, bitset, o);

        bitset.set(o.bitPosition++, true);
        preOrder(node.right, oosChar, bitset, o);
    }// end preOrder method

    /*
     * We use this class as a means of global variable. Since java has no means of
     * global variables, we simply create this class as static thereby, we can use
     * it's bitPosition attribute whenever we want. We exploit the advantage of
     * bitPosition being initialized to 0 whenever we create a new instance of this
     * object, hence we don't have to set it to 0 or create a constructor for this
     * class.
     */
    private static class Iterator {
        int bitPosition;
    }// end Iterator class

    /*
     * This is where we export the encoded file. It is named as "encoded.txt" and is
     * stored in the same folder/directory from where the input file came. As can be
     * seen, the ".txt" extension is only a design decision not a necessity. We
     * could name the file as "encoded" itself, but naming it as "encoded.txt" would
     * give us a better ability to directly open it with default text editor on any
     * OS to verify the contents once the encoding process is done.
     */
    private void exportEncodedMessage(String s, String encodeLocation) throws FileNotFoundException, IOException {
        FileOutputStream fr_out = new FileOutputStream(encodeLocation + "\\encoded.txt");
        BitSet bitSet = new BitSet();
        int i;
        for (i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                bitSet.set(i, false);
            } else {
                bitSet.set(i, true);
            }
        } // end for
        bitSet.set(i, true);
        byte[] bs = bitSet.toByteArray();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(bs);
        baos.writeTo(fr_out);
        fr_out.close();
    }// end exportEncodedMessage method

    /*
     * This prints or rather returns the character frequency map. We use it to
     * display the character- frequency in JTextArea of our main class.
     */
    private String printCharMap(HashMap<Character, Integer> map) {
        StringBuilder s = new StringBuilder();
        s.append(" Character\t\tFrequency");
        s.append("\n-----------------------------------------------------------\n");
        for (Map.Entry<Character, Integer> kv : map.entrySet()) {
            System.out.print(kv.getValue() + ",");
            s.append(" " + kv.getKey() + "\t:\t" + kv.getValue() + "\n");
        }
        return s.toString();
    }// end printCharMap method

    /*
     * This prints or rather returns the codeword frequency map. We use it to
     * display the codeword- frequency in JTextArea of our main class.
     */
    private String printMap(HashMap<Character, String> map) {
        StringBuilder s = new StringBuilder();
        s.append(" Character\t\tEncoded Word");
        s.append("\n-----------------------------------------------------------------\n");
        for (Map.Entry<Character, String> kv : map.entrySet()) {
            s.append(" " + kv.getKey() + "\t:\t" + kv.getValue() + "\n");
        } // end for
        return s.toString();
    }// end printMap method

}// end class HuffmanEncoder
