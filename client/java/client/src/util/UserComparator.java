/******************************************************************************
 * This file is licensed under the Unlicense. See License.txt for details.
 *
 * Author:
 *   Alinshans (https://github.com/Alinshans/GoClientDev)
 *   zengxingbin (https://github.com/zengxingbin/GoClientDev)
 *   lecenca (https://github.com/lecenca/GoClientDev)
 *
 * Copyright (c) 2017. All rights reserved.
 *****************************************************************************/

package src.util;

import src.main.User;

import java.util.Comparator;

public class UserComparator implements Comparator<User> {

    @Override
    public int compare(User user1, User user2) {
        int temp = user2.getPriority() - user1.getPriority();
        if (temp != 0)
            return temp;
        int temp1 = user2.getRank() - user1.getRank();
        int temp2 = user2.getWin() - user1.getWin();
        return temp1 == 0 ? temp2 : temp1;
    }

}
