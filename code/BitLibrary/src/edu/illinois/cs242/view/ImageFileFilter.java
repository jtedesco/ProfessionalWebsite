package edu.illinois.cs242.view;

import edu.illinois.cs242.model.CompressionConstants;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Jon
 * Date: Sep 24, 2010
 *
 * This class is used as a file filter for BMP images
 */
public class ImageFileFilter extends FileFilter {
  private final String[] acceptedExtensions =
    new String[] {"jpg", "bmp", "jpeg", "png"};

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
        return LabelConstants.IMAGE_FILTER_MESSAGE;
    }
}
