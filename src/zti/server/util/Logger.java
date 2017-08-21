package zti.server.util;

import java.util.*;
import java.time.LocalDateTime;

public class Logger {
	static {
		logs_ = new LinkedHashMap<LocalDateTime, String>();
	}

	private static Map<LocalDateTime, String> logs_;

	public static void Log(LocalDateTime time, String message) {
		if (logs_.size() > 10000)
			logs_.clear();

		logs_.put(time, message);
	}

	public static Map<LocalDateTime, String> getLogs() {
		return logs_;
	}
}
