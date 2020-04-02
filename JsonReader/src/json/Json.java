package json;

import java.util.LinkedList;
import java.util.Queue;

public class Json {
	public static Object CreateObject(Class<?> type, String json) {
		JsonReader reader = new JsonReader();
		Queue<Node> queue = new LinkedList<Node>();
		if (type.getAnnotation(JsonObject.class) != null) {
			Object result = null;
			try {
				result = type.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Node n = new Node();
			n.obj = result;
			n.text = Tool.Trim(json, ' ', '\n', '\r', '{', '}');
			queue.add(n);
			while(!queue.isEmpty()) {
				reader.construct(queue);
			}
			return result;
		} else {
			try {
				throw new Exception("类的上方需要添加正确的注解JsonObject");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
