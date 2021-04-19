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

public class Delete {
	
	
	public Delete() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean DeleteCaption(final Instant instant, String CaptionId) {
		
		instant.getoLog().writeLog("Start Delete caption Id:"+CaptionId, false);
		// Variable
		boolean DeleteStatus = true;

		if (CaptionId == null) {
			instant.getoLog().writeError("Caption Id is null.");
			return false;
		}

		// Call the YouTube Data API's captions.delete method to
		// delete an existing caption track.
		try {
			YouTube.Captions.Delete request = instant.getYoutube().captions().delete(CaptionId);
			request.execute();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			instant.getoLog().writeError(e.toString());
			e.printStackTrace();
		}
		instant.getoLog().writeLog("Finished delete caption Id:"+CaptionId, false);
		System.out.println("  -  Deleted caption: " + CaptionId);
		
		return DeleteStatus;
	}

}
