package ru.austeretony.events.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestClass {
	
	public static final Logger LOGGER = LogManager.getLogger();

	private int someInt = 5;
	
	public TestClass() {
				
		this.addTo(10);	
	}
	
	public void addTo(int value) {
		
		this.someInt = this.someInt + value;	
		//Добавленная строка
		//TestInjections.showValues(this.someInt, value);
		log();
	}
	
	public void log() {
		
		LOGGER.info("***************Вам не узнать значение поля <someInt>!***************");
	}
}
