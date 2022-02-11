import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;

public class HttpHelper {
	public static String get(String url, String query) throws Exception {
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url + "?" + query))
				.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
				.header("accept", "application/json").header("Accept-Charset", "utf-8").GET().build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		return response.body();
	}

	public static String post(String url, String query) throws Exception {
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
				.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
				.header("accept", "application/json").header("Accept-Charset", "utf-8")
				.POST(BodyPublishers.ofString(query)).build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		return new String(response.body().getBytes(StandardCharsets.UTF_8), "UTF-8");
	}
}
