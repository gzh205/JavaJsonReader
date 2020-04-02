package json;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.imageio.ImageIO;

public class Tool {
	public static String Trim(String dat, char... format) {
		boolean begin;// 开始标记
		boolean end;// 结束标记
		int beginIndex = 0;
		int endIndex = dat.length();
		for (int i = 0; i < dat.length(); i++) {
			begin = true;
			for (char c : format) {
				if (dat.charAt(i) == c) {
					if (dat.charAt(i) == '{' || dat.charAt(i) == '[' || dat.charAt(i) == '\"') {
						i++;
						break;
					}
					begin = false;
					break;
				}
			}
			if (begin) {
				beginIndex = i;
				break;
			}
		}
		for (int i = dat.length() - 1; i >= 0; i--) {
			end = true;
			for (char c : format) {
				if (dat.charAt(i) == c) {
					if (dat.charAt(i) == '}' || dat.charAt(i) == ']' || dat.charAt(i) == '\"') {
						i--;
						break;
					}
					end = false;
					break;
				}
			}
			if (end) {
				endIndex = i;
				break;
			}
		}
		return dat.substring(beginIndex, endIndex + 1);
	}

	public static Object SetValue(Type c, String dat) {
		if (c == int.class || c == Integer.class) {
			return Integer.parseInt(dat);
		} else if (c == float.class || c == Float.class) {
			return Float.parseFloat(dat);
		} else if (c == double.class || c == Double.class) {
			return Double.parseDouble(dat);
		} else if (c == String.class) {
			return Tool.Trim(dat, ' ', '\"');
		} else if (c == Image.class) {
			Decoder de = Base64.getMimeDecoder();
			byte[] buffer = de.decode(dat);
			Image img = null;
			try {
				img = ImageIO.read(new ByteArrayInputStream(buffer));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return img;
		}
		return null;
	}
}
