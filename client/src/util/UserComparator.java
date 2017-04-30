package src.util;

import java.util.Comparator;

import src.main.User;

public class UserComparator implements Comparator<User> {

	@Override
	public int compare(User user1, User user2) {
		int temp1 = user2.getRank() - user1.getRank();
		int temp2 = user2.getWins() - user1.getWins();
		return temp1==0?temp2:temp1;
	}

}
