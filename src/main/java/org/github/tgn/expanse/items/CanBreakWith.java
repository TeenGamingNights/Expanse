package org.github.tgn.expanse.items;

import org.bukkit.event.block.BlockBreakEvent;

public interface CanBreakWith {
	void destroy(BlockBreakEvent event);
}
