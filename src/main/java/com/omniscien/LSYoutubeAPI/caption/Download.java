package com.omniscien.LSYoutubeAPI.caption;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;
import com.google.api.services.youtube.YouTube;
import com.google.common.collect.Lists;
import com.omniscien.LSYoutubeAPI.util.Auth;
import com.omniscien.LSYoutubeAPI.util.Constant;
import com.omniscien.LSYoutubeAPI.util.ReadProp;
import com.omniscien.LSYoutubeAPI.util.Constant;
import com.omniscien.LSYoutubeAPI.model.Instant;


public class Download {
	
	public Download() {
		// TODO Auto-generated constructor stub
	}
	
	public String DownloadCaption(final Instant instant, String CaptionId, String outputPath) {
		
		instant.getoLog().writeLog("Start dowonload caption id:"+CaptionId, false);
		
		
		// Variable
		com.google.api.services.youtube.YouTube.Captions.Download captionDownload = null;

		// Prepare input CaptionId
		if (CaptionId == null) {
			instant.getoLog().writeError("Caption ID cannot null.");
			return "Caption ID cannot null.";
		} else {
			if (CaptionId.equals("")) {
				instant.getoLog().writeError("Caption ID cannot empty.");
				return "Caption ID cannot empty.";
			}
		}

		// Prepare input outputPath
		if (outputPath == null) {
			instant.getoLog().writeError("Output path is null.");
			return "Output path cannot null.";
		} else {
			if (outputPath.equals("")) {
				instant.getoLog().writeError("Output path is empty.");
				return "Output path cannot empty.";
			}
		}


		// Create an API request to the YouTube Data API's captions.download
		// method to download an existing caption track.
		try {
			captionDownload = instant.getYoutube().captions().download(CaptionId).setTfmt(Constant.SRT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			instant.getoLog().writeError(e.toString());
			e.printStackTrace();
		}

		// Set the download type and add an event listener.
		MediaHttpDownloader downloader = captionDownload.getMediaHttpDownloader();

		// Indicate whether direct media download is enabled. A value of
		// "True" indicates that direct media download is enabled and that
		// the entire media content will be downloaded in a single request.
		// A value of "False," which is the default, indicates that the
		// request will use the resumable media download protocol, which
		// supports the ability to resume a download operation after a
		// network interruption or other transmission failure, saving
		// time and bandwidth in the event of network failures.
		downloader.setDirectDownloadEnabled(false);

		// Set the download state for the caption track file.
		MediaHttpDownloaderProgressListener downloadProgressListener = new MediaHttpDownloaderProgressListener() {
			@Override
			public void progressChanged(MediaHttpDownloader downloader) throws IOException {
				switch (downloader.getDownloadState()) {
				case MEDIA_IN_PROGRESS:
					System.out.println("Download in progress");
					System.out.println("Download percentage: " + downloader.getProgress());
					break;
				// This value is set after the entire media file has
				// been successfully downloaded.
				case MEDIA_COMPLETE:
					System.out.println("Download Completed!");
					break;
				// This value indicates that the download process has
				// not started yet.
				case NOT_STARTED:
					System.out.println("Download Not Started!");
					break;
				}
			}
		};
		downloader.setProgressListener(downloadProgressListener);

		OutputStream outputFile = null;
		try {
			outputFile = new FileOutputStream(outputPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Download the caption track.
		try {
			captionDownload.executeAndDownloadTo(outputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			instant.getoLog().writeError(e.toString());
			e.printStackTrace();
		}
		instant.getoLog().writeLog("Dowonload caption id:"+CaptionId+" finished.", false);
		instant.getoLog().writeLog("Output at: "+outputPath, false);
		
		return "Download success: " +outputPath; 

	}

}
