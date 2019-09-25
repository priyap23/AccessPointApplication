import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReadAccessPoints {

	private static Map<String, JSONObject> mapOriginalFileAPObjects = new HashMap();
	
	 //JSON parser object to parse read file
	private static JSONParser jsonParser = new JSONParser();
	
	@SuppressWarnings("unchecked")
    public static void originalFileJSONObject(String strOriginatFilePath)
    {
              
        try (FileReader reader = new FileReader(strOriginatFilePath))
        {
            //Read JSON file
        	JSONObject obj = (JSONObject) jsonParser.parse(reader);
        	JSONArray aplist = (JSONArray) obj.get("access_points");
        	for (Object ap : aplist) {
        		JSONObject apJson = (JSONObject) ap;
        		mapOriginalFileAPObjects.put((String) apJson.get("ssid"), apJson);
			}
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
			e.printStackTrace();
		}
    }
 
    public static String modifiedFileJSONObject(String strFilePath)
    {
    	Map<String, JSONObject> mapAPObjects = new HashMap();
    	StringBuilder sbMessage = new StringBuilder();
    	try (FileReader reader = new FileReader(strFilePath))
        {
            //Read JSON file
        	JSONObject obj = (JSONObject) jsonParser.parse(reader);
        //	System.out.println(obj.get("access_points"));
        	JSONArray aplist = (JSONArray) obj.get("access_points");
        	
			for (Object aps : aplist) {
        		JSONObject apJson = (JSONObject) aps;
        		mapAPObjects.put((String) apJson.get("ssid"), apJson);
			}
        	
        	//Compare the modidied contents
        	String strSSID;
        	Set setKeys = mapAPObjects.keySet();
			       	
        	JSONObject modifiedJSONAP;
        	JSONObject originalJSONAP;
        	for (Object key : setKeys) {
        		strSSID = (String) key;
        		modifiedJSONAP = mapAPObjects.get(key);
        		if(mapOriginalFileAPObjects.containsKey(key)) {
        			//Check modification
        			originalJSONAP = mapOriginalFileAPObjects.get(key);
        			if(!(originalJSONAP.get("snr")).equals(modifiedJSONAP.get("snr"))) {
        				sbMessage.append(key);
            			sbMessage.append("'s  SNR  has changed from ");
            			sbMessage.append(originalJSONAP.get("snr").toString());
            			sbMessage.append(" to ");
            			sbMessage.append(modifiedJSONAP.get("snr").toString());
            			sbMessage.append("\n");
        			}
        			if(!(originalJSONAP.get("channel")).equals(modifiedJSONAP.get("channel"))) {
        				sbMessage.append(key);
            			sbMessage.append("'s  channel  has changed from ");
            			sbMessage.append(originalJSONAP.get("channel").toString());
            			sbMessage.append(" to ");
            			sbMessage.append(modifiedJSONAP.get("channel").toString());
            			sbMessage.append("\n");
        			}
        			mapOriginalFileAPObjects.put((String) key, modifiedJSONAP); // add it to the originalFileObject for next changes comparison 
        		} else {
        			//total new entry to the file
        			mapOriginalFileAPObjects.put((String) key, modifiedJSONAP);	// add it to the originalFileObject for next changes comparison 
        			// send message
        			sbMessage.append(key);
        			sbMessage.append(" is added to the list with SNR ");
        			sbMessage.append(modifiedJSONAP.get("snr"));
        			sbMessage.append(" and channel ");
        			sbMessage.append(modifiedJSONAP.get("channel"));
        			sbMessage.append("\n");
        		}
			}
        	
        	//to check removed SSID
        	Set hmoriginalKey = mapOriginalFileAPObjects.keySet();
        	for (Object orgKey : hmoriginalKey) {
				if(!mapAPObjects.containsKey(orgKey)) {
					sbMessage.append(orgKey); 
					sbMessage.append(" is removed from the list");
					sbMessage.append("\n");
				}
			}
        	
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
			e.printStackTrace();
		}
		return sbMessage.toString();
    }
}
