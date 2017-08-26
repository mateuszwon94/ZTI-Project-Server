package zti.server.util;

import java.util.*;
import java.time.LocalDateTime;

/**
 * @author Mateusz Winiarski
 * klasa przechowujaca logi
 */
public class Logger {
	static {
		logs_ = new LinkedHashMap<LocalDateTime, String>();
	}

	/**
	 * Funkcja zapisujaca log
	 * @param time czas wystapienia
	 * @param message tresc logu
	 */
	public static void Log(LocalDateTime time, String message) {
		if (logs_.size() > 10000)
			logs_.clear();

		logs_.put(time, message);
	}

	/**
	 * @return logi aplikacji
	 */
	public static Map<LocalDateTime, String> getLogs() {
		return logs_;
	}

	/**
	 * Mapa przechowujaca logi i czas logowania
	 */
	private static Map<LocalDateTime, String> logs_;
}
