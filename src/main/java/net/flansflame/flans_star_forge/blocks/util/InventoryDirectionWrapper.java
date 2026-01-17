package net.flansflame.flans_star_forge.blocks.util;

import net.minecraft.core.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InventoryDirectionWrapper {
    public Map<Direction, LazyOptional<WrappedHandler>> directionMap;

    public InventoryDirectionWrapper(IItemHandlerModifiable handler, InventoryDirectionEntry... entries) {
        this.directionMap = new HashMap<>();
        for (var x : entries) {
            directionMap.put(x.direction, LazyOptional.of(() ->
                    new WrappedHandler(handler, (i) -> Objects.equals(i, x.slotIndex), (i, s) -> x.canInsert)));
        }
    }
}
