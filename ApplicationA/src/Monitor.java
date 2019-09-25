import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import com.monitor.controller.MonitorAndSendUpdate;

public class Monitor {

	public static void main(String[] args) {
		try 
		{
			String path = args[0];
			String strFileName = args[1];
			String host = args[2];
			int port = Integer.parseInt(args[3]);
			System.out.println("************* APPLICATION A ************");
			ReadAccessPoints.originalFileJSONObject(path+File.separator+strFileName);
			MonitorAndSendUpdate obj = new MonitorAndSendUpdate(host, port);
			// Creates a instance of WatchService.
			WatchService watcher = FileSystems.getDefault().newWatchService();
			
			// Registers the logDir below with a watch service.
			Path logDir = Paths.get(path);
			logDir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
			
			// Monitor the logDir at listen for change notification.
			while (true) {
			    WatchKey key = watcher.take();
			    Thread.sleep(1000);
			    for (WatchEvent<?> event : key.pollEvents()) {
			        WatchEvent.Kind<?> kind = event.kind();
					if (kind == ENTRY_MODIFY) {
			        	if(strFileName.equalsIgnoreCase( event.context().toString())) {
			        		String strMessage = ReadAccessPoints.modifiedFileJSONObject(path+File.separator+strFileName);
			        		MonitorAndSendUpdate.monitorAndSendUpdate(strMessage);
			        	}
			        	key.reset();
					} 
			    }
			   
			}
		} catch (IOException e) {
	        e.printStackTrace();
	    }catch(InterruptedException e) {
	    	e.printStackTrace();
	    }
	}
}
