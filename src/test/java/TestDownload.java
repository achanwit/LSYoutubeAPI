import com.google.api.services.youtube.YouTube;
import com.omniscien.LSYoutubeAPI.caption.Init;
import com.omniscien.LSYoutubeAPI.caption.Download;
import com.omniscien.LSYoutubeAPI.model.Instant;

public class TestDownload {

	public TestDownload() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		//Variable ClientSecrets
		String ClientSecrets = "{\"installed\":{\"client_id\":\"1047143706622-f1rd3tmdua6mdm2bfjin05tkll4d0pge.apps.googleusercontent.com\",\"project_id\":\"booming-bonito-309508\",\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\"token_uri\":\"https://oauth2.googleapis.com/token\",\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\",\"client_secret\":\"dM_Ez4H82Qml6h7oMXZ7OGSX\",\"redirect_uris\":[\"urn:ietf:wg:oauth:2.0:oob\",\"http://localhost\"]}}";

		//Variable DowloadFilePath
		String downloadFile = "/home/chanwit/Documents/UploadSubtitleYoutube/POC/TestDownload.srt";
		
		//Variable Caption ID
		String captionId = "spmIaeUEWeNi-vVga3cqcYxM1Ay9iD--1hJuicWUGexLZEdzEu27SVI9AbZyWVUd";
		
		//Initial
		Init init = new Init();		
		Instant instant = init.initial(ClientSecrets);
		
		//Download Caption
		Download download = new Download();
		String downloadStatus = download.DownloadCaption(instant, captionId, downloadFile);
		
		System.out.println("downloadStatus: "+downloadStatus);

	}

}
