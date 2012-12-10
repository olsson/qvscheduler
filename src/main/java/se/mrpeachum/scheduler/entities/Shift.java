/**
 * 
 */
package se.mrpeachum.scheduler.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author mrpeachum
 *
 */
@Entity
public class Shift extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "positionId")
    private Position position;
    
    private Integer startHour;
    
    private Integer startMinute;
    
    private Integer endHour;
    
    private Integer endMinute;
    
    private Date day;

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
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * @return the startHour
     */
    public Integer getStartHour() {
        return startHour;
    }

    /**
     * @param startHour the startHour to set
     */
    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    /**
     * @return the startMinute
     */
    public Integer getStartMinute() {
        return startMinute;
    }

    /**
     * @param startMinute the startMinute to set
     */
    public void setStartMinute(Integer startMinute) {
        this.startMinute = startMinute;
    }

    /**
     * @return the endHour
     */
    public Integer getEndHour() {
        return endHour;
    }

    /**
     * @param endHour the endHour to set
     */
    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    /**
     * @return the endMinute
     */
    public Integer getEndMinute() {
        return endMinute;
    }

    /**
     * @param endMinute the endMinute to set
     */
    public void setEndMinute(Integer endMinute) {
        this.endMinute = endMinute;
    }

    public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
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
        Shift other = (Shift) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    } 
    
}
