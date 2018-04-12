package ru.austeretony.events.events.demo;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class DemoEventFactory {

	/*
	 * Статические методы для внедрения в стандартные события для демонстрации.
	 */
	
	//Для внедрения в DemoEventsInjection#onLivingUseItem()
	public static void onLivingFinishesUsingItem(EntityLivingBase livingBase, ItemStack itemStack) {
		
		//Вызов события.
		MinecraftForge.EVENT_BUS.post(new ItemUsedEvent(livingBase, itemStack));
	}
	
	//Для внедрения в DemoEventsInjection#onInventoryOpen()
	public static boolean onPlayerOpenInventory() {
		
		//Вернёт true при отмене события OpenInventoryEvent.
		return MinecraftForge.EVENT_BUS.post(new OpenInventoryEvent());
	}
	
	public static float onPlayerFall(EntityPlayer player, float distance) {
		
		PlayerFallEvent event = new PlayerFallEvent(player, distance);
		
		//При отмене вернёт нулевую дистанцию падения, иначе возможно изменённую.
		return MinecraftForge.EVENT_BUS.post(event) ? 0.0F : event.getDistance();
	}
}
