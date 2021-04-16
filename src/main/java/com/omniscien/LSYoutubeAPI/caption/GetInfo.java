package com.omniscien.LSYoutubeAPI.caption;

import java.io.IOException;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Caption;
import com.google.api.services.youtube.model.CaptionListResponse;
import com.google.api.services.youtube.model.CaptionSnippet;
import com.google.common.collect.Lists;
import com.omniscien.LSYoutubeAPI.util.Auth;
import com.omniscien.LSYoutubeAPI.util.Constant;
import com.omniscien.LSYoutubeAPI.util.ReadProp;

public class GetInfo {

	private ReadProp readprop = new ReadProp();
	
	 /**
     * Define a global instance of a YouTube object, which will be used to make
     * YouTube Data API requests.
     */
    private static YouTube youtube;
    
    private List<String> scopes = Lists.newArrayList(readprop.getProp(Constant.SCOPES_URL_CAPTION));

	
	public GetInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public String GetCaptionInfo(String ClientSecrets,String VideoIdStr) {
		//Variable
		Credential credential = null;
		CaptionListResponse captionListResponse = null;
		
		
		//Preapre input VideoIdStr
		if(VideoIdStr == null) {
			return "VideoId is null";
		}
		
		// Authorize the request.
        try {
			credential = Auth.authorize(scopes, "captions", ClientSecrets);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
     // This object is used to make YouTube Data API requests.
        youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
                .setApplicationName("youtube-cmdline-captions-sample").build();
		
		
        
		 // Call the YouTube Data API's captions.list method to
	      // retrieve video caption tracks.
	      try {
			captionListResponse = youtube.captions().
			      list("snippet", VideoIdStr).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	      List<Caption> captions = captionListResponse.getItems();
//	      CaptionSnippet snippet;
	      StringBuffer outputBuff = new StringBuffer();
	      if(captions != null) {	    	  
	    	  for(int i = 0; i <captions.size(); i++) {
	    		  Caption caption = captions.get(i);	    		  
					if (captions.size() == 1) {
						CaptionSnippet snippet = caption.getSnippet();
						outputBuff.append("[{\"Captionid\":\""+caption.getId()+"\",");
						outputBuff.append("\"snippetList\":");
						outputBuff.append(snippet);
						outputBuff.append("}]");
					}else if(captions.size() > 1) {
						
						
						if(i == 0) {
							outputBuff.append("[");
							CaptionSnippet snippet = caption.getSnippet();
							outputBuff.append("{\"Captionid\":\""+caption.getId()+"\",");
							outputBuff.append("\"snippetList\":");
							outputBuff.append(snippet);
							outputBuff.append("},");
						}else 
						if(i != captions.size()-1) {
							CaptionSnippet snippet = caption.getSnippet();
							outputBuff.append("{\"Captionid\":\""+caption.getId()+"\",");
							outputBuff.append("\"snippetList\":");
							outputBuff.append(snippet);
							outputBuff.append("},");
						}else {
							CaptionSnippet snippet = caption.getSnippet();
							outputBuff.append("{\"Captionid\":\""+caption.getId()+"\",");
							outputBuff.append("\"snippetList\":");
							outputBuff.append(snippet);
							outputBuff.append("}");
							outputBuff.append("]");
						}
						
					}
		      }
	      }else {
	    	  return "Captions value is null.";
	      }
	      
	      return outputBuff.toString();
	      
		
		
	}

}
