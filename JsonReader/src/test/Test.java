package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import json.Json;

public class Test {
	public static void main(String[] args) {
		File f = new File("C:\\Users\\Administrator\\Desktop\\文件\\minecraft透视材质\\assets\\minecraft\\models\\block\\xray\\ice.json");
		FileInputStream is;
		byte[] dat = null;
		try {
			is = new FileInputStream(f);
			dat = new byte[(int) f.length()];
			is.read(dat);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonFile file = (JsonFile)Json.CreateObject(JsonFile.class, new String(dat));
		System.out.println(file);
	}
}
