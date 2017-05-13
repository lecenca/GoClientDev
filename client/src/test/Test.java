package src.test;

import java.io.UnsupportedEncodingException;

import src.main.User;

public class Test {
	public static void main(String[] args) throws UnsupportedEncodingException {
		/*String s = "	";
		String s2 = "    ";
		System.out.println("tab:" + s.length());
		System.out.println("space:" + s2.length());
		User user = new User();*/
		String property = System.getProperty("line.separator");
		System.out.println("hello");
		System.out.println(property);
		System.out.println("你好");
		System.out.println("你".getBytes("utf-16").length);
		System.out.println("a".getBytes().length);
		System.out.println("a你好啊b".length());
		System.out.println("ab".getBytes().length);
		System.out.println("你好".getBytes().length);
		String text = "a*&@g你好";
		int length = text.length();
		for(int i = 0; i < text.length(); i++)
            if(text.substring(i,i+1).getBytes().length >1)
                length++;
		System.out.println("length:" + length);
	}
}
