package com.simibubi.create.content.schematics.block;

import com.simibubi.create.AllContainerTypes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

public class SchematicannonContainer extends AbstractContainerMenu {

	private SchematicannonTileEntity te;
	private Player player;

	public SchematicannonContainer(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf buffer) {
		super(type, id);
		player = inv.player;
		ClientLevel world = Minecraft.getInstance().level;
		BlockEntity tileEntity = world.getBlockEntity(buffer.readBlockPos());
		if (tileEntity instanceof SchematicannonTileEntity) {
			this.te = (SchematicannonTileEntity) tileEntity;
			this.te.handleUpdateTag(buffer.readNbt());
			init();
		}
	}

	public SchematicannonContainer(MenuType<?> type, int id, Inventory inv, SchematicannonTileEntity te) {
		super(type, id);
		player = inv.player;
		this.te = te;
		init();
	}

	public static SchematicannonContainer create(int id, Inventory inv, SchematicannonTileEntity te) {
		return new SchematicannonContainer(AllContainerTypes.SCHEMATICANNON.get(), id, inv, te);
	}

	protected void init() {
		int x = 0;
		int y = 0;

		addSlot(new SlotItemHandler(te.inventory, 0, x + 15, y + 65));
		addSlot(new SlotItemHandler(te.inventory, 1, x + 171, y + 65));
		addSlot(new SlotItemHandler(te.inventory, 2, x + 134, y + 19));
		addSlot(new SlotItemHandler(te.inventory, 3, x + 174, y + 19));
		addSlot(new SlotItemHandler(te.inventory, 4, x + 15, y + 19));

		int invX = 37;
		int invY = 161;

		// player Slots
		for (int row = 0; row < 3; ++row) 
			for (int col = 0; col < 9; ++col) 
				addSlot(new Slot(player.getInventory(), col + row * 9 + 9, invX + col * 18, invY + row * 18));
		for (int hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot)
			addSlot(new Slot(player.getInventory(), hotbarSlot, invX + hotbarSlot * 18, invY + 58));

		broadcastChanges();
	}

	@Override
	public boolean stillValid(Player player) {
		return te != null && te.canPlayerUse(player);
	}

	@Override
	public void removed(Player playerIn) {
		super.removed(playerIn);
	}

	public SchematicannonTileEntity getTileEntity() {
		return te;
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		Slot clickedSlot = getSlot(index);
		if (!clickedSlot.hasItem())
			return ItemStack.EMPTY;
		ItemStack stack = clickedSlot.getItem();

		if (index < 5) {
			moveItemStackTo(stack, 5, slots.size(), false);
		} else {
			if (moveItemStackTo(stack, 0, 1, false) || moveItemStackTo(stack, 2, 3, false)
					|| moveItemStackTo(stack, 4, 5, false))
				;
		}

		return ItemStack.EMPTY;
	}

}
