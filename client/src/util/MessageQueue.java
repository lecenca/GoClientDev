package src.util;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageQueue<T> {
	private ConcurrentLinkedQueue<T> queue;
	public MessageQueue() {
		//创建线程安全的列表
		queue = new ConcurrentLinkedQueue<T>();
			
	}
	public  void add(T t) {
		queue.add(t);
		
	}
	public T remove() {
		return queue.poll();
	}
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	public ConcurrentLinkedQueue<T> getQueue() {
		return queue;
	}
	
	
}
