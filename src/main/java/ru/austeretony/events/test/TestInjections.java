package ru.austeretony.events.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestInjections {

	public static final Logger LOGGER = LogManager.getLogger();

	public static void showValues(int field, int local) {
		
		LOGGER.info("***Внедрённый код***");
		LOGGER.info("Значение поля <someValue> равно: " + field + ", переданного аргумента: " + local);
		LOGGER.info("***Внедрённый код***");
	}
}
