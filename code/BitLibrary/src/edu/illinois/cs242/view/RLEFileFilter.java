package edu.illinois.cs242.view;

import edu.illinois.cs242.model.CompressionConstants;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Jon
 * Date: Sep 24, 2010
 *
 * This class is used as a file filter for RLE compressed images
 */
public class RLEFileFilter extends FileFilter {
  private final String[] acceptedExtensions =
    new String[] {CompressionConstants.RLE_FILE_SUFFIX.substring(1)};

  public boolean accept(File file)
  {
    for (String extension : acceptedExtensions)
    {
      if (file.getName().toLowerCase().endsWith(extension)  || file.isDirectory())
      {
        return true;
      }
    }
    return false;
  }

    @Override
    public String getDescription() {
        return LabelConstants.RLE_FILTER_MESSAGE;
    }
}
