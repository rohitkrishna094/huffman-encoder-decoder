
/*
 * Copyright 2016  Rohit Krishna Marneni  <rohitkrishna094@gmail.com>
 * 
 * Text Compression/Decompression using Huffman Coding
 * 
 * This is the decoder part of the encoder-decoder model that has been implemented.
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BitSet;

public class HuffmanDecoder {
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

    /*
     * The decode method takes a string outputString which is the absolute path of
     * the output that the user gives. The second parameter is where the
     * "encoded.txt" is located. This method will take these two parameters and
     * retrieve the original file and place it in the same folder where
     * "encoded.txt" is present.
     */
    public void decode(String outputString, String encodedFileLocation) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileOutputStream fout_decode = new FileOutputStream(outputString);
        String trueencodeLocation = encodedFileLocation.concat("\\encoded.txt");
        Node root = unpackTree(encodedFileLocation);
        Path path = Paths.get(trueencodeLocation);
        byte[] encodedBytes = Files.readAllBytes(path);
        BitSet bitset = BitSet.valueOf(encodedBytes);
        for (int i = 0; i < bitset.length() - 1;) {
            Node tmp = root;
            while (tmp.left != null) {
                if (!bitset.get(i)) {
                    tmp = tmp.left;
                } else {
                    tmp = tmp.right;
                }
                i++;
            } // end while
            fout_decode.write(tmp.ch);
        } // end for
        fout_decode.close();
    }// end decode method

    /*
     * This method unpacks the tree from "tree.txt". We need this method because we
     * stored the Huffman tree in "tree.txt" in the form of BitSet and serialized
     * it.
     */
    private Node unpackTree(String parentDir) throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream oisBranch = new ObjectInputStream(new FileInputStream(parentDir + "\\tree.txt"));
        ObjectInputStream oisChar = new ObjectInputStream(new FileInputStream(parentDir + "\\char.txt"));
        BitSet bitset = (BitSet) oisBranch.readObject();
        oisBranch.close();
        return rebuildTree(bitset, oisChar, new Iterator());
    }// end unpackTree method

    /*
     * After unpacking, we rebuild the Huffman tree in order to start the decoding
     * process.
     */
    private Node rebuildTree(BitSet bitset, ObjectInputStream oisChar, Iterator o) throws IOException {
        Node node = new Node('\0', 0, null, null);
        if (!bitset.get(o.bitPosition)) {
            o.bitPosition++;
            node.ch = oisChar.readChar();
            return node;
        }
        o.bitPosition = o.bitPosition + 1;
        node.left = rebuildTree(bitset, oisChar, o);

        o.bitPosition = o.bitPosition + 1;
        node.right = rebuildTree(bitset, oisChar, o);
        return node;
    }// end rebuildTree method

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

}// end class HuffmanDecoder
