/**
 * 
 */
package se.mrpeachum.scheduler.entities;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


/**
 * @author eolsson
 *
 */
public class ShiftDto {

	@NotNull
	private String position;
	
	@NotNull
	private Long day;

	@NotNull
	private Long employee;

	@NotNull
	@Pattern(regexp="[0-9]{1,2}")
	private String startHour;
	
	@NotNull
	@Pattern(regexp="(00|15|30|45)")
	private String startMinute;
	
	@NotNull
	@Pattern(regexp="[0-9]{1,2}")
	private String endHour;
	
	@NotNull
	@Pattern(regexp="(00|15|30|45)")
	private String endMinute;

	public String getPosition() {
		return position;
	}

	public Long getDay() {
		return day;
	}

	public Long getEmployee() {
		return employee;
	}

	public String getStartHour() {
		return startHour;
	}

	public String getStartMinute() {
		return startMinute;
	}

	public String getEndHour() {
		return endHour;
	}

	public String getEndMinute() {
		return endMinute;
	}
	
}
