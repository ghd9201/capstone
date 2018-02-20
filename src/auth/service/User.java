package auth.service;

public class User {

	private String id;
	private String name;
	private int group;
	
	public User(String id, String name, int group) {
		this.id = id;
		this.name = name;
		this.group = group;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public int getGroup() {
		return group;
	}
}
