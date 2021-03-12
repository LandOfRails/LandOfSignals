package net.landofrails.stellwand.content.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.landofrails.stellwand.utils.exceptions.ContentPackException;

public class ContentPack {

	private String modversion = "";
	private String name = "";
	private String packversion = "";
	private String author = "";

	private List<String> content = new LinkedList<>();

	// Manually filled by Loader.
	private List<ContentPackEntry> entries = new LinkedList<>();

	public ContentPack(String modversion, String name, String packversion, String author, List<String> content) {
		this.modversion = modversion;
		this.name = name;
		this.packversion = packversion;
		this.author = author;
	}

	public void setEntries(List<ContentPackEntry> entries) {
		this.entries = entries;
	}

	public List<ContentPackEntry> getEntries() {
		return entries;
	}

	public String getModversion() {
		return modversion;
	}

	public String getName() {
		return name;
	}

	public String getPackversion() {
		return packversion;
	}

	public String getAuthor() {
		return author;
	}

	public List<String> getContent() {
		return content;
	}

	public String getId() {
		return name + "@" + author;
	}

	public static ContentPack fromJson(InputStream stellwandJsonStream) {

		StringBuilder s = new StringBuilder();
		byte[] buffer = new byte[1024];
		int read = 0;

		try {
			while ((read = stellwandJsonStream.read(buffer, 0, 1024)) >= 0) {
				s.append(new String(buffer, 0, read));
			}
		} catch (IOException e) {
			throw new ContentPackException("Cant read stellwand.json: " + e.getMessage());
		}

		String json = s.toString();
		Gson gson = new GsonBuilder().create();

		return gson.fromJson(json, ContentPack.class);

	}

}
