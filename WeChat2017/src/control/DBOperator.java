package control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import model.User;

public class DBOperator {
	public static boolean register(User user) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data" + user.getUsername() + ".xxx"));
			out.writeObject(user);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public static  User  login(String username,String password) {
		File  data=new File("databases/"+username+".qq");
		if(!data.exists()) {
			return null;
		}else
		{
			
			try {
				ObjectInputStream  in=new ObjectInputStream(new FileInputStream("databases/"+username+".qq"));
				User dbUser=(User)in.readObject();
				if(password.equals(dbUser.getPassword()))
				{
					return dbUser;
				}else
				{
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
	}
	public static void main(String[] args) {
		User user1 = new User("1111", "1111", "大飞哥", "男", 99, "没有我大飞哥写不出的代码", "");
		User user2 = new User("2222", "2222", "谢老板", "男", 18, "没有我谢老板搞不定的事情", "");

		Map<String, HashSet<User>> friends = new HashMap<>();
		HashSet<User> f1s = new HashSet<>();

		f1s.add(user2);
		friends.put("老板", f1s);
		user1.setFriends(friends);

		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream("data/" + user1.getUsername() + ".xxx"));
			out.writeObject(user1);
			out.flush();
			out.close();

			out = new ObjectOutputStream(new FileOutputStream("data/" + user2.getUsername() + ".xxx"));
			out.writeObject(user2);
			out.flush();
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
