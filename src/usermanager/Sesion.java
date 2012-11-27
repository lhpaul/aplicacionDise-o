package usermanager;

import java.security.SecureRandom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import usermanager.SHA1;

public class Sesion implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String name;
    private int id;
    private String password;
    private List<User> users;
    private User owner;

    /**
     * Constructor for Sesion.
     * @param name name of the sesion.
     * @param password password of the sesion.
     */
    public Sesion(){
//        this.name = name;
//        this.password = new SHA1().getHash(password);
    	SecureRandom random = new SecureRandom();
    	this.id = random.nextInt();
        this.users = new ArrayList<User>();
    }

    /**
     * Sets the sesion owner.
     * @param u user owner of this sesion.
     */
    public void setOwner(User u){
        this.owner = u;
    }

    /**
     * Returns the owner of the sesion.
     * @return owner of the sesion.
     */
    public User getOwner(){
        return owner;
    }

    /**
     * Returns the name of the sesion.
     * @return name of the sesion.
     */
    public String getName(){
        return name;
    }

    /**
     * Returns the password of the sesion.
     * @return password of the sesion.
     */
    public String getPassword(){
        return password;
    }

    /**
     * Returns the list of users connected to this sesion.
     * @return list of users.
     */
    public List<User> getUsersList(){
        return users;
    }
}
