package com.omniscien.LSYoutubeAPI.model;


import com.google.api.services.youtube.YouTube;
import com.omniscien.LSYoutubeAPI.util.Log4J;

public class Instant {

	public Instant() {
		// TODO Auto-generated constructor stub
	}
	
	YouTube youtube;
	
	Log4J oLog;

	public YouTube getYoutube() {
		return youtube;
	}

	public void setYoutube(YouTube youtube) {
		this.youtube = youtube;
	}

	public Log4J getoLog() {
		return oLog;
	}

	public void setoLog(Log4J oLog) {
		this.oLog = oLog;
	}
	
	
	

}
