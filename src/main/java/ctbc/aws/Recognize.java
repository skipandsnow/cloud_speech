package ctbc.aws;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Recognize {
	private final String USER_AGENT = "Mozilla/5.0";
	private String client_id = "amzn1.application-oa2-client.6c35c31094b547d69cc035026bd82c5f";
	private String client_secret = "2cedb31a56e7eef3d5349f8f66217ee3023c14e2fcf9ac5238811f82084b52de";
	private String secure_profile = "CTBC";
	private String secure_profile_id = "amzn1.application.1493d5dcb01b4edbbe66b66d7b1dd96c";
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	OkHttpClient client = new OkHttpClient();
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Recognize recognize = new Recognize();

		// Sending get request
//		recognize.sendingGetRequest();

		// Sending post request
		// http.sendingPostRequest();
		recognize.get();
	}

	

	String get() throws IOException {
//		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder()
				.url("https://avs-alexa-na.amazon.com")
				.header("host", "avs-alexa-na.amazon.com")
				.addHeader(":method", "get")
				.addHeader(":scheme", "https")
				.addHeader(":path", "/v20160207/directives")
				.addHeader("authorization", "Bearer Atza%7CIwEBIHR1GnfqLXt3DMfER6z7QFvj40ArcKfRAg2KPX93dAsWWE9tOBF0lfRrcDj6JP2yKz9ugsA_wk6r9JJ6Z7FETKn0x8m6zUEeLyMC8kMZjJ8AQ1INNMAlXvfjxht3KOWnOXw6N19KY2DRBtnniKUh7ZrVXeQku-Uos_9wiAo8iIp1H403FDXqQP1l2KwXWaOGysBR9WYNih9A2A_MNqYGQo-D6NyM-oTlKwVwiDcCjfbg0J3WzTVT22i_ttIa2jUBalPVvz3CFu8M-nNGL6iS6HWuU9imcW_R8yaZl_HZouUb51s4f9hJJswNxbecT87oQvZq3VBnHytQfUrTWRVm5cVRDHaB2Pn_J3DOB6DgDdB9Q-cMMw4SVzTY0tLB4I8ThC9y8hTkvHL5yAYZ7wXv6Ya5qJt4URHnvzeP-w5mlK5d-3dSDTdxQiBW-a288Q7Pj7gXy7j5ldQ4_vfVOWFV5wRV2JR67BlMW7maq00RExTg6pptTt9r85qmBAZABni2SqTh2n_F5lOeEAXSAkTWqjEzFzJzMf0UeuNlKulTig8xRQ")
//				.post(body)
				.build();
		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}

	String bowlingJson(String player1, String player2) {
		return "{'winCondition':'HIGH_SCORE'," + "'name':'Bowling'," + "'round':4," + "'lastSaved':1367702411696,"
				+ "'dateStarted':1367702378785," + "'players':[" + "{'name':'" + player1
				+ "','history':[10,8,6,7,8],'color':-13388315,'total':39}," + "{'name':'" + player2
				+ "','history':[6,10,5,10,10],'color':-48060,'total':41}" + "]}";
	}

	// HTTP GET request
	private void sendingGetRequest() throws Exception {

		StringBuffer urlSb = new StringBuffer();
		urlSb.append("https://www.amazon.com/ap/oa?");
		urlSb.append("&client_id=" + client_id);
		urlSb.append("&scope=" + "profile");
		urlSb.append("&redirect_uri=" + "http://localhost");
		urlSb.append("&response_type=" + "token");
		URL url = new URL(urlSb.toString());
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

		// By default it is GET request
		con.setRequestMethod("GET");
		con.setInstanceFollowRedirects(true);
		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("Sending get request : " + url);
		System.out.println("Response code : " + responseCode);
		String response = readFullyAsString(con.getInputStream(), "UTF-8");
		System.out.println(response);
		System.out.println("redirected url: " + con.getURL());

	}

	public String readFullyAsString(InputStream inputStream, String encoding) throws IOException {
		return readFully(inputStream).toString(encoding);
	}

	private ByteArrayOutputStream readFully(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		return baos;
	}

}
