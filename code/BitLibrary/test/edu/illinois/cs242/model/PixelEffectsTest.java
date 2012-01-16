package edu.illinois.cs242.model;

import edu.illinois.cs242.effects.SimpleEffects;
import junit.framework.TestCase;
/**
 *
 *               _______________________________________________
 *              |                                               |
 *              |  NOTE: This is not my code, but was taken     |
 *              |   from CS125, and modified by me heavily      |
 *              |_______________________________________________|
 *
 *
 * Tests SimpleEffects.
 * You do not need to modify this file, however it may be useful
 * to read how each test works.
 * @author angrave
 *
 */
public class PixelEffectsTest extends TestCase {

	/**
     *  Creates a new array of the reference test data.
	 */
	public int[][] getTestSourceArray() {
		return new int[][] { { 0, 1, 2, 3 }, { 10, 11, 12, 13 },
				{ 20, 21, 22, 23 } };
	}

	public void assertSourceCopiedAndUnchanged(int[][] expected,int[][] source,int [][]dest) {
		String ERR = "The source array cannot be modified!";
		
		assertEquals(ERR, expected.length,source.length);
		for (int i = 0; i < expected.length; i++) {
			assertNotNull(ERR, source[i]);
			assertEquals(ERR, expected[i].length,source[i].length);
			for(int x=0; dest!=null && x<dest.length;x++) {
				assertTrue("Source and target cannot share subarrays", source[i] != dest[x]);
			}	
			for (int j = 0; j < expected[i].length; j++)
				assertEquals(ERR, expected[i][j], source[i][j]);
		}
		
	}

    /**
     * Tests that the entries of a 2D array are equal
     *
     * @param expected The expected array
     * @param actual The actual array
     */
	public void assertEntriesEqual(int[][] expected, int[][] actual) {
		if (expected == actual)
			return;
		if (expected == null) {
			assertTrue(null == actual);
		} else
			assertNotNull(actual);
		assertEquals("Outer array dimensions are incorrect", expected.length,
				actual.length);
		for (int i = 0; i < expected.length; i++) {
			if (expected[i] == actual[i])
				continue;
			if (expected[i] == null) {
				assertTrue("arr[" + i + "] should be null", null == actual[i]);
			} else
				assertNotNull("arr[" + i + "] should not be null");

			assertEquals("arr[" + i + "] incorrect length", expected[i].length,
					actual[i].length);
			for (int j = 0; j < expected[i].length; j++)
				assertEquals("arr[" + i + "][" + j + "] incorrect value",
						expected[i][j], actual[i][j]);
		}
	}

    /**
     * Test that we correctly copy an image
     */
	public void testCopy() {
		int[][] source = getTestSourceArray();

		int[][] result = SimpleEffects.copy(source);
		assertNotNull(result);
		assertEntriesEqual(source, result); // check the individual elements
		assertEntriesEqual(source,getTestSourceArray());
		assertTrue("A new array should be returned", source != result);
		for (int i = 0; i < source.length; i++)
			assertTrue(
					"source and copy must not share the same sub-arrays (these need to be copied too)",
					source[i] != result[i]);

	}

    /**
     * Test that we can correctly resize an image to double its original dimensions
     */
	public void testResizeDoubleSize() {
		int[][] source = getTestSourceArray(); // 3 x 4 -> 1 x 2
		int w = 6, h = 8;
		int[][] expected = { { 0, 0, 1, 1, 2, 2, 3, 3 },
				{ 0, 0, 1, 1, 2, 2, 3, 3 }, { 10, 10, 11, 11, 12, 12, 13, 13 },
				{ 10, 10, 11, 11, 12, 12, 13, 13 },
				{ 20, 20, 21, 21, 22, 22, 23, 23 },
				{ 20, 20, 21, 21, 22, 22, 23, 23 } };
		int[][] actual = SimpleEffects.resize(source, w, h);
		assertEntriesEqual(expected, actual);
		assertSourceCopiedAndUnchanged(source,getTestSourceArray(),actual);
	}

    /**
     * Test that we correctly flip an image
     */
	public void testFlip() {
		int[][] source = getTestSourceArray();
		int[][] expected = { { 3, 2, 1, 0 }, { 13, 12, 11, 10 },
				{ 23, 22, 21, 20 } };
		int[][] actual = SimpleEffects.flip(source);
		assertEntriesEqual(expected, actual);
		assertSourceCopiedAndUnchanged(source,getTestSourceArray(),actual);
	}

    /**
     * Test that we mirror images correctly
     */
	public void testMirror() {
		int[][] source = getTestSourceArray();
		int[][] expected = { { 20, 21, 22, 23 }, { 10, 11, 12, 13 },
				{ 0, 1, 2, 3 } };
		int[][] actual = SimpleEffects.mirror(source);
		assertEntriesEqual(expected, actual);
		assertSourceCopiedAndUnchanged(source,getTestSourceArray(),actual);
	}

    /**
     * Test that we rotate images left correctly
     */
	public void testRotateLeft() {
		int[][] source = getTestSourceArray();
		int[][] expected = { { 20, 10, 0 }, { 21, 11, 1 }, { 22, 12, 2 },
				{ 23, 13, 3 } };
		int[][] actual = SimpleEffects.rotateLeft(source);
		assertEntriesEqual(expected, actual);
		assertSourceCopiedAndUnchanged(source,getTestSourceArray(),actual);
	}
}
