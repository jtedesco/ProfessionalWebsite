package edu.illinois.cs242.model;

import junit.framework.TestCase;

/**
 *               _______________________________________________
 *              |                                               |
 *              |  NOTE: This is not my code, but was taken     |
 *              |   from CS125, and modified by me heavily      |
 *              |_______________________________________________|
 *
 *
 * Tests RGBUtilities.
 * @author angrave
 *
 */
public class RGBUtilitiesTest extends TestCase {

    /**
     * Test translating from color data to rgb data
     */
	public void testToComponents() {
		assertEquals( 0x33, RGBUtilities.getRed(0x44332211));
		assertEquals( 0x22, RGBUtilities.getGreen(0x44332211));
		assertEquals( 0x11, RGBUtilities.getBlue(0x44332211));
	}

    /**
     * Test translating from rgb data to color data
     */
	public void testToRGB() {
		assertEquals( 0x112233, RGBUtilities.getRGB(0x11, 0x22, 0x33));
	}
}
