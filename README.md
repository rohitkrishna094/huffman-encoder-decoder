# huffman-encoder-decoder
A small implementation of Huffman encoding and decoding in Java

### Encoder

The below screenshot shows the GUI for the Encoder.

![encoder](https://user-images.githubusercontent.com/18495886/34317738-cf571f14-e77a-11e7-9cf1-13eeade4c27f.png)

1. Select the file you want to compress by clicking "Open File".
2. Once the file is loaded, you will see a lable mentioning the name of the file on the right
3. Now click "Encode" button which will encode the file in the directory that file belongs to.
4. Once encoded, you can see the original file size, compressed file size and compression percentage statistics. You can also view the character frequency map on the left side and the encoded messages for each character on the right side. 

The encoding process creates three files named

1. "encoded.txt" - The compression version of the original file. (The compressed file is automatically named "encoded.txt")
2. "tree.txt" - A supporting file needed for decompression, which consists of serialized object with information regarding character to codeword map
3. "char.txt" - A helper file containing the character frequency map (the map shown on the left side in the above picture)

All the above files are stored using BitSet, hence they may not be in a human readable format.


### Decoder

The below screenshot shows the GUI for the Decoder.


![decoder](https://user-images.githubusercontent.com/18495886/34317797-2319a7e2-e77c-11e7-8cbd-51b947e81bbd.png)

1. Select the file called "encoded.txt" that you would have obtained from the encoding process.
2. Once the file is selected, name your output file.
3. Now click "Decode" button which will prompt saying that the file is restored and will show it's original size.

The decoding process needs two other supporting files ("char.txt", "tree.txt") which would have been created as by products during the encoding process.

### Future Implementations: Todo List

* Use an adaptive huffman technique
* Allow the user to specify the name of the output file that would be generated after encoding step
* Add feature for compression of multiple files

