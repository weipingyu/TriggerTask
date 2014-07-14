package cron;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.opensymphony.oscache.util.FastCronParser;

public class CronTest {

	public static void main(String[] args) throws ParseException {
		FastCronParser fastCronParser = new FastCronParser("0 6 * * *");
		System.out.println(DateFormatUtils.format(new Date(fastCronParser.getTimeBefore(System.currentTimeMillis())),"yyyy-MM-dd HH:mm:ss"));

		System.out.println("dev");
		
	}

}
