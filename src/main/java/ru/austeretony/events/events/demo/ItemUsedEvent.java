package ru.austeretony.events.events.demo;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ItemUsedEvent extends Event {
	
	/**
	 * ItemUsedEvent срабатывает когда EntityLivingBase завершает использование предмета.<br>
	 * Срабатывает через {@link DemoEventsInjection#onLivingUseItem(LivingEntityUseItemEvent.Tick)}<br>
	 * <br>
	 * {@link #livingBase} EntityLivingBase, использовавшую предмет.<br>
	 * {@link #itemStack} используемый предмет.<br>
	 * <br>
	 * Это событие нельзя отменить. {@link Cancelable}. <br>
	 * <br>
	 * Это событие не имеет результата. {@link HasResult}<br>
	 * <br>
	 * Это событие использует {@link MinecraftForge#EVENT_BUS}.<br>
	 **/
	
	private final EntityLivingBase livingBase;

	private final ItemStack itemStack;

	public ItemUsedEvent(EntityLivingBase livingBase, ItemStack itemStack) {
		
		this.livingBase = livingBase;
		this.itemStack = itemStack;
	}
	
	public EntityLivingBase getEntityLiving() {
		
		return this.livingBase;
	}
	
	public ItemStack getItemStack() {
		
		return this.itemStack;
	}
}
