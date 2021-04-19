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
import com.omniscien.LSYoutubeAPI.model.Instant;

public class GetInfo {

	public GetInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public String GetCaptionInfo(final Instant instant,String VideoIdStr) {
		
		instant.getoLog().writeLog("Start get caption info of Video Id:"+VideoIdStr, false);
		
		//Variable
		Credential credential = null;
		CaptionListResponse captionListResponse = null;
		
		
		//Preapre input VideoIdStr
		if(VideoIdStr == null) {
			instant.getoLog().writeError("Video Id is null.");
			return "VideoId is null";
		}
		
        
		 // Call the YouTube Data API's captions.list method to
	      // retrieve video caption tracks.
	      try {
			captionListResponse = instant.getYoutube().captions().
			      list("snippet", VideoIdStr).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			instant.getoLog().writeError(e.toString());
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
	      String output = outputBuff.toString();
	      instant.getoLog().writeLog("Finished get caption info of Video Id:"+VideoIdStr, false);
	      instant.getoLog().writeLog("Caption info result:"+output, false);
	      return output;
	      
		
		
	}

}
