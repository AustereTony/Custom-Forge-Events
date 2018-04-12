package ru.austeretony.events.events.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.austeretony.events.events.demo.ItemUsedEvent;
import ru.austeretony.events.events.demo.OpenInventoryEvent;
import ru.austeretony.events.events.demo.PlayerFallEvent;
import ru.austeretony.events.events.injected.InternalControlsEvent;
import ru.austeretony.events.events.injected.InternalControlsEvent.EnumControlType;
import ru.austeretony.events.events.injected.PutStackSlotEvent;
import ru.austeretony.events.events.injected.TakeStackSlotEvent;
import ru.austeretony.events.main.EventsMain;

@EventBusSubscriber(modid = EventsMain.MODID)
public class CustomEvents {

	@SubscribeEvent
	public static void onPlayerUsedItem(ItemUsedEvent event) {		
		
		//Только на сервере, только для игрока и при поедании еды.
		if (!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof EntityPlayer && event.getItemStack().getItem() instanceof ItemFood) {
						
			//Отправка игроку сообщения с названиеим съеденного предмета.
			((EntityPlayer) event.getEntityLiving()).sendMessage(new TextComponentTranslation("event.itemUsed", event.getItemStack().getDisplayName()));
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)	
	public static void onPlayerOpenInventory(OpenInventoryEvent event) {
		
		//Если у игрока занята правая рука, то он не сможет открыть инвентарь.
		if (Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() != Items.AIR) {
						
			//Отмена собственного события.
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void onPlayerFall(PlayerFallEvent event) {
		
		//Уменьшение высоты падения для смягчения урона в два раза.
		event.setDistance(event.getDistance() / 2);
	}
	
	@SubscribeEvent
	public static void onTakeFromSlotPost(TakeStackSlotEvent.Post event) {
		
		if (event.getInventory() instanceof InventoryPlayer && !event.getEntityPlayer().world.isRemote) {		

			if (event.getSlotIndex() == 39 && event.getStackInSlot().getItem() != Items.CHAINMAIL_HELMET) {
			
				//Если после клика в слоте другой предмет - удаление эффекта.	
				event.getEntityPlayer().removePotionEffect(MobEffects.NIGHT_VISION);
			}
			
			else if (event.getSlotIndex() == 38 && event.getStackInSlot().getItem() != Items.DIAMOND_CHESTPLATE) {
				
				//При изъятии удаление эффекта.	
				event.getEntityPlayer().removePotionEffect(MobEffects.REGENERATION);					
			}
		}
	}
	
	@SubscribeEvent
	public static void onPutToSlotPost(PutStackSlotEvent.Post event) {
		
		if (event.getInventory() instanceof InventoryPlayer && !((InventoryPlayer) event.getInventory()).player.world.isRemote) {

			if (event.getSlotIndex() == 39 && event.getStackInSlot().getItem() == Items.CHAINMAIL_HELMET) {
			
				//Если после клика в слоте нужный предмет - добавление эффекта
				((InventoryPlayer) event.getInventory()).player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 24000));//
			}
			
			else if (event.getSlotIndex() == 39 && event.getStackInSlot().getItem() != Items.CHAINMAIL_HELMET) {
				
				//Если после клика в слоте другой предмет - удаление эффекта
				((InventoryPlayer) event.getInventory()).player.removePotionEffect(MobEffects.NIGHT_VISION);
			}
			
			if (event.getSlotIndex() == 38 && event.getStackInSlot().getItem() == Items.DIAMOND_CHESTPLATE) {
				
				//Если после клика в слоте нужный предмет - добавление эффекта
				((InventoryPlayer) event.getInventory()).player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 24000));								
			}
			
			else if (event.getSlotIndex() == 38 && event.getStackInSlot().getItem() != Items.DIAMOND_CHESTPLATE) {
				
				//Если после клика в слоте другой предмет - удаление эффекта
				((InventoryPlayer) event.getInventory()).player.removePotionEffect(MobEffects.REGENERATION);						
			}
		}
	}
	
	@SubscribeEvent	
	public static void onDebugMenu(InternalControlsEvent event) {
		
		//Отмена открытия меню отладки и всего что с ним связано.
		if (event.getControl() == EnumControlType.DEBUG) {
						
		    event.setCanceled(true);
		}
	}
}
