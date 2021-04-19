package com.omniscien.LSYoutubeAPI.caption;

import java.io.IOException;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.youtube.YouTube;
import com.google.common.collect.Lists;
import com.omniscien.LSYoutubeAPI.util.Auth;
import com.omniscien.LSYoutubeAPI.util.Constant;
import com.omniscien.LSYoutubeAPI.util.ReadProp;
import com.omniscien.LSYoutubeAPI.model.Instant;
import com.omniscien.LSYoutubeAPI.model.ServletContextMock;
import com.omniscien.LSYoutubeAPI.util.Log4J;

public class Init {

	private ReadProp readprop = new ReadProp();
	
	private com.omniscien.LSYoutubeAPI.util.Log4J oLog = null;
	
	 /**
     * Define a global instance of a YouTube object, which will be used to make
     * YouTube Data API requests.
     */
    
    private List<String> scopes = Lists.newArrayList(readprop.getProp(Constant.SCOPES_URL_CAPTION));
	
	public Init() {
		// TODO Auto-generated constructor stub
	}
	
	public Instant initial(String ClientSecrets) {
		
		Instant instant = new Instant();
		
		ServletContextMock app = new ServletContextMock();
		
		if (oLog == null) {
			try {
				oLog = new Log4J(app);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			oLog.debugMode = true;
			oLog.setDebugPath(readprop.getProp(Constant.LOG_PATH));
			oLog.log4JPropertyFile = readprop.getProp(Constant.LOG_4J);
		}
		
		//Variable 
		/**
	     * Define a global instance of a YouTube object, which will be used to make
	     * YouTube Data API requests.
	     */
		YouTube youtube;
		Credential credential = null;

		 // Authorize the request.
       try {
			credential = Auth.authorize(scopes, "captions", ClientSecrets);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			oLog.writeError(e1.toString());
			e1.printStackTrace();
		}
       
       // This object is used to make YouTube Data API requests.
       youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
               .setApplicationName("youtube-API-captions-mangement").build();
       
       instant.setYoutube(youtube);
       instant.setoLog(oLog);
       
       oLog.writeLog("Success for initial of ClientSecrets: "+ClientSecrets, false);
       
       
       return instant;
		
	}

}
