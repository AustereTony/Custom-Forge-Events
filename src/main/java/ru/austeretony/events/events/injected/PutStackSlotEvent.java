package ru.austeretony.events.events.injected;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import ru.austeretony.events.coremod.CustomEventsFactory;

public class PutStackSlotEvent extends Event {
	
	/**
	 * PutStackSlotEvent срабатывает когда игрок кладёт предмет в слот.<br>
	 * Срабатывает через: <Pre> {@link CustomEventsFactory#putStackPre(Slot, ItemStack)}<br>
	 * Срабатывает через: <Post> {@link CustomEventsFactory#putStackPost(Slot, ItemStack)}<br>
	 * <br>
	 * {@link #slot} изменяемый слот.<br>
	 * {@link #inventory} инвентарь, которому принадлежит слот<br>
	 * (не тот, в котором находится, а тот, который передан в конструктор).<br>
	 * {@link #slotIndex} индекс слота в инвентаре.<br>
	 * {@link #stackInSlot} предмет в слоте.<br>
	 * {@link #stackToPut} добавляемый предмет.<br>
	 * <br>
	 * Это событие нельзя отменить. {@link Cancelable}. <br>
	 * <br>
	 * Это событие не имеет результата. {@link HasResult}<br>
	 * <br>
	 * Это событие использует {@link MinecraftForge#EVENT_BUS}.<br>
	 **/
	
	private final Slot slot;
	
	private final IInventory inventory;
	
	private final int slotIndex;
	
	private final ItemStack stackInSlot, stackToPut;

	public PutStackSlotEvent(Slot slot, ItemStack itemStack) {
		
		this.slot = slot;
		this.inventory = slot.inventory;
		this.slotIndex = slot.getSlotIndex();
		this.stackInSlot = slot.getStack();
		this.stackToPut = itemStack;
	}
	
	public Slot getSlot() {
		
		return this.slot;
	}
	
	public IInventory getInventory() {
		
		return this.inventory;
	}
	
	public int getSlotIndex() {
		
		return this.slotIndex;
	}
	
	public ItemStack getStackInSlot() {
		
		return this.stackInSlot;
	}
	
	public ItemStack getStackToPut() {
		
		return this.stackToPut;
	}
	
	public static class Pre extends PutStackSlotEvent {

		/**
		 * Срабатывает до внесения изменений.
		 */
		
		public Pre(Slot slot, ItemStack itemStack) {
			
			super(slot, itemStack);
		}		
	}
	
	public static class Post extends PutStackSlotEvent {

		/**
		 * Срабатывает после внесения изменений.
		 */
		
		public Post(Slot slot, ItemStack itemStack) {
			
			super(slot, itemStack);
		}		
	}
}
