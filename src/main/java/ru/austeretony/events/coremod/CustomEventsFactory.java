package ru.austeretony.events.coremod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import ru.austeretony.events.events.injected.InternalControlsEvent;
import ru.austeretony.events.events.injected.PutStackSlotEvent;
import ru.austeretony.events.events.injected.TakeStackSlotEvent;
import ru.austeretony.events.events.injected.InternalControlsEvent.EnumControlType;

public class CustomEventsFactory {
	
	//Для внедрения в класс Minecraft в метод runTickKeyboard().
	public static boolean onGUIHide() {
				
		return !MinecraftForge.EVENT_BUS.post(new InternalControlsEvent(EnumControlType.HIDE_GUI));//Вызов срабатывания события типа HIDE_GUI (F1)
	}

	//Для внедрения в класс Minecraft в метод processKeyF3().
	public static boolean onDebugMenuOpen() {
				
		return MinecraftForge.EVENT_BUS.post(new InternalControlsEvent(EnumControlType.DEBUG));//Вызов срабатывания события типа DEBUG (F3)
	}
	
	//Для внедрения в класс Slot в начало onTake().
	public static void onTakePre(Slot slot, EntityPlayer player, ItemStack itemStack) {
		
		//Требуется отсечение клиентских срабатываний, так как сервер постоянно синхронизирует инвентари и на клиенте вызов будет происходить 
		//очень часто "ложно" (брать и пихать стаки будет сервер при синхронизации, а не игрок).			
		MinecraftForge.EVENT_BUS.post(new TakeStackSlotEvent.Pre(slot, player, itemStack));//Вызов срабатывания события фазы Pre
	}
	
	//Для внедрения в класс Slot в конец onTake() перед return.
	public static void onTakePost(Slot slot, EntityPlayer player, ItemStack itemStack) {
		
		MinecraftForge.EVENT_BUS.post(new TakeStackSlotEvent.Post(slot, player, itemStack));//Вызов срабатывания события фазы Post
	}
	
	//Для внедрения в класс Slot в начало putStack().
	public static void putStackPre(Slot slot, ItemStack itemStack) {
		
		if (itemStack != itemStack.EMPTY && itemStack.getItem() != Items.AIR) {
			
			MinecraftForge.EVENT_BUS.post(new PutStackSlotEvent.Pre(slot, itemStack));
		}
	}
	
	//Для внедрения в класс Slot в конец putStack().
	public static void putStackPost(Slot slot, ItemStack itemStack) {
		
		if (itemStack != itemStack.EMPTY && itemStack.getItem() != Items.AIR) {

			MinecraftForge.EVENT_BUS.post(new PutStackSlotEvent.Post(slot, itemStack));
		}
	}
}
