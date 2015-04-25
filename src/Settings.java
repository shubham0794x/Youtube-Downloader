import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Settings
{
	private Settings() 
	{
		// static class
	}
	
	private static Properties settings;
    private static final String DIRECTORY = "directory";
    private static final String CONVERT_TO_MP3 = "mp3";
    private static final String DELETE_AFTER_CONVERSION = "delete";
    private static final File SETTINGS_FILE = new File("yt-dl.properties");
    
    static
    {
        if (!SETTINGS_FILE.exists()) create();
        else open();
    }
    
    private static void create() {
        settings = new Properties();
        StringBuilder downloadDirectory = new StringBuilder();
        downloadDirectory.append(System.getProperty("user.home"));
        downloadDirectory.append(File.separator);
        downloadDirectory.append("Downloads");
        File downloadFile = new File(downloadDirectory.toString());
        if (!downloadFile.isDirectory()) downloadFile.mkdirs();
        settings.put(DIRECTORY, downloadFile.getAbsolutePath());
        settings.put(CONVERT_TO_MP3, Boolean.toString(true));
        settings.put(DELETE_AFTER_CONVERSION, Boolean.toString(true));
        save();
    }
    
    private static void open() {
        settings = new Properties();
        try {
            settings.load(new FileReader(SETTINGS_FILE));
            File downloadLocation = new File((String)settings.get(DIRECTORY));
            if (!downloadLocation.isDirectory()) {
            	create();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static void save() {
        try 
        {
            settings.store(new FileWriter(SETTINGS_FILE), null);
        } 
        catch (IOException e) 
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Sets the directory
     * @param directory
     */
    public static void setDirectory(File directory) {
        settings.put(DIRECTORY, directory.getAbsolutePath());
        save();
    }
    
    /**
     * Sets convert to MP3
     * @param toMP3 
     */
    public static void setConvertToMP3(boolean mp3) {
        settings.put(CONVERT_TO_MP3, Boolean.toString(mp3));
        save();
    }
    
    /**
     * Sets delete video after conversion
     * @param delete 
     */
    public static void setDeleteAfterConversion(boolean delete) {
        settings.put(DELETE_AFTER_CONVERSION, Boolean.toString(delete));
        save();
    }
    
    /**
     * Gets the downloads directory
     * @return the downloads directory
     */
    public static File getDirectory() {
        return new File((String) settings.get(DIRECTORY));
    }
    
    /**
     * Is convert to MP3 selected?
     * @return whether or not convert to MP3 is selected
     */
    public static boolean getConvertToMp3() {
        return Boolean.parseBoolean((String) settings.get(CONVERT_TO_MP3));
    }
    
    /**
     * Is delete after conversion selected?
     * @return whether or not delete after conversion is selected
     */
    public static boolean getDeleteAfterConversion() {
        return Boolean.parseBoolean((String) settings.get(DELETE_AFTER_CONVERSION));
    }
    
    
}