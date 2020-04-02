package json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class JsonReader {
	public void construct(Queue<Node> queue) {
		Node node = queue.element();
		queue.remove();
		int numSquare = 0;// 方括号计数,遇到'['该数值+1,遇到']'该数值-1
		int numBrace = 0;// 大括号计数,同上
		int beginFlag = 0;// 一个json元素的起始位置
		int splitFlag = 0;// 一个json元素:的位置
		String name = "";// json的键名
		// JsonObject的处理逻辑
		if (node.obj.getClass().getAnnotation(JsonObject.class) != null) {
			for (int i = 0; i < node.text.length(); i++) {
				if (node.text.charAt(i) == ':' && numSquare == 0 && numBrace == 0) {
					name = node.text.substring(beginFlag, i);
					name = Tool.Trim(name, ' ', '\"', '\n', '\r');
					splitFlag = i + 1;
				} else if ((node.text.charAt(i) == ',' && numSquare == 0 && numBrace == 0)
						|| (i == node.text.length() - 1)) {
					String dat = node.text.substring(splitFlag, i);
					beginFlag = i + 1;
					Field f = null;
					try {
						f = node.obj.getClass().getDeclaredField(Tool.Trim(name, '\"', ' '));
					} catch (NoSuchFieldException | SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (f.getAnnotation(JsonObject.class) != null) {
						Object res = null;
						try {
							res = f.getClass().newInstance();
							f.set(node.obj, res);
						} catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Node n = new Node();
						n.obj = res;
						n.text = Tool.Trim(dat, ' ', '\n', '\r', '{', '}');
						queue.add(n);
					} else if (f.getAnnotation(JsonArray.class) != null) {
						Object arr = null;
						arr = Array.newInstance(f.getClass(), 0);
						try {
							f.set(node.obj, arr);
						} catch (IllegalArgumentException | IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Node n = new Node();
						n.obj = arr;
						n.text = Tool.Trim(dat, ' ', '\r', '\n', '[', ']');
						queue.add(n);
					} else {
						try {
							f.set(node.obj, Tool.SetValue(f.getClass(), Tool.Trim(dat, ' ', '\r', '\n')));
						} catch (IllegalArgumentException | IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				if (node.text.charAt(i) == '{')
					numBrace++;
				else if (node.text.charAt(i) == '}')
					numBrace--;
				else if (node.text.charAt(i) == '[')
					numSquare++;
				else if (node.text.charAt(i) == ']')
					numSquare--;
			}
		}
		// JsonArray的处理逻辑
		else if (node.obj.getClass().getAnnotation(JsonArray.class) != null) {
			List<Object> list = new ArrayList<Object>();
			Class<?> type = node.obj.getClass().getComponentType();
			for (int i = 0; i < node.text.length(); i++) {
				if ((node.text.charAt(i) == ',' && numBrace == 0 && numSquare == 0) || (i == node.text.length() - 1)) {
					String dat = node.text.substring(beginFlag, i);
					beginFlag = i + 1;
					if (type.getAnnotation(JsonObject.class) != null) {
						try {
							Object o = type.newInstance();
							Node n = new Node();
							n.obj = o;
							n.text = Tool.Trim(dat, ' ', '\r', '\n', '{', '}');
							list.add(o);
						} catch (InstantiationException | IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (type.getAnnotation(JsonArray.class) != null) {
						Object o = Array.newInstance(type, 0);
						Node n = new Node();
						n.obj = 0;
						n.text = Tool.Trim(dat, ' ', '\r', '\n', '[', ']');
						list.add(o);
					} else {
						list.add(Tool.SetValue(type, Tool.Trim(dat, ' ', '\n', '\r')));
					}
				}
				if (node.text.charAt(i) == '{')
					numBrace++;
				else if (node.text.charAt(i) == '}')
					numBrace--;
				else if (node.text.charAt(i) == '[')
					numSquare++;
				else if (node.text.charAt(i) == ']')
					numSquare--;
			}
			Object[] a = list.toArray();
			System.arraycopy(a, 0, node.obj, 0, a.length);
		} else {
			try {
				throw new Exception("你似乎忘记设置JsonObject或者JsonArray的注解了");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
