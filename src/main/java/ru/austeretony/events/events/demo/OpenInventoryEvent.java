package ru.austeretony.events.events.demo;

import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class OpenInventoryEvent extends Event {
	
	/**
	 * OpenInventoryEvent срабатывает когда игрок открывает инвентарь.<br>
	 * Срабатывает через {@link DemoEventsInjection#onInventoryOpen(GuiOpenEvent)}<br>
	 * <br>
	 * Это событие можно отменить. {@link Cancelable}. <br>
	 * Отмена предотвратит открытие инвентаря.<br>
	 * <br>
	 * Это событие не имеет результата. {@link HasResult}<br>
	 * <br>
	 * Это событие использует {@link MinecraftForge#EVENT_BUS}.<br>
	 **/

	public OpenInventoryEvent() {}
}
