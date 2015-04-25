import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class youtube_dl
{
	private youtube_dl()
	{
		// static class
	}
	/**
     * Downloads a YouTube video
     * 
     * @param directory where to save videos to
     * @param url the video URL
     * @param mp3Format true if the video is to be converted to MP3; false otherwise
     * @param keepVideo whether or not to keep the original video after conversion
     */
	public static void download(String directory, String url, boolean mp3Format, boolean deleteAfterConversion) throws IOException
	{
		Thread t = new Thread(new Runnable() {
		    public void run() {
				List<String> cmd = new ArrayList<>();
				cmd.add("cmd");
				cmd.add("/c");
				String yt = "youtube-dl";
				yt += " --output \""+directory+"/%(title)s.%(ext)s\""+" --no-mtime";
				if(mp3Format) {
					yt += " --extract-audio --audio-format mp3 --audio-quality=320k";
					if(!deleteAfterConversion) yt += " --keep-video";
				}
				yt += " "+url;
				//System.out.println(yt);
				cmd.add(yt);
				ProcessBuilder builder = new ProcessBuilder(cmd);
		        builder.redirectErrorStream(true);
		        Process p = null;
				try {
					p = builder.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        String line = null;
		        while(true) {
		        	try {
						line = r.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	if(line == null) break;
		            System.out.println(line);
		        }
		    }
		});

		t.start();
	}
	
	public static void update() throws IOException
	{
		Thread t = new Thread(new Runnable() {
		    public void run() {
				List<String> cmd = new ArrayList<>();
				cmd.add("cmd");
				cmd.add("/c");
				String yt = "youtube-dl --update";
				cmd.add(yt);
				ProcessBuilder builder = new ProcessBuilder(cmd);
		        builder.redirectErrorStream(true);
		        Process p = null;
				try {
					p = builder.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        String line = null;
		        while(true) {
		        	try {
						line = r.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	if(line == null) break;
		            System.out.println(line);
		        }
		    }
		});
		t.start();
	}

}