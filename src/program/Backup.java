package program;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import api.YAMLFile;

public class Backup implements ActionListener, FileFilter{
	
	public static Gui frame;
	public static YAMLFile config = new YAMLFile(true);
	public static File configFile;
	File destinationFolder;
	ArrayList<File> sourceFolders = new ArrayList<>();
	
	int totalFiles = 1, doneFiles = 1;
	String curFileName = "";
	
	public Backup() {
		frame = new Gui(this);
		configFile = new File((new File("").getAbsolutePath().subSequence(0, 1)) + ":\\beheer\\backup2.0\\config.yml");
		if(!configFile.exists()){
			config.addString("Destination", "");
			for(int i = 1 ; i<10 ; i++)
				config.addString("Source."+i, "");
			try {
				configFile.getParentFile().mkdirs();
				configFile.createNewFile();
				config.Save(configFile);
			} catch (Exception e) {
				e.printStackTrace();
				quitMessage("Something went wrong: " + System.lineSeparator() + e.getStackTrace());
			}
		}
		config.Load(configFile);
		if(config.getString("Destination") == null || config.getString("Destination").equals(""))
			quitMessage("The config file is filled in yet. Please do so.");
		destinationFolder = new File(config.getString("Destination") + "\\"+new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()));
		for(int i = 1 ; i<10 ; i++)
			if(config.getString("Source."+i) != null && !config.getString("Source."+i).equals("")){
				File temp = new File(config.getString("Source."+i));
				sourceFolders.add(temp);
				frame.addCheckBox(temp.getName());
			}
		frame.pack();
	}
	
	@Override
	public void actionPerformed(ActionEvent action) {
		frame.setStatus("Starting Backup...");
		frame.button.setEnabled(false);
		final Backup thiss = this;
		Thread copeyer = new Thread(new Runnable() {
			@Override
			public void run() {
				for(File sourceFolder : sourceFolders)
					if(frame.isCheckboxSelected(sourceFolder.getName()))
						totalFiles += getFileAmount(sourceFolder);
				System.out.println("total file: " + totalFiles);
				for(File sourceFolder : sourceFolders){
					if(frame.isCheckboxSelected(sourceFolder.getName())){
						try {
							FileUtils.copyDirectory(sourceFolder, new File(destinationFolder.getAbsolutePath() + "\\" + sourceFolder.getName()), thiss);
						} catch (IOException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(frame, "Something went wrong while coppying: " + System.lineSeparator() + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				frame.setStatus("Backup Done");
				if(JOptionPane.showConfirmDialog(frame, "Backup Done! "+System.lineSeparator()+"Do you want to open the destiantion file?", "Done!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
					try {
						Runtime.getRuntime().exec("explorer "+destinationFolder.getAbsolutePath());
					} catch (IOException e) {
						e.printStackTrace();
					}
				System.exit(0);
			}
		});
		copeyer.start();
	}
	
	public static int getFileAmount(File folder){
		int out = 0;
		File[] listOfFiles = folder.listFiles();
		if(listOfFiles == null)
			return 0;
		
		for (int i = 0; i < listOfFiles.length; i++){
			System.out.println("regel 97: " + listOfFiles[i].getAbsolutePath());
			if (listOfFiles[i].isFile())
				out++;
			else if(listOfFiles[i].isDirectory())
				out += getFileAmount(listOfFiles[i]);
		}
		return out;
	}
	
	public void quitMessage(String err){
		JOptionPane.showMessageDialog(frame, err, "Something went wrong", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
	
	public static void main(String[] args){
		new Backup();
	}
	
	public String getSpace(int i){
		String out = "";
		for(int j = 0 ; j<i ; j++)
			out += " ";
		return out;
	}

	@Override
	public boolean accept(File pathname) {
		if(!pathname.isFile())
			return true;
		doneFiles++;
		frame.setStatus("Copying Directory: " + pathname.getParentFile().getAbsolutePath() + System.lineSeparator() +
				"Copying File:      " + pathname.getName() + System.lineSeparator() +
				"Progress:          " + doneFiles + "/" + totalFiles + " files done");
		frame.getProgressBar().setValue((int)((doneFiles/(float)totalFiles)*1000));
		frame.revalidate();
		return true;
	}

}
