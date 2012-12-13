/**
 * 
 */
package se.mrpeachum.scheduler.entities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author eolsson
 * 
 */
public class ShiftDto {

	private static final DateFormat DF = new SimpleDateFormat("hh:mm a"); 
	
	@NotNull
	private String position;

	@NotNull
	private Long day;

	@NotNull
	private Long employee;

	@NotNull
	@Pattern(regexp = "[0-9]{2}:[0-9]{2} (AM|PM)")
	private String startTime;

	@NotNull
	@Pattern(regexp = "[0-9]{2}:[0-9]{2} (AM|PM)")
	private String endTime;

	@NotNull
	private String[] days;

	public String getPosition() {
		return position;
	}

	public Long getDay() {
		return day;
	}

	public Long getEmployee() {
		return employee;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}
	
	public Date getStartDate() {
		Date d = null;
		try {
			d = DF.parse(this.startTime);
		} catch (ParseException e) {
			throw new IllegalStateException("Unable to parse date!", e);
		}
		return d;
	}

	public Date getEndDate() {
		Date d = null;
		try {
			d = DF.parse(this.endTime);
		} catch (ParseException e) {
			throw new IllegalStateException("Unable to parse date!", e);
		}
		return d;
	}
	
	public boolean shouldCopyToDayOfWeek(int dayOfWeek) {
		return Arrays.asList(this.days).contains(Integer.toString(dayOfWeek));
	}

	public String[] getDays() {
		return days;
	}
}
