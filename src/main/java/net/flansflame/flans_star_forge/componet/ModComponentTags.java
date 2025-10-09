package net.flansflame.flans_star_forge.componet;

import net.flansflame.flans_knowledge_lib.component.StringTag;
import net.flansflame.flans_knowledge_lib.component.TagRegisterer;
import net.flansflame.flans_star_forge.FlansStarForge;

public class ModComponentTags {

    public static final TagRegisterer TAGS = new TagRegisterer(FlansStarForge.MOD_ID);

    public static final StringTag OWNER_UUID = TAGS.register(new StringTag("owner_uuid"));
}
