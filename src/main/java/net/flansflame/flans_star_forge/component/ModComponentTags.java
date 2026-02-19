package net.flansflame.flans_star_forge.component;

import net.flansflame.flans_knowledge_lib.component.*;
import net.flansflame.flans_star_forge.FlansStarForge;

public class ModComponentTags {

    public static final TagRegisterer TAGS = new TagRegisterer(FlansStarForge.MOD_ID);

    public static final StringTag OWNER_UUID = TAGS.register(new StringTag("owner_uuid"));
    public static final StringTag NAME = TAGS.register(new StringTag("name"));
    public static final BooleanTag TOGGLE_BLESSING = TAGS.register(new BooleanTag("toggle_blessing"));

    public static final QuintLongTag STORED_ENERGY = TAGS.register(new QuintLongTag("stored_energy"));
    public static final IntegerTag STORED_FLUID_AMOUNT = TAGS.register(new IntegerTag("stored_fluid_amount"));
    public static final StringTag STORED_FLUID_TYPE = TAGS.register(new StringTag("stored_fluid_type"));
}
