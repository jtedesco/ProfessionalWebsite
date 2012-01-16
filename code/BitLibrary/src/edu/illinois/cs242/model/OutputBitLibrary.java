package edu.illinois.cs242.model;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <p> Subclass of <code>BitLibrary</code> that contains specific implementation details for the output functionality of BitLibrary  
 *
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 8, 2010
 */
public class OutputBitLibrary extends BitLibrary
{
    /**
    * An output stream object, representing the input
    */
    private OutputStream outputStream = null;

    /**
    * A byte-typed variable used as an 8-bit buffer. This is crucial since we cannot write data to disk at the
    * granularity of bits, only bytes. This will be accounted for whenever we read or write, regardless of our
    * implementation.
    */
    private byte writeBuffer = 0;

    /**
    * Variable used exclusively for counting the number of bits that hold actual data in the readBuffer.
    */
    private int writeBufferCounter = 0;

    /**
     * Constructor that builds the outputStream object
     */
    public OutputBitLibrary(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * {@inheritDoc}
     */
	public void writeNBits(byte[] inputData, int howMany) throws IOException {
        if(outputStream == null){
            throw new IOException("BitLibrary was not properly initialized! Cannot write to a null output stream!");
        } else if(howMany <= 0){
            return;
        }

        if(howMany < (8-writeBufferCounter)){         //Case where we write only to the buffer
            writeNBitsToBufferOnly(inputData, howMany);


        }else{

            //Build the data to write!
            if(writeBufferCounter == 0){ //The easy case, where the buffer was empty before

                writeNBitsWithEmptyBuffer(inputData, howMany);

            } else { //If the buffer was not empty before...

                writeNBitsWithNonEmptyBuffer(inputData, howMany);

            }
        }
    }

    /**
     * Private helper function that handles the case where we write only to the buffer, not to the stream
     */
    private void writeNBitsToBufferOnly(byte[] inputData, int howMany) {
        //Figure out what the new buffer data should look like
        inputData[0] = (byte) ((inputData[0] >>> (8-howMany)) << (8 - howMany));
        byte data = (byte) (writeBuffer | (inputData[0] >>> writeBufferCounter));

        //Update buffer data
        setWriteBuffer(data);
        setWriteBufferCounter(getWriteBufferCounter() + howMany);
    }

    /**
     * Private helper function that handles the case where we write with an initially empty buffer
     */
    private void writeNBitsWithEmptyBuffer(byte[] inputData, int howMany) throws IOException {
        try{

            //Create local vars
            int bytesToWrite = howMany/8;
            byte[] data = new byte[inputData.length];

            //If we are not writing an even multiple of bytes, take the spillover and put it to the buffer
            if(howMany % 8 != 0){

                //Resize the data array to be written (remove last entry and assign it to the buffer)
                data = new byte[bytesToWrite];
                for(int index = 0; index<bytesToWrite; index++){
                    data[index] = inputData[index];
                }
                setWriteBuffer(inputData[bytesToWrite]);
            } else {

                //Fill the data array to be written
                for(int index = 0; index<bytesToWrite; index++){
                    data[index] = inputData[index];
                }
            }

            //Write the data to disk
            outputStream.write(data);
            outputStream.flush();

        } catch (IOException e) {
            throw new IOException("Failed to write data:\n" + e.getMessage(), e);
        }
    }

    /**
     * Private helper function that handles the case where we write with an initially non-empty buffer
     */
    private void writeNBitsWithNonEmptyBuffer(byte[] inputData, int howMany) throws IOException {
        //Calculate the number of bytes we expect to be written and initialize local vars
        int curByte = 0;
        int bytesToWrite = (howMany+writeBufferCounter)/8;
        byte[] data = new byte[bytesToWrite];

        //Fill the first entry of the return data array with the buffer and data read
        data[curByte] = writeBuffer;
        data[curByte] = (byte) (data[curByte] | (byte) (inputData[curByte] >>> writeBufferCounter));

        //If there was more data returned, continue until we're all done
        while(curByte < bytesToWrite-1){
            curByte++;

            //Get the remainder of this current byte of data
            data[curByte] = (byte) (inputData[curByte-1] << (8-writeBufferCounter));

            //Get the first part of the next byte of data
            data[curByte] = (byte) (data[curByte] | (inputData[curByte] >>> writeBufferCounter));
        }

        //Put the remaining data into the buffer again, and update the buffer counter
        writeBuffer = (byte) (inputData[curByte] << (8-writeBufferCounter));
        writeBufferCounter = ((howMany + writeBufferCounter) % 8);


        //Actually write the data to disk
        outputStream.write(data);
    }

    /**
     * {@inheritDoc}
     */
    public void flush() throws IOException{
        if(outputStream == null){
            throw new IOException("BitLibrary was not properly initialized! Cannot write to a null output stream!");
        }

        //Zero out the right 8-writeBufferCounter bits by bit shifting right (8-writeBufferCounter) bits and back again
        byte bufferedData = (byte) ((writeBuffer >>> (8-writeBufferCounter)) << (8-writeBufferCounter));


        try {
            //Write the buffer to memory
            byte[] bufferedDataArray = new byte[1];
            bufferedDataArray[0] = bufferedData;
            outputStream.write(bufferedDataArray);

            //Update buffer variables
            writeBufferCounter = 0;
            writeBuffer = 0;

            //Flush the output stream!
            outputStream.flush();

        } catch (IOException e) {
            throw new IOException("Error flushing buffered data to disk!\n" + e.getMessage(), e);
        }
    }

    public byte getWriteBuffer() {
        return writeBuffer;
    }

    public void setWriteBuffer(byte writeBuffer) {
        this.writeBuffer = writeBuffer;
    }

    public int getWriteBufferCounter() {
        return writeBufferCounter;
    }

    public void setWriteBufferCounter(int writeBufferCounter) {
        this.writeBufferCounter = writeBufferCounter;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}
