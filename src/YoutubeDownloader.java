import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;

import net.miginfocom.swing.MigLayout;

public class YoutubeDownloader{

	private JFrame frame;
	private static JTextField urlTextField;
	private JLabel directoryLabel, urlLabel;
	private JButton downloadButton, saveButton;
	private JCheckBox mp3CheckBox, deleteCheckBox;
	private JTextArea outputTextArea;
	private Color backgroundColor = new Color(241,241,241);

	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try{
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} 
					catch(Exception e) {
						System.out.println(e);
					}
					YoutubeDownloader window = new YoutubeDownloader();
					window.frame.setLocationRelativeTo(null);
					window.frame.setVisible(true);
					window.frame.addWindowFocusListener(new WindowAdapter() {
					      public void windowGainedFocus(WindowEvent e) {
					    	  try {
					  			String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
					  			if(data.toLowerCase().indexOf("youtube") > -1) urlTextField.setText(data);
					  		} catch (Exception exc) {
					  		} 
					      }
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public YoutubeDownloader() {
		initialize();
		try {
			youtube_dl.update();
		} 
		catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		ArrayList<Image> imageList = new ArrayList<Image>();
	    imageList.add(Toolkit.getDefaultToolkit().getImage(YoutubeDownloader.class.getResource("/images/16.png")));
	    imageList.add(Toolkit.getDefaultToolkit().getImage(YoutubeDownloader.class.getResource("/images/32.png")));
	    imageList.add(Toolkit.getDefaultToolkit().getImage(YoutubeDownloader.class.getResource("/images/64.png")));
	    imageList.add(Toolkit.getDefaultToolkit().getImage(YoutubeDownloader.class.getResource("/images/128.png")));
	    imageList.add(Toolkit.getDefaultToolkit().getImage(YoutubeDownloader.class.getResource("/images/256.png")));
		frame.setIconImages(imageList);
		frame.setResizable(false);
		frame.setBackground(backgroundColor);
		frame.setTitle("Youtube Downloader");
		frame.getContentPane().setBackground(backgroundColor);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][][][][grow]"));
		
		urlLabel = new JLabel("Enter the URL of the video you want to download (e.g. http://www.youtube.com...)");
		urlLabel.setBackground(backgroundColor);
		frame.getContentPane().add(urlLabel, "cell 0 0");
		
		urlTextField = new JTextField();
		frame.getContentPane().add(urlTextField, "flowx,cell 0 1,growx");
		urlTextField.setColumns(10);
		
		downloadButton = new JButton("Download");
		downloadButton.setHorizontalAlignment(SwingConstants.TRAILING);
		downloadButton.setBackground(backgroundColor);
		downloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(urlTextField.getText().trim().length() > 0)
					{
						youtube_dl.download(directoryLabel.getText(),urlTextField.getText().trim(),mp3CheckBox.isSelected(),deleteCheckBox.isSelected());
						urlTextField.setText("");
					}
				} 
				catch (IOException e) {
					System.out.println("failure");
				}
			}
		});
		frame.getContentPane().add(downloadButton, "cell 0 1");
		
		saveButton = new JButton("Save to");
		saveButton.setHorizontalAlignment(SwingConstants.LEADING);
		saveButton.setBackground(backgroundColor);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
		        chooser.setCurrentDirectory(new File(directoryLabel.getText()));
		        chooser.setDialogTitle("Choose download directory");
		        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        chooser.setAcceptAllFileFilterUsed(false);
		        if (chooser.showOpenDialog(new JPanel()) == JFileChooser.APPROVE_OPTION) { 
		        	directoryLabel.setText(chooser.getSelectedFile().toString());
		        	Settings.setDirectory(chooser.getSelectedFile());
		        }
			}
		});
		frame.getContentPane().add(saveButton, "flowx,cell 0 2");
		directoryLabel = new JLabel(Settings.getDirectory().toString());
		directoryLabel.setBackground(new Color(255, 255, 255));
		frame.getContentPane().add(directoryLabel, "cell 0 2");
		
		mp3CheckBox = new JCheckBox("Automatically convert to MP3");
		mp3CheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
                Settings.setConvertToMP3(mp3CheckBox.isSelected());
			}
		});
		mp3CheckBox.setBackground(backgroundColor);
		mp3CheckBox.setSelected(Settings.getConvertToMp3());
		frame.getContentPane().add(mp3CheckBox, "flowx,cell 0 3");
		
		deleteCheckBox = new JCheckBox("Delete original file after conversion");
		deleteCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Settings.setDeleteAfterConversion(deleteCheckBox.isSelected());
			}
		});
		deleteCheckBox.setSelected(Settings.getDeleteAfterConversion());
		deleteCheckBox.setBackground(backgroundColor);
		frame.getContentPane().add(deleteCheckBox, "cell 0 3");
		
		outputTextArea = new JTextArea();
		outputTextArea.setWrapStyleWord(true);
		outputTextArea.setBackground(Color.WHITE);
		outputTextArea.setEditable(false);
		outputTextArea.setLineWrap(true);
		DefaultCaret caret = (DefaultCaret) outputTextArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		MessageConsole mc = new MessageConsole(outputTextArea);
		mc.redirectOut(null, System.out);
		mc.redirectErr(Color.RED, null);
		mc.setMessageLines(100);
		frame.getContentPane().add(new JScrollPane(outputTextArea), "cell 0 4,grow");
	}
	
}