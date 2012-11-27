package usermanager;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username = null;
    private int id;
    private String password = null;
    private List<ResourceState> resources;

    public void setResources(List<ResourceState> resources) {
		this.resources = resources;
	}

	/**
     * Constructor for user, receives the username and password.
     * 
     * @param username
     *            username of the user.
     * @param password
     *            password of the user.
     */
    public User(int id) {
        // this.username = username;
        // this.password = new SHA1().getHash(password);
    	
       
    	this.id = id;
        
        //this.devices = new ArrayList<Device>();
        this.resources = new ArrayList<ResourceState>();
    }

    public int getId() {
        return id;
    }

    /**
     * Returns the user's username.
     * 
     * @return user's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password of the user.
     * 
     * @return password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the list of devices for this user.
     * 
     * @return the list of devices.
     */
   /* public List<Device> getDevices() {
        return devices;
    }*/

    public List<ResourceState> getResources() {
        return this.resources;
    }
    
    
    public void updateResources(){
    	
    }
}
