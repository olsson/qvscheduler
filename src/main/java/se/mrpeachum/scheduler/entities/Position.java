/**
 * 
 */
package se.mrpeachum.scheduler.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author mrpeachum
 *
 */
@Entity
public class Position extends BaseEntity {

	public enum Type {
		REGULAR, FULLDAY
	}
	
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    
    private String color;
    
    @Enumerated(EnumType.STRING)
    private Type type;
    
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public void setTypeString(String type) {
		this.type = Type.valueOf(type);
	}
	
	public String getTypeString() {
		if (type == null) {
			return Type.REGULAR.name();
		} else {
			return type.name();
		}
	}

	/* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Position other = (Position) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

	@Override
	public String toString() {
		return "Position [id=" + id + ", name=" + name + ", color=" + color + ", type=" + type + "]";
	}
    
}
