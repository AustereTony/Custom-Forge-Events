package ru.austeretony.events.events.demo;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.austeretony.events.main.EventsMain;

@EventBusSubscriber(modid = EventsMain.MODID)
public class DemoEventsInjection {
	
	/*
	 * Демонстрационные хуки собственных простых событий.
	 * Созданы для симуляции настоящих событий.
	 */

	@SubscribeEvent
	public static void onLivingUseItem(LivingEntityUseItemEvent.Tick event) {			
			
		if (event.getEntityLiving().getItemInUseCount() == 1) {//За тик до окончания использования предмета.
						
			//Вызов события. 
			DemoEventFactory.onLivingFinishesUsingItem(event.getEntityLiving(), event.getItem());
		}
	}
	
	@SubscribeEvent
	public static void onInventoryOpen(GuiOpenEvent event) {			
		
		if (event.getGui() instanceof GuiInventory) {
						
			//Вызов события. 
			//Если OpenInventoryEvent будет отменено, это событие так же прервётся.
			event.setCanceled(DemoEventFactory.onPlayerOpenInventory());
		}
	}
	
	@SubscribeEvent
	public static void onLivingFall(LivingFallEvent event) {
		
		if (event.getEntityLiving() instanceof EntityPlayer) {
			
			//Вызов события.
			float distance = DemoEventFactory.onPlayerFall((EntityPlayer) event.getEntityLiving(), event.getDistance());
			
			//Установка дистанции падения.
			event.setDistance(distance);
			
			//Отмена события.
			event.setCanceled(distance == 0.0F);
		}
	}
}
