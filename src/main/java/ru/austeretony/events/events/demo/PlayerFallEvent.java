package ru.austeretony.events.events.demo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PlayerFallEvent extends Event {

	/**
	 * PlayerFallEvent срабатывает когда грок приземляется после падения.<br>
	 * Срабатывает через {@link DemoEventsInjection#onLivingUseItem(LivingEntityUseItemEvent.Tick)}<br>
	 * <br>
	 * {@link #player} упавший игрок.<br>
	 * {@link #distance} дистанция падения.<br>
	 * <br>
	 * Это событие можно отменить. {@link Cancelable}. <br>
	 * При отмене игрок не получит урона от падения.<br>
	 * <br>
	 * Это событие не имеет результата. {@link HasResult}<br>
	 * <br>
	 * Это событие использует {@link MinecraftForge#EVENT_BUS}.<br>
	 **/
	
	private final EntityPlayer player;
	
	private float distance;
	
	public PlayerFallEvent(EntityPlayer player, float distance) {
		
		this.player = player;
		this.distance = distance;
	}
	
	public EntityPlayer getPlayer() {
		
		return this.player;
	}
	
	public float getDistance() {
		
		return this.distance;
	}
	
	public void setDistance(float distance) {
		
		this.distance = distance;
	}
}
