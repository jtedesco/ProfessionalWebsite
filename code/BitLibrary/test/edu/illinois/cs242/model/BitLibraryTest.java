package edu.illinois.cs242.model;

import junit.framework.TestCase;

import java.io.*;
import java.util.Random;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyByte;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.*;

public class BitLibraryTest extends TestCase {

    /*
     * Instance variables to get a handle on mock input and output streams that will be used throughout testing
     */
    private InputStream mockInputStream;
    private OutputStream mockOutputStream;


    /*
     * Builds a BitLibrary using mock input and output streams for testing purposes
     */
    private BitLibrary constructMockBitLibrary(){
        mockInputStream = mock(InputStream.class);
        mockOutputStream = mock(OutputStream.class);
        try {
            return constructBitLibrary(mockInputStream, mockOutputStream);
        } catch (IOException e) {
            fail("Threw an exception creating the BitLibrary!");
            return null;
        }
    }

    /*
     * Builds an actual BitLibrary object, but using mock input and output streams again
     *  (This is useful for tracking functions called within the library itself)
     */
    private BitLibrary constructSpiedBitLibrary() {
        return spy(constructMockBitLibrary());
    }

    /*
     * Builds an actual BitLibrary object using passed inputStream and outputStream objects
     */
    private BitLibrary constructBitLibrary(InputStream inputStream, OutputStream outputStream) throws IOException {
       return new BitLibrary(inputStream, outputStream);
    }

    /*
     * Test that the constructor that takes InputStream and OutputStream objects works correctly with legitimate parameters
     */
    public void testBitLibraryInputStreamOutputStream() {
        BitLibrary streamLibrary = null;
        try {
            File newFile = lazyCreateTempFile();
            streamLibrary = constructBitLibrary(new FileInputStream(newFile), new FileOutputStream(newFile));
        } catch (FileNotFoundException e) {
            fail("Should not have thrown an exception!");
        } catch (IOException e) {
            fail("Threw an exception creating the BitLibrary!");
        }
        assertNotNull(streamLibrary);
    }

    /*
     * Test that the constructor that takes InputStream and OutputStream objects works correctly with legitimate parameters
     */
    public void testBitLibraryInputStreamOutputStreamBadInputs() {
        BitLibrary streamLibrary = null;
        File newFile = lazyCreateTempFile();
        try {
            streamLibrary = constructBitLibrary(null, new FileOutputStream(newFile));
        } catch (IOException e) {
            fail("Should have thrown an exception for passing it a null pointer!");
        }
        assertNotNull(streamLibrary);

        streamLibrary = null;
        try {
            streamLibrary = constructBitLibrary(new FileInputStream(newFile), null);
        } catch (IOException e) {
            fail("Should have thrown an exception for passing it a null pointer!");
        }
        assertNotNull(streamLibrary);

        streamLibrary = null;
        try {
            streamLibrary = constructBitLibrary(null, null);
        } catch (IOException e) {
            fail("Should have thrown an exception for passing it a null pointer!");
        }
        assertNotNull(streamLibrary);
    }

    /*
     * Test that the constructor that takes File objects works correctly with legitimate parameters, and that the result
     * object using this constructor results in an identical object to any creating using the other (given parameters
     * are the same)
     */
    public void testBitLibraryFileFile() {
        BitLibrary fileLibrary = null;
        File newFile = lazyCreateTempFile();
        try {
            fileLibrary = new BitLibrary(newFile, newFile);
        } catch (IOException e) {
            fail("Should not have thrown an exception when initializing File objects!");
        }
        BitLibrary streamLibrary = null;
        try {
            streamLibrary = constructBitLibrary(new FileInputStream(newFile), new FileOutputStream(newFile));
        } catch (FileNotFoundException e) {
            fail("Should not have thrown an exception when initializing FileInputStream and FileOutputStream objects!");
        } catch (IOException e) {
            fail("Threw an exception creating the BitLibrary!");
        }
        assertNotNull(fileLibrary);
        assertNotNull(fileLibrary.getInputStream());
        assertNotNull(fileLibrary.getOutputStream());
    }

    /*
     * Test that calling the File constructor creates the files if they don't already exist
     */
    public void testBitLibraryFileFileNonExistentFiles(){
        BitLibrary bitLibrary = null;
        File newFile1 = new File("newFile1");
        File newFile2 = new File("newFile2");

        assertNotNull(newFile1);
        assertNotNull(newFile2);
        newFile1.delete();
        newFile2.delete();
        assertFalse(newFile1.exists());
        assertFalse(newFile2.exists());

        try {
            bitLibrary = new BitLibrary(newFile1, newFile2);
        } catch (IOException e) {
            fail("Should not have thrown an exception!");
        }

        assertTrue(newFile1.exists());
        assertTrue(newFile2.exists());
        assertNotNull(bitLibrary);

        newFile1.delete();
        newFile2.delete();
    }

    /*
     * Test that the constructor that takes File objects works correctly with bad parameters
     */
    public void testBitLibraryFileFileBadInputs() {
        BitLibrary streamLibrary = null;
        File newFile = lazyCreateTempFile();
        try {
            streamLibrary = constructBitLibrary(null, new FileOutputStream(newFile));
        } catch (IOException e) {
            fail("Should not have thrown an exception for passing it a null pointer!");
        }
        assertNotNull(streamLibrary);

        streamLibrary = null;
        try {
            streamLibrary = constructBitLibrary(new FileInputStream(newFile), null);
        } catch (IOException e) {
            fail("Should not have thrown an exception for passing it a null pointer!");
        }
        assertNotNull(streamLibrary);

        streamLibrary = null;
        try {
            streamLibrary = constructBitLibrary(null, null);
        } catch (IOException e) {
            fail("Should not have thrown an exception for passing it a null pointer!");
        }
        assertNotNull(streamLibrary);
    }

    /*
     * Tests that the library throws exceptions when it is unitialized, as we expect
     */
    public void testNoInitializations() {
        //Create an uninitialized BitLibrary
        BitLibrary bitLibrary = new BitLibrary();

        //Test that readBit fails if library is uninitialized
        try{
            bitLibrary.readBit();
            fail("Should have thrown an exception when trying to read a bit to uninitialized library");
        } catch (IOException exception){}

        //Test that readByte fails if library is uninitialized
        try{
            bitLibrary.readByte();
            fail("Should have thrown an exception when trying to read a bit to uninitialized library");
        } catch (IOException exception){}

        //Test that readShort fails if library is uninitialized
        try{
            bitLibrary.readShort();
            fail("Should have thrown an exception when trying to read a bit to uninitialized library");
        } catch (IOException exception){}

        //Test that readInt fails if library is uninitialized
        try{
            bitLibrary.readInt();
            fail("Should have thrown an exception when trying to read a bit to uninitialized library");
        } catch (IOException exception){}

        //Test that readLong fails if library is uninitialized
        try{
            bitLibrary.readLong();
            fail("Should have thrown an exception when trying to read a bit to uninitialized library");
        } catch (IOException exception){}

        //Test that readNBits fails if library is uninitialized
        try{
            bitLibrary.readNBits(Math.abs(randomInt()));
            fail("Should have thrown an exception when trying to read a bit to uninitialized library");
        } catch (IOException exception){}

        //Test that writeBit fails if library is uninitialized
        try{
            bitLibrary.writeBit(randomBoolean());
            fail("Should have thrown an exception when trying to write a bit to uninitialized library");
        } catch (IOException exception){}

        //Test that writeByte fails if library is uninitialized
        try{
            bitLibrary.writeByte(randomByte());
            fail("Should have thrown an exception when trying to write a bit to uninitialized library");
        } catch (IOException exception){}

        //Test that writeShort fails if library is uninitialized
        try{
            bitLibrary.writeShort(randomShort());
            fail("Should have thrown an exception when trying to write a bit to uninitialized library");
        } catch (IOException exception){}

        //Test that writeInt fails if library is uninitialized
        try{
            bitLibrary.writeInt(randomInt());
            fail("Should have thrown an exception when trying to write a bit to uninitialized library");
        } catch (IOException exception){}

        //Test that writeLong fails if library is uninitialized
        try{
            bitLibrary.writeLong(randomLong());
            fail("Should have thrown an exception when trying to write a bit to uninitialized library");
        } catch (IOException exception){}

        //Test that writeNBytes fails if library is uninitialized
        int length = Math.abs(randomShort());
        byte[] randomByteArray = new byte[length];
        for( byte thisByte : randomByteArray){
            thisByte = randomByte();
        }
        try{
            bitLibrary.writeNBytes(randomByteArray, length/Math.abs(randomShort()));
            fail("Should have thrown an exception when trying to write a bit to uninitialized library");
        } catch (IOException exception){}

        //Test that writeNBits fails if library is uninitialized
        length = Math.abs(randomShort());
        randomByteArray = new byte[length];
        for( byte thisByte : randomByteArray){
            thisByte = randomByte();
        }
        try{
            bitLibrary.writeNBits(randomByteArray, (length*8)/Math.abs(randomShort()));
            fail("Should have thrown an exception when trying to write a bit to uninitialized library");
        } catch (IOException exception){}
     }

    //Verify that readBit(), if buffer is empty, will call read() on the input stream
    public void testMockReadBit() {
        //Setup
        BitLibrary bitLibrary = constructMockBitLibrary();
        InputStream mockInputStream = bitLibrary.getInputStream();
        byte[] expectedParameter = new byte[1];
        try {
            when(mockInputStream.read(eq(expectedParameter))).thenReturn(1);
        } catch (IOException e) {
            fail("Mocking out behavior of input stream failed!");
        }

        try {
            //Test
            bitLibrary.readBit();

            //Verify
            verify(mockInputStream, times(1)).read(eq(expectedParameter));
            verify(mockOutputStream, never()).write(anyByte());
            verify(mockOutputStream, never()).write((byte[]) any(), anyInt(), anyInt());

        } catch (IOException e) {
            fail("Should not have thrown an IOException!");
        }

    }

    //Verify that readByte(), if buffer is empty, will call read() on the input stream
    public void testMockReadByte() {
        //Setup
        BitLibrary bitLibrary = constructMockBitLibrary();
        InputStream mockInputStream = bitLibrary.getInputStream();
        byte[] expectedParameter = new byte[1];
        try {
            when(mockInputStream.read(eq(expectedParameter))).thenReturn(1);
        } catch (IOException e) {
            fail("Mocking out behavior of input stream failed!");
        }

        try {
            //Test
            bitLibrary.readByte();

            //Verify
            verify(mockInputStream, times(1)).read(eq(expectedParameter));
            verify(mockOutputStream, never()).write(anyByte());
            verify(mockOutputStream, never()).write((byte[]) any(), anyInt(), anyInt());

        } catch (IOException e) {
            fail("Should not have thrown an IOException!");
        }

    }

    //Verify that readShort(), if buffer is empty, will call read() on the input stream
    public void testMockReadShort() {
        //Setup
        BitLibrary bitLibrary = constructMockBitLibrary();
        InputStream mockInputStream = bitLibrary.getInputStream();
        byte[] expectedParameter = new byte[2];
        try {
            when(mockInputStream.read(eq(expectedParameter))).thenReturn(2);
        } catch (IOException e) {
            fail("Mocking out behavior of input stream failed!");
        }

        try {
            //Test
            bitLibrary.readShort();

            //Verify
            verify(mockInputStream, times(1)).read(eq(expectedParameter));
            verify(mockOutputStream, never()).write((byte[]) any());
            verify(mockOutputStream, never()).write((byte[]) any(), anyInt(), anyInt());

        } catch (IOException e) {
            fail("Should not have thrown an IOException!");
        }
    }

    //Verify that readInt(), if buffer is empty, will call read() on the input stream
    public void testMockReadInt() {
        //Setup
        BitLibrary bitLibrary = constructMockBitLibrary();
        InputStream mockInputStream = bitLibrary.getInputStream();
        byte[] expectedParameter = new byte[4];
        try {
            when(mockInputStream.read(eq(expectedParameter))).thenReturn(4);
        } catch (IOException e) {
            fail("Mocking out behavior of input stream failed!");
        }

        try {
            //Test
            bitLibrary.readInt();

            //Verify
            verify(mockInputStream, times(1)).read(eq(expectedParameter));
            verify(mockOutputStream, never()).write((byte[]) any());
            verify(mockOutputStream, never()).write((byte[]) any(), anyInt(), anyInt());

        } catch (IOException e) {
            fail("Should not have thrown an IOException!");
        }
    }

    //Verify that readLong(), if buffer is empty, will call read() on the input stream
    public void testMockReadLong() {
        //Setup
        BitLibrary bitLibrary = constructMockBitLibrary();
        InputStream mockInputStream = bitLibrary.getInputStream();
        byte[] expectedParameter = new byte[8];
        try {
            when(mockInputStream.read(eq(expectedParameter))).thenReturn(8);
        } catch (IOException e) {
            fail("Mocking out behavior of input stream failed!");
        }

        try {
            //Test
            bitLibrary.readLong();

            //Verify
            verify(mockInputStream, times(1)).read(eq(expectedParameter));
            verify(mockOutputStream, never()).write((byte[]) any());
            verify(mockOutputStream, never()).write((byte[]) any(), anyInt(), anyInt());

        } catch (IOException e) {
            fail("Should not have thrown an IOException!");
        }
    }

    //Verify that readNBits(), if buffer is empty, will call read() on the input stream
	public void testMockReadNBits() {
        //Setup
        BitLibrary bitLibrary = constructMockBitLibrary();
        InputStream mockInputStream = bitLibrary.getInputStream();
        short numBitsToRead = (short) Math.abs(randomShort());
        while(numBitsToRead <= 7){
            numBitsToRead = (short) Math.abs(randomShort());
        }
        int numBytes = (numBitsToRead % 8 == 0) ? numBitsToRead/8 : numBitsToRead/8+1;
        byte[] expectedParameter = new byte[numBytes];
        try {
            when(mockInputStream.read(eq(expectedParameter))).thenReturn(numBytes);
        } catch (IOException e) {
            fail("Mocking out behavior of input stream failed!");
        }

        try {
            //Test
            bitLibrary.readNBits(numBitsToRead);

            //Verify
            verify(mockInputStream, times(1)).read(eq(expectedParameter));
            verify(mockOutputStream, never()).write((byte[]) any());
            verify(mockOutputStream, never()).write((byte[]) any(), anyInt(), anyInt());

        } catch (IOException e) {
            fail("Should not have thrown an IOException!");
        }
	}

    //Verify that writeBit(), if buffer is empty, will call niether write() nor read() on the i/o streams
	public void testMockWriteBit() {
        //Setup
        BitLibrary bitLibrary = constructMockBitLibrary();

        try {
            //Test
            bitLibrary.writeBit(randomBoolean());

            //Verify
            verify(mockInputStream, never()).read();
            verify(mockOutputStream, never()).write(anyInt());
            verify(mockOutputStream, never()).write((byte[]) any());
            verify(mockOutputStream, never()).write((byte[]) any(), anyInt(), anyInt());

        } catch (IOException e) {
            fail("Should not have thrown an IOException!");
        }
	}

    //Verify that writeByte(), even when the buffer is empty, will call write() on the output stream, and will not call read()
    public void testMockWriteByte() {
        //Setup
        BitLibrary bitLibrary = constructMockBitLibrary();

        try {
            //Test
            bitLibrary.writeByte(randomByte());

            //Verify
            verify(mockInputStream, never()).read();
            verify(mockOutputStream, times(1)).write((byte[]) any());

        } catch (IOException e) {
            fail("Should not have thrown an IOException!");
        }
    }

    //Verify that writeShort(), even when the buffer is empty, will call write() on the output stream, and will not call read()
    public void testMockWriteShort() {
        //Setup
        BitLibrary bitLibrary = constructMockBitLibrary();

        try {
            //Test
            bitLibrary.writeShort(randomShort());

            //Verify
            verify(mockInputStream, never()).read();
            verify(mockOutputStream, times(1)).write((byte[]) any());

        } catch (IOException e) {
            fail("Should not have thrown an IOException!");
        }
	}

    //Verify that writeInt(), even when the buffer is empty, will call write() on the output stream, and will not call read()
	public void testMockWriteInt() {
        //Setup
        BitLibrary bitLibrary = constructMockBitLibrary();

        try {
            //Test
            bitLibrary.writeInt(randomInt());

            //Verify
            verify(mockInputStream, never()).read();
            verify(mockOutputStream, times(1)).write((byte[]) any());

        } catch (IOException e) {
            fail("Should not have thrown an IOException!");
        }
	}

    //Verify that writeLong(), even when the buffer is empty, will call write() on the output stream, and will not call read()
	public void testMockWriteLong() {
        //Setup
        BitLibrary bitLibrary = constructMockBitLibrary();

        try {
            //Test
            bitLibrary.writeLong(randomLong());

            //Verify
            verify(mockInputStream, never()).read();
            verify(mockOutputStream, times(1)).write((byte[]) any());

        } catch (IOException e) {
            fail("Should not have thrown an IOException!");
        }
	}

    //Verify that readBit() correctly delegates its work to readNBits()
    public void testReadBitCallsReadNBits() {
        //Setup
        lazyCreateTempFile();
        BitLibrary spiedBitLibrary = constructSpiedBitLibrary();
        InputStream mockInputStream = spiedBitLibrary.getInputStream();
        byte[] expectedParameter = new byte[1];
        try {
            when(mockInputStream.read(eq(expectedParameter))).thenReturn(1);
        } catch (IOException e) {
            fail("Mocking out behavior of input stream failed!");
        }

        try {
            //Test
            spiedBitLibrary.readBit();

            //Verify
            verify(spiedBitLibrary, times(1)).readNBits(1);

        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }

    }

    //Verify that readByte() correctly delegates its work to readNBits()
    public void testReadByteCallsReadNBits() {
        //Setup
        lazyCreateTempFile();
        BitLibrary spiedBitLibrary = constructSpiedBitLibrary();
        InputStream mockInputStream = spiedBitLibrary.getInputStream();
        byte[] expectedParameter = new byte[1];
        try {
            when(mockInputStream.read(eq(expectedParameter))).thenReturn(1);
        } catch (IOException e) {
            fail("Mocking out behavior of input stream failed!");
        }

        try {
            //Test
            spiedBitLibrary.readByte();

            //Verify
            verify(spiedBitLibrary, times(1)).readNBits(8);

        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }
    }

    //Verify that readShort() correctly delegates its work to readNBits()
    public void testReadShortCallsReadNBits() {
        //Setup
        lazyCreateTempFile();
        BitLibrary spiedBitLibrary = constructSpiedBitLibrary();
        InputStream mockInputStream = spiedBitLibrary.getInputStream();
        byte[] expectedParameter = new byte[2];
        try {
            when(mockInputStream.read(eq(expectedParameter))).thenReturn(2);
        } catch (IOException e) {
            fail("Mocking out behavior of input stream failed!");
        }

        try {
            //Test
            spiedBitLibrary.readShort();

            //Verify
            verify(spiedBitLibrary, times(1)).readNBits(16);

        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }
    }

    //Verify that readInt() correctly delegates its work to readNBits()
    public void testReadIntCallsReadNBits() {
        //Setup
        lazyCreateTempFile();
        BitLibrary spiedBitLibrary = constructSpiedBitLibrary();
        InputStream mockInputStream = spiedBitLibrary.getInputStream();
        byte[] expectedParameter = new byte[4];
        try {
            when(mockInputStream.read(eq(expectedParameter))).thenReturn(4);
        } catch (IOException e) {
            fail("Mocking out behavior of input stream failed!");
        }

        try {
            //Test
            spiedBitLibrary.readInt();

            //Verify
            verify(spiedBitLibrary, times(1)).readNBits(32);

        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }
    }

    //Verify that readLong() correctly delegates its work to readNBits()
    public void testReadLongCallsReadNBits() {
        //Setup
        lazyCreateTempFile();
        BitLibrary spiedBitLibrary = constructSpiedBitLibrary();
        InputStream mockInputStream = spiedBitLibrary.getInputStream();
        byte[] expectedParameter = new byte[8];
        try {
            when(mockInputStream.read(eq(expectedParameter))).thenReturn(8);
        } catch (IOException e) {
            fail("Mocking out behavior of input stream failed!");
        }

        try {
            //Test
            spiedBitLibrary.readLong();

            //Verify
            verify(spiedBitLibrary, times(1)).readNBits(64);

        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }
    }

    //Verify that writeBit() correctly delegates its work to writeNBits()
    public void testWriteBitCallsWriteNBits() {
        //Setup
        lazyCreateTempFile();
        BitLibrary spiedBitLibrary = constructSpiedBitLibrary();
        boolean randomBoolean = randomBoolean();
        byte[] expected = new byte[1];
        expected[0] = (byte) (randomBoolean ? getOne() : 0);
        expected[0] = (byte) (expected[0] << 7);

        try {
            //Test
            spiedBitLibrary.writeBit(randomBoolean);

            //Verify
            verify(spiedBitLibrary, times(1)).writeNBits(eq(expected), eq(1));

        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }
    }

    //Verify that writeByte() correctly delegates its work to writeNBits()
    public void testWriteByteCallsWriteNBits() {
        //Setup
        lazyCreateTempFile();
        BitLibrary spiedBitLibrary = constructSpiedBitLibrary();
        byte randomByte = randomByte();
        byte[] expected = {randomByte};

        try {
            //Test
            spiedBitLibrary.writeByte(randomByte);

            //Verify
            verify(spiedBitLibrary, times(1)).writeNBits(eq(expected), eq(8));

        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }
    }

    //Verify that writeShort() correctly delegates its work to writeNBits()
    public void testWriteShortCallsWriteNBits() {
        //Setup
        lazyCreateTempFile();
        BitLibrary spiedBitLibrary = constructSpiedBitLibrary();
        short randomShort = randomShort();
        byte[] expected = new byte[2];
        expected[0] = (byte) (randomShort >>> 8); //Take left half of short
        expected[1] = (byte) randomShort; //Take right half of short

        try {
            //Test
            spiedBitLibrary.writeShort(randomShort);

            //Verify
            verify(spiedBitLibrary, times(1)).writeNBits(eq(expected), eq(16));

        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }
    }

    //Verify that readInt() correctly delegates its work to writeNBits()
    public void testWriteIntCallsWriteNBits() {
        //Setup
        lazyCreateTempFile();
        BitLibrary spiedBitLibrary = constructSpiedBitLibrary();
        int randomInt = randomInt();
        byte[] expected = new byte[4];
        expected[0] = (byte) (randomInt >>> 24); //Take left half of int
        expected[1] = (byte) (randomInt >>> 16);
        expected[2] = (byte) (randomInt >>> 8); //Take right half of int
        expected[3] = (byte) randomInt;

        try {
            //Test
            spiedBitLibrary.writeInt(randomInt);

            //Verify
            verify(spiedBitLibrary, times(1)).writeNBits(eq(expected), eq(32));

        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }
    }

    //Verify that writeLong() correctly delegates its work to writeNBits()
    public void testWriteLongCallsWriteNBits() {
        //Setup
        lazyCreateTempFile();
        BitLibrary spiedBitLibrary = constructSpiedBitLibrary();
        long randomLong = randomLong();
        byte[] expected = new byte[8];
        expected[0] = (byte) (randomLong >>> 56); //Left half of long
        expected[1] = (byte) (randomLong >>> 48);
        expected[2] = (byte) (randomLong >>> 40);
        expected[3] = (byte) (randomLong >>> 32);
        expected[4] = (byte) (randomLong >>> 24); //Right half of long
        expected[5] = (byte) (randomLong >>> 16);
        expected[6] = (byte) (randomLong >>> 8);
        expected[7] = (byte) randomLong;

        try {
            //Test
            spiedBitLibrary.writeLong(randomLong);

            //Verify
            verify(spiedBitLibrary, times(1)).writeNBits(eq(expected), eq(64));

        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }
    }

    //Verify that writeNBytes() correctly delegates its work to writeNBits()
    public void testWriteNBytesCallsWriteNBits() {
        //Setup
        lazyCreateTempFile();
        BitLibrary spiedBitLibrary = constructSpiedBitLibrary();
        int numberOfBytes = Math.abs(randomShort());
        byte[] expected = new byte[numberOfBytes];
        for(byte entry : expected){
            entry = randomByte();
        }

        try {
            //Test
            spiedBitLibrary.writeNBytes(expected, numberOfBytes);

            //Verify
            verify(spiedBitLibrary, times(1)).writeNBits(eq(expected), eq(8*numberOfBytes));

        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }
    }

    //Verify that we throw an exception if we try to read a bit, and there is no data to read
    public void testReadBitWithNoData() {
        //Setup
        File newFile = hardCreateTempFile();

        BitLibrary bitLibrary = null;
        try {
            bitLibrary = constructBitLibrary(new FileInputStream(newFile), new FileOutputStream(newFile));
        } catch (FileNotFoundException e) {
            fail("Should not have thrown IOException!");
        } catch (IOException e) {
            fail("Threw an exception creating the BitLibrary!");
        }

        //Test & Verify
        try{
            bitLibrary.readBit();
            fail("Should have thrown an exception because there is no data to read!");
        } catch (IOException ioe){}
    }

    //Test that, when the library has a half-full buffer for writing, the streams do not get called, but do after we
    // flush the BitLibrary
    public void testFlush()  {
        //Setup
        BitLibrary bitLibrary = constructMockBitLibrary();
        boolean randomBoolean = randomBoolean();
        byte[] expected = new byte[1];
        expected[0] = (byte) (randomBoolean ? getOne() : 0);
        expected[0] = (byte) (expected[0] << 7); //Shift data all the way to the left, so we have one bit of data.

        try {
            //Test
            bitLibrary.writeBit(randomBoolean);

            //Verify -- should not have interacted with streams at this point
            verify(mockInputStream, never()).read();
            verify(mockOutputStream, never()).write(anyInt());
            verify(mockOutputStream, never()).write((byte[]) any());
            verify(mockOutputStream, never()).write((byte[]) any(), anyInt(), anyInt());

            //Test
            bitLibrary.flush();

            //Verify
            verify(mockInputStream, never()).read();
            verify(mockOutputStream).write(eq(expected));

        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }
    }

    /* Verify that readNBits() correctly reads several bits from the buffer, and that reading less than one byte when
     * there is data in the buffer doesn't calls the streams
     */
    public void testReadNBitsFromBufferOnly() {
        //Setup (Pretend there's 6 bits of data in our buffer, successively reading bits should get us the buffer)
        short numBytes = (short) ((Math.abs(randomShort())/2)+1); //Make sure it's reasonably small and greater than zero
        byte sampleData = randomByte();
        BitLibrary bitLibrary = constructMockBitLibrary();
        bitLibrary.setReadBuffer(sampleData);
        int bufferCounter = 6;
        bitLibrary.setReadBufferCounter(bufferCounter); //Means we are on the index 6 bit, which is the 7th bit of data (there are 6 bits of data in the buffer)
        byte fiveOnes = (byte) (((byte) getOne() << 7) + ((byte) getOne() << 6) + ((byte) getOne() << 5) + ((byte) getOne() << 4) + ((byte) getOne() << 3)); //Ones in the first five bits!

        try {
            //Test
            byte[] firstResult = bitLibrary.readNBits(1);

            //Verify
            assertEquals(bitLibrary.getReadBufferCounter(), bufferCounter-1);
            assertNotNull(firstResult);
            assertEquals(firstResult.length, 1);
            assertEquals((byte) ((sampleData >>> 7) << 7), firstResult[0]);    //Zero out all but the first bit of result

            //Test
            byte[] secondResult = bitLibrary.readNBits(5); //Read the rest of our buffer

            //Verify
            assertEquals(bitLibrary.getReadBufferCounter(), 0);
            assertNotNull(secondResult);
            assertEquals(secondResult.length, 1);
            assertEquals((byte) (((sampleData >>> 2) << 2) << 1), secondResult[0]); //Verify the first five bits are equal
            verify(mockInputStream, never()).read();
            verify(mockInputStream, never()).read((byte[]) any());   //Should never call the input/output streams

        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }
    }

    /*
     * Verify that readNBits works correctly when reading whole bytes at a time (no buffer interaction)
     */
    public void testReadNBitsInWholeBytesOnly() {
        //Setup
        File newFile = new File("temp.txt");
        if (newFile.exists()) { //Ensures that this is a new file we're looking at
            newFile.delete();
        }
        short numBytes = (short) ((Math.abs(randomShort())/2)+1); //Make sure it's reasonably small and greater than zero
        byte[] sampleData = new byte[numBytes];
        for(int curByte = 0; curByte<numBytes; curByte++){
            sampleData[curByte] = randomByte();
        }
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }

        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        BitLibrary bitLibrary = null;
        try {
            inputStream = spy(new FileInputStream(newFile));
            outputStream = spy(new FileOutputStream(newFile));

        } catch (FileNotFoundException e) {
            fail("Should not have thrown FileNotFoundException!");
        }

        try {
            bitLibrary = constructBitLibrary(inputStream, outputStream);
        } catch (IOException e) {
            fail("Threw an exception creating the BitLibrary!");
        }
        try {
            outputStream.write(sampleData);

            //Test
            byte[] result = bitLibrary.readNBits(8*numBytes);

            //Verify
            for(int curByte = 0; curByte<numBytes; curByte++){
                assertEquals(result[curByte], sampleData[curByte]);
            }
        } catch (IOException e) {
            fail("Should not have thrown an exception!");
        }
    }

    /*
     * Verify that readNBits() correctly reads several and a fraction of bytes (correctly transition between file & buffers),
     * and that this correclty calls the streams
     */
    public void testReadNBitsFromBufferAndStream() {

        //Setup
        File newFile = new File("temp.txt");
        if (newFile.exists()) { //Ensures that this is a new file we're looking at
            newFile.delete();
        }
        short numBytes = (short) ((Math.abs(randomShort())/2)+1); //Make sure it's reasonably small and greater than zero
        byte[] sampleData = new byte[numBytes];
        for(int curByte = 0; curByte<numBytes; curByte++){
            sampleData[curByte] = randomByte();
        }
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }

        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        BitLibrary bitLibrary = null;
         try{
            //Setup
            newFile.delete();
            newFile.createNewFile();
            inputStream = spy(new FileInputStream(newFile));
            outputStream = spy(new FileOutputStream(newFile));
            bitLibrary = constructBitLibrary(inputStream, outputStream);
            outputStream.write(sampleData);
            byte newBuffer = (byte) ((randomByte() >>> 5) << 5);
            bitLibrary.setReadBuffer(newBuffer);
            int bufferCounter = 3;
            bitLibrary.setReadBufferCounter(bufferCounter); //Let's pretend we have three additional bits sitting in the buffer

            //Test
            byte[] result = bitLibrary.readNBits(8*numBytes+bufferCounter); //Read everything we just wrote to the file, and the new buffer data

            //Build the expected result
            byte[] expectedResult = new byte[numBytes+1];
            int curByte = 0;
            expectedResult[curByte] = newBuffer;
            expectedResult[curByte] = (byte) (expectedResult[curByte] | (byte) (sampleData[curByte] >>> bufferCounter));
            while(curByte < sampleData.length-1){
                curByte++;
                expectedResult[curByte] = (byte) (sampleData[curByte-1] << bufferCounter);
                expectedResult[curByte] = (byte) (sampleData[curByte] | (sampleData[curByte] >>> bufferCounter));
            }

            //Verify
            assertNotNull(result);
            assertEquals(numBytes + 1, result.length);
            for (int i=0; i<numBytes-1; i++){
                assertEquals(expectedResult[i], result[i]); //Verify everything read from the stream is correct
            }

        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }
    }


    /**
     * Tests an actual usage case of the library, when we write two ints to a file and read them immediately back
     *
     * @throws IOException If there is an IOException thrown from the BitLibrary class
     */
    public void testWriteIntReadInt() throws IOException {

        //Setup
        File newFile = new File("temp.txt");
        if (newFile.exists()) { //Ensures that this is a new file we're looking at
            newFile.delete();
        }
        BitLibrary bitLibrary = new BitLibrary(null, newFile);
        int width = 800;
        int height = 600;

        //Write
        bitLibrary.writeInt(width);
        bitLibrary.writeInt(height);
        bitLibrary.close();


        //Read
        bitLibrary = new BitLibrary(newFile, null);
        assertEquals(width, bitLibrary.readInt());
        assertEquals(height, bitLibrary.readInt());
        bitLibrary.close();
    }
    

    /* Verify that writeNBits() correctly writes several bits to the buffer, and that writing less than one byte when
     * there is not data in the buffer initially doesn't calls the streams
     */
    public void testWriteNBitsToBufferOnly() {
        //Setup
        BitLibrary bitLibrary = constructMockBitLibrary();
        byte sampleData = randomByte();
        boolean randomBoolean = (sampleData >>> 7) != 0;

        while(!randomBoolean){
            sampleData = randomByte();
            randomBoolean = (sampleData >>> 7) != 0;
        }

        try {
            //Test
            bitLibrary.writeBit(randomBoolean); //Pass the first bit of data to be written

            //Verify
            byte expectedWriteBuffer = (byte) ((randomBoolean ? getOne() : 0) << 7);
            verify(mockInputStream, never()).read();
            verify(mockOutputStream, never()).write(anyInt());
            verify(mockOutputStream, never()).write((byte[]) any());
            verify(mockOutputStream, never()).write((byte[]) any(), anyInt(), anyInt());
            assertNotNull(bitLibrary.getWriteBuffer());
            assertEquals(bitLibrary.getWriteBufferCounter(), 1);
            assertEquals(bitLibrary.getWriteBuffer(), expectedWriteBuffer);

            //Test
            byte[] temp = new byte[1];
            temp[0] = (byte) (sampleData << 1);
            bitLibrary.writeNBits(temp, 6); //Pass the next 6 bits of our byte to be written

            //Verify -- should not have written to the streams without a flush()
            verify(mockInputStream, never()).read();
            verify(mockOutputStream, never()).write(anyInt());
            verify(mockOutputStream, never()).write((byte[]) any());
            verify(mockOutputStream, never()).write((byte[]) any(), anyInt(), anyInt());
            assertNotNull(bitLibrary.getWriteBuffer());
            assertEquals(bitLibrary.getWriteBufferCounter(), 7);
            byte sevenOnes = (byte) (((byte) getOne() << 7) + ((byte) getOne() << 6) + ((byte) getOne() << 5) + ((byte) getOne() << 4) + ((byte) getOne() << 3) + ((byte) getOne() << 2) + + ((byte) getOne() << 1)); //Ones in the first seven bits!
            assertEquals((sampleData & sevenOnes), (bitLibrary.getWriteBuffer() & sevenOnes));

        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }
    }

    /*
     * Verify that writeNBits() works correctly when writing in whole bytes (no buffer interaction)
     */
    public void testWriteNBitsInWholeBytesOnly() {
        //Setup
        File newFile = hardCreateTempFile();

        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(newFile);
            outputStream = spy(new FileOutputStream(newFile));

        } catch (FileNotFoundException e){

        }
        BitLibrary bitLibrary = null;
        try {
            bitLibrary = constructBitLibrary(inputStream, outputStream);
        } catch (IOException e) {
            fail("Threw an exception creating the BitLibrary!");
        }
        short numBytes = (short) ((Math.abs(randomShort())/2)+1); //Make sure it's reasonably small and greater than zero
        byte[] sampleData = new byte[numBytes];
        for(int index = 0; index<numBytes; index++){
            sampleData[index] = randomByte();
        }

        //Test
        try {
            bitLibrary.writeNBits(sampleData, (numBytes*8));
            byte[] result = new byte[numBytes];
            int bytesRead = inputStream.read(result);

            //Verify
            verify(outputStream, times(1)).write((byte[]) any());
            assertEquals(bytesRead, numBytes);
            for(int index = 0; index<numBytes; index++){
                assertEquals(sampleData[index], result[index]);
            }

        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }

    }

    /*
     * Verify that writeNBits() correctly writes several and a fraction of bytes (correctly transition between file & buffers),
     * and that this correctly calls the streams
     */
    public void testWriteNBitsToBufferAndStream() {

        //Setup
        File newFile = hardCreateTempFile();
        BitLibrary bitLibrary = null;
        newFile = hardCreateTempFile();
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(newFile);
            outputStream = spy(new FileOutputStream(newFile));

        } catch (FileNotFoundException e){

        }
        try {
            bitLibrary = constructBitLibrary(inputStream, outputStream);
        } catch (IOException e) {
            fail("Threw an exception creating the BitLibrary!");
        }
        short numBytes = (short) ((Math.abs(randomShort())/2)+1); //Make sure it's reasonably small and greater than zero
        byte[] sampleData = new byte[numBytes];
        for(int index = 0; index<numBytes; index++){
            sampleData[index] = randomByte();
        }
        byte fourOnes = (byte) (((byte) getOne() << 7) + ((byte) getOne() << 6) + ((byte) getOne() << 5) + ((byte) getOne() << 4)); //Ones in the first seven bits!
        sampleData[numBytes-1] = (byte) (sampleData[numBytes-1] & fourOnes); //Zero out all but the first four bits of the
                                                                        // last entry of sampleData

        try {
            //Test
            bitLibrary.setWriteBuffer((byte) getOne());
            int writeBufferCounter = 3;
            bitLibrary.setWriteBufferCounter(writeBufferCounter);
            bitLibrary.writeNBits(sampleData, numBytes*8 + 4);
            byte[] result = new byte[numBytes];
            int bytesRead = inputStream.read(result);

            //Verify
            verify(outputStream, times(1)).write((byte[]) any());
            assertNotNull(bitLibrary.getWriteBuffer());
            assertEquals(7, bitLibrary.getWriteBufferCounter());
            assertEquals(numBytes, bytesRead);
            byte[] expectedResult = new byte[numBytes];
            expectedResult[0] = (byte) getOne();                                        //buffer or'd with first sample data entry
            expectedResult[0] = (byte) (expectedResult[0] | (sampleData[0] >>> writeBufferCounter));
            for(int index = 1; index<numBytes; index++){
                expectedResult[index] = (byte) (sampleData[index-1] << (8-writeBufferCounter));
                expectedResult[index] = (byte) (expectedResult[index] | sampleData[index] >>> writeBufferCounter);
            }

            for(int index = 0; index<numBytes; index++){
                assertEquals(expectedResult[index], result[index]);
            }

        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }
    }

    //Tests the close function to make sure we actually tru to close the streams
    public void testClose() {
        BitLibrary library = constructSpiedBitLibrary();

        try {
            library.close();
        } catch (IOException e) {
            fail("Should not have thrown an exception!");
        }
    }

    //Method used to compensate for Endianness
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

    //Utility method used to create "temp.txt" in the current working directory, and deleting it if it already exists
    private File hardCreateTempFile() {
        File newFile = new File("temp.txt");
        if (newFile.exists()) { //Ensures that this is a new file we're looking at
            newFile.delete();
        }
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            fail("Should not have thrown IOException!");
        }
        return newFile;
    }

    //Utility method used to create "temp.txt" in the current working directory, if it does not already exist
    private File lazyCreateTempFile() {
        File newFile = new File("temp.txt");
        if (!newFile.exists()) {
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                fail("Should not have thrown IOException!");
            }
        }
        return newFile;
    }

    //Utility method for generating and returning a random byte
    private byte randomByte() {
        Random random = new Random();
        byte[] aByte = new byte[1];
        random.nextBytes(aByte);
        return aByte[0];
    }

    //Utility method for generating and returning a random boolean
    private boolean randomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    //Utility method for generating and returning a random int
    private int randomInt() {
        Random random = new Random();
        return random.nextInt();
    }

    //Utility method for generating and returning a random short
    private short randomShort() {
        Random random = new Random();
        return (short) random.nextInt();
    }

    //Utility method for generating and returning a random long
    private short randomLong() {
        Random random = new Random();
        return (short) random.nextLong();
    }
}
