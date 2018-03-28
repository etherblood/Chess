package com.etherblood.chess.bot.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class HttpService {

	private final Config config;
	private final Gson gson;

	public HttpService(Config config, Gson gson) {
		super();
		this.config = config;
		this.gson = gson;
	}

	public <T> T post(String path, Object body, Type resultType) throws IOException {
		URL url = buildUrl(path);
		URLConnection connection = url.openConnection();
		HttpURLConnection http = (HttpURLConnection) connection;
		http.setRequestMethod("POST");
		http.setDoOutput(true);
		addBasicAuth(http);

		if (body != null) {
			byte[] out = gson.toJson(body).getBytes(StandardCharsets.UTF_8);
			int length = out.length;

			http.setFixedLengthStreamingMode(length);
			http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			http.connect();
			try (OutputStream os = http.getOutputStream()) {
				os.write(out);
				os.flush();
			}
		}
		try (InputStream is = http.getInputStream()) {
			return gson.fromJson(new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8)), resultType);
		}
	}

	public <T> T get(String path, Type resultType) throws IOException {
		URL url = buildUrl(path);
		URLConnection connection = url.openConnection();
		HttpURLConnection http = (HttpURLConnection) connection;
		http.setRequestMethod("GET");
		http.setDoOutput(true);
		addBasicAuth(http);

		http.connect();
		try (InputStream is = http.getInputStream()) {
			return gson.fromJson(new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8)), resultType);
		}
	}

	private void addBasicAuth(HttpURLConnection http) {
		http.setRequestProperty("Authorization", "Basic " + Base64.getEncoder()
				.encodeToString((config.loginHandle + ":" + config.password).getBytes(StandardCharsets.UTF_8)));
	}

	private URL buildUrl(String path) throws MalformedURLException {
		return URI.create(config.serverUrl + path).toURL();
	}
}
