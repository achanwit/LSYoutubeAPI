package com.omniscien.LSYoutubeAPI.caption;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Caption;
import com.google.api.services.youtube.model.CaptionSnippet;
import com.google.common.collect.Lists;
import com.omniscien.LSYoutubeAPI.util.Auth;
import com.omniscien.LSYoutubeAPI.util.Constant;
import com.omniscien.LSYoutubeAPI.util.ReadProp;
import com.omniscien.LSYoutubeAPI.util.Constant;
import com.omniscien.LSYoutubeAPI.model.Instant;

public class Update {
	
	public Update() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean UpdateCaption(final Instant instant, String captionId, String captionFilePath) {
		
		instant.getoLog().writeLog("Start Update caption id:"+captionId, false);
		// Variable
		boolean updateStatus = true;
		File captionUpdateFile = null;
		Credential credential = null;
		InputStreamContent mediaContent = null;
		Caption captionUpdateResponse = null;
		com.google.api.services.youtube.YouTube.Captions.Update captionUpdate = null;

		// Prepare input Caption Id
		if (captionId != null) {
			if (captionId.equals("")) {
				instant.getoLog().writeError("caption Id is empty.");
				return false;
			}
		} else {
			instant.getoLog().writeError("caption Id is null.");
			return false;
		}

		// Prepare input caption file path
		if (captionFilePath == null) {
			instant.getoLog().writeError("Caption File Path is null.");
			return false;
		} else {
			if (captionFilePath.equals("")) {
				instant.getoLog().writeError("Caption File Path is empty.");
				return false;
			} else {
				captionUpdateFile = new File(captionFilePath);
			}
		}


		// Modify caption's isDraft property to unpublish a caption track.
		CaptionSnippet updateCaptionSnippet = new CaptionSnippet();
		updateCaptionSnippet.setIsDraft(false);
		Caption updateCaption = new Caption();
		updateCaption.setId(captionId);
		updateCaption.setSnippet(updateCaptionSnippet);

		if (captionUpdateFile == null) {
			// Call the YouTube Data API's captions.update method to update an existing
			// caption track.
			try {
				captionUpdateResponse = instant.getYoutube().captions().update("snippet", updateCaption).execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				instant.getoLog().writeError(e.toString());
				e.printStackTrace();
			}

		} else {
			// Create an object that contains the caption file's contents.

			try {
				mediaContent = new InputStreamContent(Constant.CAPTION_FILE_FORMAT,
						new BufferedInputStream(new FileInputStream(captionUpdateFile)));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				instant.getoLog().writeError(e.toString());
				e.printStackTrace();
			}
			mediaContent.setLength(captionUpdateFile.length());

			// Create an API request that specifies that the mediaContent
			// object is the caption of the specified video.
			try {
				captionUpdate = instant.getYoutube().captions()
						.update("snippet", updateCaption, mediaContent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				instant.getoLog().writeError(e.toString());
				e.printStackTrace();
			}
			
			 // Set the upload type and add an event listener.
	        MediaHttpUploader uploader = captionUpdate.getMediaHttpUploader();
	        
	     // Indicate whether direct media upload is enabled. A value of
	        // "True" indicates that direct media upload is enabled and that
	        // the entire media content will be uploaded in a single request.
	        // A value of "False," which is the default, indicates that the
	        // request will use the resumable media upload protocol, which
	        // supports the ability to resume an upload operation after a
	        // network interruption or other transmission failure, saving
	        // time and bandwidth in the event of network failures.
	        uploader.setDirectUploadEnabled(false);
	        
	     // Set the upload state for the caption track file.
	        MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
	            @Override
	            public void progressChanged(MediaHttpUploader uploader) throws IOException {
	                switch (uploader.getUploadState()) {
	                    // This value is set before the initiation request is
	                    // sent.
	                    case INITIATION_STARTED:
	                    	instant.getoLog().writeLog("Initiation Started", false);
	                        System.out.println("Initiation Started");
	                        break;
	                    // This value is set after the initiation request
	                    //  completes.
	                    case INITIATION_COMPLETE:
	                    	instant.getoLog().writeLog("Initiation Completed", false);
	                        System.out.println("Initiation Completed");
	                        break;
	                    // This value is set after a media file chunk is
	                    // uploaded.
	                    case MEDIA_IN_PROGRESS:
	                    	instant.getoLog().writeLog("Upload in progress", false);
	                        System.out.println("Upload in progress");
	                        instant.getoLog().writeLog("Upload percentage: " + uploader.getProgress(), false);
	                        System.out.println("Upload percentage: " + uploader.getProgress());
	                        break;
	                    // This value is set after the entire media file has
	                    //  been successfully uploaded.
	                    case MEDIA_COMPLETE:
	                    	instant.getoLog().writeLog("Upload Completed!", false);
	                        System.out.println("Upload Completed!");
	                        break;
	                    // This value indicates that the upload process has
	                    //  not started yet.
	                    case NOT_STARTED:
	                    	instant.getoLog().writeLog("Upload Not Started!", false);
	                        System.out.println("Upload Not Started!");
	                        break;
	                }
	            }
	        };
	        uploader.setProgressListener(progressListener);
	     // Upload the caption track.
	        try {
				captionUpdateResponse = captionUpdate.execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				instant.getoLog().writeError(e.toString());
				e.printStackTrace();
			}
	        instant.getoLog().writeLog("\n================== Uploaded New Caption Track ==================\n", false);
	        System.out.println("\n================== Uploaded New Caption Track ==================\n");
		}
		 // Print information from the API response.
		instant.getoLog().writeLog("\n================== Updated Caption Track ==================\n", false);
	      System.out.println("\n================== Updated Caption Track ==================\n");
	      CaptionSnippet snippet = captionUpdateResponse.getSnippet();
	      
	      instant.getoLog().writeLog("Snippet: "+snippet, false);
	      System.out.println("Snippet: "+snippet);
	      
	      instant.getoLog().writeLog("  - ID: " + captionUpdateResponse.getId(), false);
	      System.out.println("  - ID: " + captionUpdateResponse.getId());
	      
	      instant.getoLog().writeLog("  - Name: " + snippet.getName(), false);
	      System.out.println("  - Name: " + snippet.getName());
	      
	      instant.getoLog().writeLog("  - Language: " + snippet.getLanguage(), false);
	      System.out.println("  - Language: " + snippet.getLanguage());
	      
	      instant.getoLog().writeLog("  - Draft Status: " + snippet.getIsDraft(), false);
	      System.out.println("  - Draft Status: " + snippet.getIsDraft());
	      
	      instant.getoLog().writeLog("\n-------------------------------------------------------------\n", false);
	      System.out.println("\n-------------------------------------------------------------\n");
	      
	      
	      return updateStatus;
	}

}
