package edu.illinois.cs242.model;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p> Subclass of <code>BitLibrary</code> that handles much of the input functionality.
 *
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 8, 2010
 */
public class InputBitLibrary extends BitLibrary {

    /**
    * An input stream object, representing the input
    */
    private InputStream inputStream = null;

    /**
    * A byte-typed variable used as an 8-bit buffer. This is crucial since we cannot write data to disk at the
    * granularity of bits, only bytes. This will be accounted for whenever we read or write, regardless of our
    * implementation.
    */
    private byte readBuffer = 0;

    /**
    * Variable used exclusively for counting the number of bits that hold actual data in the writeBuffer.
    */
    private int readBufferCounter = 0;

    /**
     * Constructor that builds the inputStream object
     */
    public InputBitLibrary(InputStream inputStream) {
        this.inputStream = inputStream; 
    }

    /**
     * {@inheritDoc}
     */
	public byte[] readNBits(int howMany) throws IOException{
        if(inputStream == null){
            throw new IOException("BitLibrary was not properly initialized! Cannot read from a null input stream!");
        } else if(howMany <= 0){
            return null;
        }

        if(howMany <= readBufferCounter){
            return readNBitsFromBufferOnly(howMany);

        } else {
            //First, read from the stream
            int bitsToRead = howMany - readBufferCounter;
            if(bitsToRead % 8 != 0){
                bitsToRead = (bitsToRead/8 + 1)*8;
            }
            byte[] data = new byte[bitsToRead/8];

            try{
                //Pull the actual data from disk
                int bytesRead = inputStream.read(data);

                //Check for errors (i.e. we hit the end of file or we generally just did not read the number of bits we were asked to read)
                if(bytesRead == -1){
                    throw new IOException("Failed to read " + bitsToRead + " bits from input stream. Reached end of input stream!");
                } else if(bytesRead < (bitsToRead/8)){
                    throw new IOException("Failed to read " + bitsToRead + " bits from file. Only read " + (bytesRead*8) + " bits of data!");
                }
            } catch (IOException e) {
                throw new IOException("Failed to read data:\n" + e.getMessage(), e);
            }

            //Create the array to return
            byte[] returnData;
            if(howMany % 8 == 0){
                returnData = new byte[(howMany /8)];
            } else {
                returnData = new byte[(howMany /8 + 1)];
            }

            //Build the return data!
            if(readBufferCounter == 0){ //The easy case, where the buffer was empty before
                return readNBitsToEmptyBuffer(data, returnData);

            } else {    //If the buffer was not empty before...
                return readNBitsWithNonEmptyBuffer(data, returnData);

            }


        }
    }

    /**
    * Private helper function to handle the case when we are reading and the data we need is already in the buffer
    */
    private byte[] readNBitsFromBufferOnly(int howMany) {
        //Zero out everything beyond the 'howMany' bits we care about
        byte data = (byte) ((readBuffer >>> (8-howMany)) << (8-howMany));

        //Update buffer and bufferCounter
        readBuffer = (byte) (readBuffer << howMany);
        readBufferCounter -= howMany;

        //Prepare and return data
        byte[] dataArray = new byte[1];
        dataArray[0] = data;
        return dataArray;
    }

    /**
     * Private helper function to handle the case when we are writing and the buffer is empty, and we will only need
     *  to write to the buffer
     */
    private byte[] readNBitsToEmptyBuffer(byte[] data, byte[] returnData) {
        for(int curByte = 0; curByte<data.length; curByte++){
            returnData[curByte] = data[curByte];
        }

        return returnData;
    }

    /**
     * Private helper function to handle the case when we are reading and the buffer is not empty
     *      (but we still need to hit disk as well as the buffer)
     */
    private byte[] readNBitsWithNonEmptyBuffer(byte[] data, byte[] returnData) {
        int curByte = 0;

        //Fill the first entry of the return data array with the buffer and data read
        returnData[curByte] = readBuffer;
        returnData[curByte] = (byte) (returnData[curByte] | (byte) (data[curByte] >>> readBufferCounter));

        //If there was more data returned, continue until we're all done
        while(curByte < data.length-1){
            curByte++;

            //Get the remainder of this current byte of data
            returnData[curByte] = (byte) (data[curByte-1] << readBufferCounter);

            //Get the first part of the next byte of data
            returnData[curByte] = (byte) (data[curByte] | (data[curByte] >>> readBufferCounter));
        }

        //Put the remaining data into the buffer again -- buffer counter should not change
        //      (since we read a multiple of eight bits from disk)
        readBuffer = (byte) (data[curByte] << readBufferCounter);

        return returnData;
    }

    public byte getReadBuffer() {
        return readBuffer;
    }

    public void setReadBuffer(byte readBuffer) {
        this.readBuffer = readBuffer;
    }

    public int getReadBufferCounter() {
        return readBufferCounter;
    }

    public void setReadBufferCounter(int readBufferCounter) {
        this.readBufferCounter = readBufferCounter;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
