package ru.austeretony.events.events.injected;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import ru.austeretony.events.coremod.CustomEventsFactory;

public class TakeStackSlotEvent extends Event {
	
	/**
	 * TakeStackSlotEvent срабатывает когда игрок забирает предмет из слота.<br>
	 * Срабатывает через: <Pre> {@link CustomEventsFactory#onTakePre(Slot, ItemStack)}<br>
	 * Срабатывает через: <Post> {@link CustomEventsFactory#onTakePost(Slot, ItemStack)}<br>
	 * <br>
	 * {@link #slot} изменяемый слот.<br>
	 * {@link #inventory} инвентарь, которому принадлежит слот<br>
	 * (не тот, в котором находится, а тот, который передан в конструктор).<br>
	 * {@link #player} игрок, взаимодействующий со слотом.<br>
	 * {@link #slotIndex} индекс слота в инвентаре.<br>
	 * {@link #stackInSlot} предмет в слоте.<br>
	 * {@link #mouseStack} предмет, над которым курсор.<br>
	 * <br>
	 * Это событие нельзя отменить. {@link Cancelable}. <br>
	 * <br>
	 * Это событие не имеет результата. {@link HasResult}<br>
	 * <br>
	 * Это событие использует {@link MinecraftForge#EVENT_BUS}.<br>
	 **/
	
	private final Slot slot;
	
	private final IInventory inventory;
	
	private final EntityPlayer player;
	
	private final int slotIndex;
	
	private final ItemStack stackInSlot, mouseStack;

	public TakeStackSlotEvent(Slot slot, EntityPlayer player, ItemStack itemStack) {
		
		this.slot = slot;
		this.player = player;
		this.inventory = slot.inventory;
		this.slotIndex = slot.getSlotIndex();
		this.stackInSlot = slot.getStack();
		this.mouseStack = itemStack;
	}
	
	public Slot getSlot() {
		
		return this.slot;
	}
	
	public IInventory getInventory() {
		
		return this.inventory;
	}
	
	public EntityPlayer getEntityPlayer() {
		
		return this.player;
	}
	
	public int getSlotIndex() {
		
		return this.slotIndex;
	}
	
	public ItemStack getStackInSlot() {
		
		return this.stackInSlot;
	}
	
	public ItemStack getMouseHeldStack() {
		
		return this.mouseStack;
	}
	
	public static class Pre extends TakeStackSlotEvent {

		/**
		 * Срабатывает до внесения изменений.
		 */
		
		public Pre(Slot slot, EntityPlayer player, ItemStack itemStack) {
			
			super(slot, player, itemStack);
		}		
	}
	
	public static class Post extends TakeStackSlotEvent {

		/**
		 * Срабатывает после внесения изменений.
		 */
		
		public Post(Slot slot, EntityPlayer player, ItemStack itemStack) {
			
			super(slot, player, itemStack);
		}		
	}
}
