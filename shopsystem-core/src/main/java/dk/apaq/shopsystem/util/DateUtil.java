package dk.apaq.shopsystem.util;

import java.util.Date;

/**
 * A simple util class for common Date stuff.
 * @author michael
 */
public class DateUtil {

	public static Date getToday(){
		return getBeginningOfDay(new Date());
	}
	
	public static Date getNow(){
		return new Date();
	}
	
	public static Date getBeginningOfDay(Date date){
		date = new Date(date.getTime());
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		return date;
	}
	
	public static Date getEndOfDay(Date date){
		date = new Date(date.getTime());
		date.setHours(23);
		date.setMinutes(59);
		date.setSeconds(59);
		return date;
	}
	
	public static Date getBeginningOfMonth(Date date){
		date = new Date(date.getTime());
		date.setDate(1);
		date=getBeginningOfDay(date);
		return date;
	}
	
	public static Date getEndOfMonth(Date date){
		//This is awkward. Theres i no way to get the number 
		//of days in a month. But we can go to the beginning
		//of the month, then add one month and subtract one day.
		//That will live us the last day in the current month
		//(BLAME SUN)
		date = getBeginningOfMonth(date);
		date.setMonth(date.getMonth()+1);
		date.setDate(date.getDate()-1);
		date=getEndOfDay(date);
		return date;
	}
}
