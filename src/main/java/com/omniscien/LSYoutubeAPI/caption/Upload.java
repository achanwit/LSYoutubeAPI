package com.omniscien.LSYoutubeAPI.caption;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.InputStreamContent;
import com.omniscien.LSYoutubeAPI.util.Auth;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Captions.Download;
import com.google.api.services.youtube.YouTube.Captions.Insert;
import com.google.api.services.youtube.YouTube.Captions.Update;
import com.google.api.services.youtube.model.Caption;
import com.google.api.services.youtube.model.CaptionListResponse;
import com.google.api.services.youtube.model.CaptionSnippet;
import com.google.common.collect.Lists;

import com.omniscien.LSYoutubeAPI.util.Constant;
import com.omniscien.LSYoutubeAPI.util.ReadProp;

public class Upload {
	
	private ReadProp readprop = new ReadProp();
	
    
    
    /**
     * Define a global instance of a YouTube object, which will be used to make
     * YouTube Data API requests.
     */
    private static YouTube youtube;
    
    
    
   
    
    private List<String> scopes = Lists.newArrayList(readprop.getProp(Constant.SCOPES_URL_CAPTION));

	public Upload() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean UploadCaption(String ClientSecrets, String VideoIdStr, String Languageid, String CaptionName, String CaptionFilePath) {
		//Variable
		boolean uploadStatus = true;
		File captionInputFile = null;
		Insert captionInsert = null;
		Credential credential = null;
		
		
		//Prepare input VideoIdStr
		if(VideoIdStr != null) {
			if(VideoIdStr.equals("")) {
				return false;
			}
		}else {
			return false; 
		}
		
		//Prepare input Languageid 
		if(Languageid != null) {
			if(!Languageid.equals("")) {
				Languageid = Languageid.toLowerCase();
				Languageid = Languageid.substring(0, 2);
			}else {
				return false; 
			}
		}else {
			return false; 
		}
		
		//Prepare Caption name
		if(CaptionName ==null) {
			return false; 
		}
		
		//Prepare Caption InputFile 
		if(CaptionFilePath != null) {
			if(!CaptionFilePath.equals("")) {
				captionInputFile = new File(CaptionFilePath);
			}else {
				return false; 
			}			
		}else {
			return false; 
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
		
		
		
		 // Add extra information to the caption before uploading.
	      Caption captionObjectDefiningMetadata = new Caption();
		
	   // Most of the caption's metadata is set on the CaptionSnippet object.
	      CaptionSnippet snippet = new CaptionSnippet();

	      // Set the video, language, name and draft status of the caption.
	      snippet.setVideoId(VideoIdStr);
	      snippet.setLanguage(Languageid);
	      snippet.setName(CaptionName);
	      snippet.setIsDraft(false);
	      
	   // Add the completed snippet object to the caption resource.
	      captionObjectDefiningMetadata.setSnippet(snippet);
	      
	   // Create an object that contains the caption file's contents.
	      InputStreamContent mediaContent = null;
		try {
			mediaContent = new InputStreamContent(
			          Constant.CAPTION_FILE_FORMAT, 
			          new BufferedInputStream(new FileInputStream(captionInputFile)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	      mediaContent.setLength(captionInputFile.length());
	      
	   // Create an API request that specifies that the mediaContent
	      // object is the caption of the specified video.
	      try {
			captionInsert = youtube.captions().insert("snippet", captionObjectDefiningMetadata, mediaContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	   // Set the upload type and add an event listener.
	      MediaHttpUploader uploader = captionInsert.getMediaHttpUploader();
	      
	   // Indicate whether direct media upload is enabled. A value of
	      // "True" indicates that direct media upload is enabled and that
	      // the entire media content will be uploaded in a single request.
	      // A value of "False," which is the default, indicates that the
	      // request will use the resumable media upload protocol, which
	      // supports the ability to resume an upload operation after a
	      // network interruption or other transmission failure, saving
	      // time and bandwidth in the event of network failures.
	      uploader.setDirectUploadEnabled(true);
	      
	      // Set the upload state for the caption track file.
	      MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
	          @Override
	          public void progressChanged(MediaHttpUploader uploader) throws IOException {
	              switch (uploader.getUploadState()) {
	                  // This value is set before the initiation request is
	                  // sent.
	                  case INITIATION_STARTED:
	                      System.out.println("Initiation Started");
	                      break;
	                  // This value is set after the initiation request
	                  //  completes.
	                  case INITIATION_COMPLETE:
	                      System.out.println("Initiation Completed");
	                      break;
	                  // This value is set after a media file chunk is
	                  // uploaded.
	                  case MEDIA_IN_PROGRESS:
	                      System.out.println("Upload in progress");
	                      System.out.println("Upload percentage: " + uploader.getProgress());
	                      break;
	                  // This value is set after the entire media file has
	                  //  been successfully uploaded.
	                  case MEDIA_COMPLETE:
	                      System.out.println("Upload Completed!");
	                      break;
	                  // This value indicates that the upload process has
	                  //  not started yet.
	                  case NOT_STARTED:
	                      System.out.println("Upload Not Started!");
	                      break;
	              }
	          }
	      };
			
	      uploader.setProgressListener(progressListener);

	      // Upload the caption track.
	      Caption uploadedCaption = null;
		try {
			uploadedCaption = captionInsert.execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	      // Print the metadata of the uploaded caption track.
	      System.out.println("\n================== Uploaded Caption Track ==================\n");
	      snippet = uploadedCaption.getSnippet();
	      System.out.println("  - ID: " + uploadedCaption.getId());
	      System.out.println("  - Name: " + snippet.getName());
	      System.out.println("  - Language: " + snippet.getLanguage());
	      System.out.println("  - Status: " + snippet.getStatus());
	      System.out
	          .println("\n-------------------------------------------------------------\n");
		return uploadStatus;
		
	}

}
