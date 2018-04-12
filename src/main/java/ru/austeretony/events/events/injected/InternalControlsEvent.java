package ru.austeretony.events.events.injected;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import ru.austeretony.events.coremod.CustomEventsFactory;

@Cancelable
public class InternalControlsEvent extends Event {
	
	/**
	 * InternalControlsEvent.Debug срабатывает когда игрок пытается открыть меню отладки.<br>
	 * Срабатывает через {@link CustomEventsFactory#onDebugMenuOpen()}<br>
	 * <br>
	 * {@link #controlType} нажатая кнопка: HIDE_GUI - F1, DEBUG - F3<br>
	 * <br>
	 * Это событие можно отменить. {@link Cancelable}. <br>
	 * При отмене события меню отладки не будет открыто.<br>
	 * <br>
	 * Это событие не имеет результата. {@link HasResult}<br>
	 * <br>
	 * Это событие использует {@link MinecraftForge#EVENT_BUS}.<br>
	 **/
		
	private final EnumControlType controlType;

	public InternalControlsEvent(EnumControlType controlType) {
				
		this.controlType = controlType;
	}
	
	public EnumControlType getControl() {
		
		return this.controlType;
	}
	
    public static enum EnumControlType {
    	
    	HIDE_GUI,
        DEBUG
    }
}
