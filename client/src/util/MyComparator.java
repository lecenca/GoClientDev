package src.util;

import java.util.Comparator;

import src.main.User;

public class MyComparator implements Comparator<User> {

	@Override
	public int compare(User user1, User user2) {
		int temp1 = user2.getData().getRank() - user1.getData().getRank();
		int temp2 = user2.getData().getWin() - user1.getData().getWin(); 
		return temp1==0?temp2:temp1;
	}

}
