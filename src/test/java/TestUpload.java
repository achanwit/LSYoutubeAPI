import com.google.api.services.youtube.YouTube;
import com.omniscien.LSYoutubeAPI.caption.Init;
import com.omniscien.LSYoutubeAPI.model.Instant;
import com.omniscien.LSYoutubeAPI.caption.Upload;
public class TestUpload {

	
	public TestUpload() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		// Variable ClientSecrets
		String ClientSecrets = "{\"installed\":{\"client_id\":\"1047143706622-f1rd3tmdua6mdm2bfjin05tkll4d0pge.apps.googleusercontent.com\",\"project_id\":\"booming-bonito-309508\",\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\"token_uri\":\"https://oauth2.googleapis.com/token\",\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\",\"client_secret\":\"dM_Ez4H82Qml6h7oMXZ7OGSX\",\"redirect_uris\":[\"urn:ietf:wg:oauth:2.0:oob\",\"http://localhost\"]}}";
		String VideoIdStr = "qsEB5ZCbPSU";
		String Languageid = "th";
		String CaptionName = "THaiSub";
		String CaptionFilePath = "/home/chanwit/Documents/UploadSubtitleYoutube/TH-Sub.srt";

		// Initial
		Init init = new Init();
		Instant instant = init.initial(ClientSecrets);

		// Upload
		Upload cp = new Upload();

		/***
		 * Param String VideoIdStr, String Languageid, String CaptionName, String
		 * CaptionFilePath
		 ***/

		boolean uploadstatus = cp.UploadCaption(instant, VideoIdStr, Languageid, CaptionName, CaptionFilePath);
		System.out.println("uploadstatus: " + uploadstatus);

	}

}
