/*
 * Your task for this file:
 *
 * add javadoc style comments to each of the functions. These
 * comments should include what the function does, a brief overview
 * of how it does it, what any parameters are for, what any
 * return values are for, and any error conditions, exceptions, etc
 *
 * You do NOT have to implement the functionality of this
 * file for assignment 1.1
 */
package edu.illinois.cs242.model;

import java.io.*;

import static edu.illinois.cs242.model.BitManipulationConstants.*;

/**
 * <p> Class for reading and writing to/from disk at bit-level granularity. Guarantees compactness, except in the case where
 * <code>flush</code> is called.
 *
 *  <p> In general, this class assumes that reading and write happen from the MSB side of a byte (e.g. any input bytes will
 * be read from MSB to LSB, regardless of Endianess).
 *
 * @author Jon Tedesco <tedesco1 @ illinois.edu>
 */

public class BitLibrary {

    /**
    * Objects to allow delegation to subclasses for input/output
    */
    private InputBitLibrary inputBitLibrary;
    private OutputBitLibrary outputBitLibrary;

    /**
     * Constructs an uninitialized BitLibrary object (for testing purposes)
     */
    public BitLibrary() {}

    /**
    * Constructs a new BitLibrary object.
    *
    * <p>Constructs a new BitLibrary object using input and output file objects. The input file
    * is read from by the BitLibrary, while the output file is written to. (e.g. to write to
    * particular file and read from it and the same time, File objects representing the same
    * file into this constructor). If the passed file does not exist, it will attempt to create it, but will propagate
    * any errors produced from java.io that occur while doing so.
    *
    * @see java.io.File for information on how to use File objects.
    *
    * @param inputFile File object to be read from by the BitLibrary class.
    * @param outputFile File object to be written to by the BitLibrary class.
    *
    * @return Returns (implicitly) a pointer to a new BitLibrary object that will read from inputFile
    *          and write to outputFile.
    *
    * @throws IOException Throws an IOException when the input and/or output File objects are null. Likewise,
    * it will throw an exception if opening input and/or output streams fail using these input objects.
    *
    */
	public BitLibrary(File inputFile, File outputFile) throws IOException {

        if(inputFile != null && !inputFile.exists()){
            inputFile.createNewFile();
        }
        if(outputFile != null && !outputFile.exists()){
            outputFile.createNewFile();
        }

        if(inputFile != null){
            inputBitLibrary = new InputBitLibrary(new FileInputStream(inputFile));
        }
        if (outputFile != null) {
            outputBitLibrary = new OutputBitLibrary(new FileOutputStream(outputFile));
        }
    }

    /**
     * Constructs a new BitLibrary object.
     *
     * <p>Constructs a new BitLibrary object using input and output file streams. The input stream
     * is read from by the BitLibrary, while the output stream is written to. (e.g. to write to
     * particular file and read from it and the same time, FileStream objects representing the same
     * file into this constructor).
     *
     * @see java.io.InputStream See InputStream for information on how to use InputStream objects.
     * @see java.io.OutputStream See OutputStream for information on how to use OutputStream objects.
     *
     * @param inputStream InputStream object to be read from by the BitLibrary class.
     * @param outputStream OutputStream object to be written to by the BitLibrary class.
     *
     * @return Returns (implicitly) a pointer to a new BitLibrary object that will read from inputStream
     *          and write to outputStream.
     *
     * @throws IOException Throws an IOException when the input and/or output streams are null.
     */
	public BitLibrary(InputStream inputStream, OutputStream outputStream) throws IOException  {
        inputBitLibrary = new InputBitLibrary(inputStream);
        outputBitLibrary = new OutputBitLibrary(outputStream);
	}


    /**
    * Reads one bit of data from the input stream or input file.
    *
    * <p>Reads one bit of data from the input File or InputStream, and returns it as a boolean, where
    * 1=true and 0=false. Reads from the current location in the inputStream, so that calling this method
    * twice in a row will read and return the values of two successive bits from the inputStream.
    *
    * <p>Delegates work to readNBits.
    *
    * @return Returns a boolean representing the bit read (where 1 corresponds to True and 0 corresponds to False)
    *
    * @throws IOException Throws an IOException if there are any exceptions thrown while reading from the
    *                      <code>InputStream</code> or input <code>File</code> object, propagating the error message
    *                      as well. Will also throw an exception if the input stream is null (was never
    *                      initialized properly).
    */
    public boolean readBit() throws IOException {
        //This will hold only one bit of useful data, in the MSB (Note that if this is not zero, we read a one)
        byte[] result;
        boolean bit;
        try{
            result = readNBits(1);

            if (result != null && result.length != 0) {
                bit = result[0]!=0;
            } else {
                throw new IOException("readNBits() failed to read data from input stream!");
            }
        } catch (IOException e) {
            throw new IOException("Error reading one bit:\n"+e.getMessage(), e);
        }
        return bit;
    }

    /**
    * Reads one byte of data from the input stream or input file.
    *
    * <p>Reads one byte of data from the input File or InputStream, and returns the data as type <code>byte<code>.
    * Reads from the current location in the inputStream, so that calling this method twice in a row will read and
    * return the values of two successive bytes from the inputStream.
    *
    * <p>Delegates work to readNBits.
    *
    * @return Returns one byte of date from the input file or data stream.
    *
    * @throws IOException Throws an IOException if there are any exceptions thrown while reading from the
    *                      <code>InputStream</code> or input <code>File</code> object, propagating the error message
    *                      as well. Will also throw an exception if the input stream or file is null (was never
    *                      initialized properly).
    */
    public byte readByte() throws IOException{

        byte result;
        try{
            byte[] resultArray = readNBits(ONE_BYTE);

            if(resultArray != null && resultArray.length != 0){
                result = resultArray[0];
            } else {
                throw new IOException("readNBits() failed to read data from input stream!");
            }
        } catch (IOException e) {
            throw new IOException("Error reading 8 bits:\n"+e.getMessage(), e);
        }

        return result;
    }

    /**
    * Reads one short (two bytes) of data from the input stream or input file.
    *
    * <p>Reads two bytes of data from the input File or InputStream, and returns the data as type <code>short<code>.
    * Reads from the current location in the inputStream, so that calling this method twice in a row will read and
    * return the values of two successive bytes from the inputStream. Will return either all of the data or none of it,
    * (it will not return a portion of the requested data if there is an error while reading the data). If an error
    * occurs, an exception will be thrown.
    *
    * <p>Delegates work to readNBits.
    *
    * @return Returns two bytes of data, represented as type <code>short<code>.
    *
    * @throws IOException Throws an IOException if there are any exceptions thrown while reading from the
    *                      <code>InputStream</code> or input <code>File</code> object, propagating the error message
    *                      as well. Will also throw an exception if the input stream or file is null (was never
    *                      initialized properly).
    */
    public short readShort() throws IOException{

        short result = 0;
        try{
            byte[] resultArray = readNBits(TWO_BYTES);

            if(resultArray != null && resultArray.length != 0){
                result |=  (((BYTE_MASK) & ((int) resultArray[0] << ONE_BYTE)));
                result |=  (((BYTE_MASK) & ((int) resultArray[1])));
            } else {
                throw new IOException("readNBits() failed to read data from input stream!");
            }
        } catch (IOException e) {
            throw new IOException("Error reading 16 bits:\n"+e.getMessage(), e);
        }

        return result;
    }

    /**
    * Reads one integer (four bytes) of data from the input stream or input file.
    *
    * <p>Reads four bytes of data from the input File or InputStream, and returns the data as type <code>int<code>.
    * Reads from the current location in the inputStream, so that calling this method twice in a row will read and
    * return the values of two four-byte chunks of data from the inputStream. Will return either all of the data or
    * none of it, (it will not return a portion of the requested data if there is an error while reading the data). If
    * an error occurs, an exception will be thrown.
    *
    * <p>Delegates work to readNBits.
    *
    * @return Returns four bytes of data, represented as a variable of type <code>int<code>.
    *
    * @throws IOException Throws an IOException if there are any exceptions thrown while reading from the
    *                      <code>InputStream</code> or input <code>File</code> object, propagating the error message
    *                      as well. Will also throw an exception if the input stream or file is null (was never
    *                      initialized properly).
    */
    public int readInt() throws IOException{

        int result = 0;
         try{
             byte[] resultArray = readNBits(FOUR_BYTES);

             if(resultArray != null && resultArray.length != 0){
                 result |=  (((BYTE_MASK) & ((int) resultArray[0])) << THREE_BYTES);
                 result |=  (((BYTE_MASK) & ((int) resultArray[1])) << TWO_BYTES);
                 result |=  (((BYTE_MASK) & ((int) resultArray[2])) << ONE_BYTE);
                 result |= (BYTE_MASK) & (resultArray[3]);
             } else {
                 throw new IOException("readNBits() failed to read data from input stream!");
             }
         } catch (IOException e) {
             throw new IOException("Error reading 32 bits:\n"+e.getMessage(), e);
         }

        return result;
    }

    /**
    * Reads one long (eight bytes) of data from the input stream or input file.
    *
    * <p>Reads eight bytes of data from the input File or InputStream, and returns the data as type <code>long<code>.
    * Reads from the current location in the inputStream, so that calling this method twice in a row will read and
    * return the values of two eight-byte chunks of data from the inputStream. Will return either all of the data or
    * none of it, (it will not return a portion of the requested data if there is an error while reading the data). If
    * an error occurs, an exception will be thrown.
    *
    * <p>Delegates work to readNBits.
    *
    * @return Returns eight bytes of data, represented as a variable of type <code>long<code>.
    *
    * @throws IOException Throws an IOException if there are any exceptions thrown while reading from the
    *                      <code>InputStream</code> or input <code>File</code> object, propagating the error message
    *                      as well. Will also throw an exception if the input stream or file is null (was never
    *                      initialized properly).
    */
    public long readLong() throws IOException{

        long result = 0;
         try{
             byte[] resultArray = readNBits(EIGHT_BYTES);

             if(resultArray != null && resultArray.length != 0){
                 result |=  (((BYTE_MASK) & ((int) resultArray[0] << SEVEN_BYTES)));
                 result |=  (((BYTE_MASK) & ((int) resultArray[1] << SIX_BYTES)));
                 result |=  (((BYTE_MASK) & ((int) resultArray[2] << FIVE_BYTES)));
                 result |=  (((BYTE_MASK) & ((int) resultArray[3] << FOUR_BYTES)));
                 result |=  (((BYTE_MASK) & ((int) resultArray[4] << THREE_BYTES)));
                 result |=  (((BYTE_MASK) & ((int) resultArray[5] << TWO_BYTES)));
                 result |=  (((BYTE_MASK) & ((int) resultArray[6] << ONE_BYTE)));
                 result |=  (((BYTE_MASK) & ((int) resultArray[7])));
             } else {
                 throw new IOException("readNBits() failed to read data from input stream!");
             }
         } catch (IOException e) {
             throw new IOException("Error reading 64 bits:\n"+e.getMessage(), e);
         }

        return result;
    }

    /**
    * Reads N bits of data from the input stream.
    *
    * <p>Reads N bits of data from the input File or InputStream, and returns the data as an array of bytes, where the
    * last bits are garbage bits, having value 0. (i.e. Reading 59 bits will result in returning an array of eight bytes,
    * where the first 59 bits correspond to actual data, and the last 5 bits are zeroed out). Likewise, this reads from
    * the current location in the inputStream, so that calling this method twice in a row will read and return the
    * values of N-bit chunks of data from the inputStream. Will return either all of the data or
    * none of it, (it will not return a portion of the requested data if there is an error while reading the data). If
    * an error occurs, an exception will be thrown.
    *
    * @param howMany Specifies the number of bits to read from the input stream or input file as an integer. If this
    *                  value is less than or equal to zero, this function will do nothing and return null.
    *
    * @return Returns N bits of data, represented as an array of bytes. If the parameter is zero, will return null.
    *
    * @throws IOException Throws an IOException if there are any exceptions thrown while reading from the
    *                      <code>InputStream</code> or input <code>File</code> object, propagating the error message
    *                      as well. Will also throw an exception if the input stream or file is null (was never
    *                      initialized properly). Likewise, this function will throw an exception on failing to read all
    *                      bits requested from input stream, including the case where we hit the end of the file.
    */
    public byte[] readNBits(int howMany) throws IOException {
        try {
            return inputBitLibrary.readNBits(howMany);
        } catch (NullPointerException npe) {
            throw new IOException("BitLibrary was not properly initialized!");
        }
    }

    /**
    * Writes one bit of data to the output stream or output file.
    *
    * <p>Writes one bit of data to the output stream or file, and returns nothing. Throws an exception if there is an
    * error while writing data.
    *
    * <p>Delegates work to writeNBits.
    *
    * @param bitValue The bit value to be written to the output file or output stream, represented as a boolean. (Where
    *  true represents 1 and false represents 0).
    *
    * @throws IOException Throws an IOException if there are any exceptions thrown while writing to the
    *                      <code>OutputStream</code> or output <code>File</code> object, propagating the error message
    *                      as well. Will also throw an exception if the output stream or file is null (if it was never
    *                      initialized properly).
    */
	public void writeBit(boolean bitValue) throws IOException{

         try{
             byte[] data = new byte[1];

             int dataBit = bitValue ? getOne() : 0;
             data[0] = (byte) (dataBit << ONE_BYTE-1);

             writeNBits(data, 1);

         } catch (IOException e) {
             throw new IOException("Error writing one bit:\n"+e.getMessage(), e);
         }
    }

    /**
     * Writes one byte of data to the output stream or output file.
     *
     * <p>Writes one byte of data to the output stream or file, and returns nothing. Throws an exception if there is any
     * error while writing data.
     *
     * @param byteValue The value to be written to the output file or output stream, represented as a variable of type
     *                  <code>byte<code>.
     *
     * @throws IOException Throws an IOException if there are any exceptions thrown while writing to the
     *                      <code>OutputStream</code> or output <code>File</code> object, propagating the error message
     *                      as well. Will also throw an exception if the output stream or file is null (if it was never
     *                      initialized properly).
     */
	public void writeByte(byte byteValue) throws IOException{

        try{
            byte[] data = new byte[1];
            data[0] = byteValue;

            writeNBits(data, ONE_BYTE);

        } catch (IOException e) {
            throw new IOException("Error writing 8 bits:\n"+e.getMessage(), e);
        }
    }

    /**
     * Writes two bytes of data to the output stream or output file.
     *
     * <p>Writes two bytes of data to the output stream or file, and returns nothing. Throws an exception if there is any
     * error while writing data.
     *
     * <p>Delegates work to writeNBits.
     *
     * @param shortValue The value to be written to the output file or output stream, represented as a variable of type
     *                  <code>short<code>, that holds two bytes of data.
     *
     * @throws IOException Throws an IOException if there are any exceptions thrown while writing to the
     *                      <code>OutputStream</code> or output <code>File</code> object, propagating the error message
     *                      as well. Will also throw an exception if the output stream or file is null (if it was never
     *                      initialized properly).
     */
	public void writeShort(short shortValue) throws IOException{

        try{
            byte[] data = new byte[2];
            data[0] = (byte) (shortValue >>> ONE_BYTE);
            data[1] = (byte) shortValue;

            writeNBits(data, TWO_BYTES);

        } catch (IOException e) {
            throw new IOException("Error writing 16 bits:\n"+e.getMessage(), e);
        }
    }

    /**
     * Writes four bytes of data to the output stream or output file.
     *
     * <p>Writes four bytes of data to the output stream or file, and returns nothing. Throws an exception if there is any
     * error while writing data.
     *
     * <p>Delegates work to writeNBits.
     *
     * @param intValue The value to be written to the output file or output stream, represented as a variable of type
     *                  <code>int<code>, that holds four bytes of data.
     *
     * @throws IOException Throws an IOException if there are any exceptions thrown while writing to the
     *                      <code>OutputStream</code> or output <code>File</code> object, propagating the error message
     *                      as well. Will also throw an exception if the output stream or file is null (if it was never
     *                      initialized properly).
     */
	public void writeInt(int intValue) throws IOException{

        try{
            byte[] data = new byte[4];
            data[0] = (byte) (intValue >>> THREE_BYTES);
            data[1] = (byte) (intValue >>> TWO_BYTES);
            data[2] = (byte) (intValue >>> ONE_BYTE);
            data[3] = (byte) intValue;

            writeNBits(data, FOUR_BYTES);

        } catch (IOException e) {
            throw new IOException("Error writing 32 bits:\n"+e.getMessage(), e);
        }
    }

    /**
     * Writes eight bytes of data to the output stream or output file.
     *
     * <p>Writes eight bytes of data to the output stream or file, and returns nothing. Throws an exception if there is any
     * error while writing data.
     *
     * <p>Delegates work to writeNBits.
     *
     * @param longValue The value to be written to the output file or output stream, represented as a variable of type
     *                  <code>long<code>, that holds eight bytes of data.
     *
     * @throws IOException Throws an IOException if there are any exceptions thrown while writing to the
     *                      <code>OutputStream</code> or output <code>File</code> object, propagating the error message
     *                      as well. Will also throw an exception if the output stream or file is null (if it was never
     *                      initialized properly).
     */
	public void writeLong(long longValue) throws IOException{

        try{
            byte[] data = new byte[8];
            data[0] = (byte) (longValue >>> SEVEN_BYTES); //Left half of long
            data[1] = (byte) (longValue >>> SIX_BYTES);
            data[2] = (byte) (longValue >>> FIVE_BYTES);
            data[3] = (byte) (longValue >>> FOUR_BYTES);
            data[4] = (byte) (longValue >>> THREE_BYTES); //Right half of long
            data[5] = (byte) (longValue >>> TWO_BYTES);
            data[6] = (byte) (longValue >>> ONE_BYTE);
            data[7] = (byte) longValue;

            writeNBits(data, EIGHT_BYTES);

        } catch (IOException e) {
            throw new IOException("Error writing 64 bits:\n"+e.getMessage(), e);
        }
    }

    /**
     * Writes N bytes of data to the output stream or output file.
     *
     * <p>Writes N bytes of data from the input File or InputStream, and takes the data as an array of bytes, where the
     * last bytes are garbage bytes, having value 0. (i.e. Writing 59 bytes would take an array of eight bytes,
     * where the first 59 bytes correspond to actual data, and the last 5 bytes are zeroed out). Likewise, this writes to
     * the current location in the inputStream, so that calling this method twice in a row will write to two, adjacent
     * N-bit chunks of data from the inputStream.
     *
     * <p>Delegates work to writeNBits.
     *
     * @param bytes The value to be written to the output file or output stream, represented as an array of bytes,
     *                  where the last bytes may not correspond to actual data. Note that calling this function with eight
     *                  bytes of data, but requesting 59 bytes be read will only write seven bytes (assuming the buffer
     *                  was empty before this was called)
     *
     * @param howMany Specifies the number of bytes to write to the output stream or output file as an integer. If this
     *                  value is less than or equal to zero, this function will do nothing
     *
     * @throws IOException Throws an IOException if there are any exceptions thrown while writing to the
     *                      <code>OutputStream</code> or output <code>File</code> object, propagating the error message
     *                      as well. Will also throw an exception if the output stream or file is null (if it was never
     *                      initialized properly).
     */
	public void writeNBytes(byte[] bytes, int howMany) throws IOException{
        writeNBits(bytes, howMany*8);
    }

    /**
    * Writes N bits of data to the output stream or output file.
    *
    * <p>Writes N bits of data from the input File or InputStream, and takes the data as an array of bytes, where the
    * last bits are garbage bits, having value 0. (i.e. Writing 59 bits would take an array of eight bytes,
    * where the first 59 bits correspond to actual data, and the last 5 bits are zeroed out). Likewise, this writes to
    * the current location in the inputStream, so that calling this method twice in a row will write to two, adjacent
    * N-bit chunks of data from the inputStream.
    *
    * @param inputData The value to be written to the output file or output stream, represented as an array of bytes,
    *                  where the last bits may not correspond to actual data. Note that calling this function with eight
    *                  bytes of data, but requesting 59 bytes be read will only write
    *
    * @param howMany Specifies the number of bits to write to the output stream or output file as an integer. If this
    *                  value is less than or equal to zero, this function will do nothing
    *
    * @throws IOException Throws an IOException if there are any exceptions thrown while writing to the
    *                      <code>OutputStream</code> or output <code>File</code> object, propagating the error message
    *                      as well. Will also throw an exception if the output stream or file is null (if it was never
    *                      initialized properly).
    */
    public void writeNBits(byte[] inputData, int howMany) throws IOException {
        try {
            outputBitLibrary.writeNBits(inputData, howMany);
        } catch (NullPointerException npe) {
            throw new IOException("BitLibrary was not properly initialized!");
        }
    }

    /**
    * Writes any data in the buffer to disk.
    *
    * <p>Forces any data in the buffers (either the local buffer or the buffer of the output stream) to be written to disk.
    * If there is less than one byte remaining to be written to disk, one byte will be written, appending zeros to the
    * end of the real data to fill the entire byte.
    *
    * <p>If the buffer is not completely full, the data in the buffer will be shifted all the way to the left before
    * writing to disk.
    *
    * @throws IOException Throws an IOException if there are any errors while writing to disk, and propagates the error
    *                      message itself. Will also throw an exception if the output stream or file is null (if it was
    *                      never initialized properly).
    */
	public void flush() throws IOException{
        try {
            outputBitLibrary.flush();
        } catch (NullPointerException npe) {
            throw new IOException("BitLibrary was not properly initialized!");
        }
    }

    /**
     * Responsible for cleaning the leftover memory and variables of the library (e.g. Stream objects and File objects)
     */
    public void close() throws IOException {
        if(inputBitLibrary != null && inputBitLibrary.getInputStream() != null){
            try {
                inputBitLibrary.getInputStream().close();
            } catch (IOException e) {
                throw new IOException("Error closing input and output streams:\n" + e.getMessage(), e);
            }
        }

        if(outputBitLibrary != null && outputBitLibrary.getOutputStream() != null){
            try {
                outputBitLibrary.getOutputStream().close();
            } catch (IOException e) {
                throw new IOException("Error closing input and output streams:\n" + e.getMessage(), e);
            }
        }
    }


    //Internal method used to compensate for Endianness
    private int getOne() {
        int test = 1;
        test = test >>> 1;
        test = test << 1;

        //If test is zero, then we can return 1 as normal, otherwise we must return the furthest right bit
        if(test == 0){
            return 0x0001; //00000000 00000000 00000000 00000001
        } else {
            return 0x8000; //00000000 00000000 00000000 00000001, in this case!
        }
    }

    public void setInputStream(InputStream inputStream) {
        inputBitLibrary.setInputStream(inputStream);
    }

    public InputStream getInputStream() {
        return inputBitLibrary.getInputStream();
    }

    public void setReadBufferCounter(int readBufferCounter) {
        inputBitLibrary.setReadBufferCounter(readBufferCounter);
    }

    public int getReadBufferCounter() {
        return inputBitLibrary.getReadBufferCounter();
    }

    public void setReadBuffer(byte readBuffer) {
        inputBitLibrary.setReadBuffer(readBuffer);
    }

    public byte getReadBuffer() {
        return inputBitLibrary.getReadBuffer();
    }

    public byte getWriteBuffer() {
        return outputBitLibrary.getWriteBuffer();
    }

    public void setWriteBuffer(byte writeBuffer) {
        outputBitLibrary.setWriteBuffer(writeBuffer);
    }

    public int getWriteBufferCounter() {
        return outputBitLibrary.getWriteBufferCounter();
    }

    public void setWriteBufferCounter(int writeBufferCounter) {
        outputBitLibrary.setWriteBufferCounter(writeBufferCounter);
    }

    public OutputStream getOutputStream() {
        return outputBitLibrary.getOutputStream();
    }
}
