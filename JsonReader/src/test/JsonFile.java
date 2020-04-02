package test;

import json.JsonObject;

@JsonObject
public class JsonFile {
	public String parent;
	@JsonObject
	public Texture textures;
}
