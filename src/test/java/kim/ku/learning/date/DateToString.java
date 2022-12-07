package kim.ku.learning.date;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DateToString {


	@Test
	void dateToString() {
		Date date = new Date();

		String result = new SimpleDateFormat("yyyyMMddHHmm").format(date);

		System.out.println(result);
	}

	@Test
	void 시간연산() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");

		System.out.println(df.format(cal.getTime()));
		cal.add(Calendar.MINUTE, -1);
		System.out.println(df.format(cal.getTime()));
	}

	@Test
	void 시간연산반복() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");

		List<String> keys = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			cal.add(Calendar.MINUTE, -1);
			String format = df.format(cal.getTime());
			System.out.println(format);
		}
	}
}
