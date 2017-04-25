package src.main;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadLock {
	public static final Lock lock = new ReentrantLock();
	public static final Condition client = lock.newCondition();
}
