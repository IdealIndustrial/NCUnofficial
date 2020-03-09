package shedar.mods.ic2.nuclearcontrol.crossmod.waila;

import java.util.List;

import shedar.mods.ic2.nuclearcontrol.api.BonyDebugger;
import shedar.mods.ic2.nuclearcontrol.tileentities.*;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class CrossWaila {
	
	public static void callbackRegister(IWailaRegistrar register){
		register.registerBodyProvider(new ThermoProvider(), TileEntityThermo.class);
		register.registerBodyProvider(new InfoPanelProvider(), TileEntityInfoPanel.class);
		register.registerBodyProvider(new HowlerAlarmProvider(), TileEntityHowlerAlarm.class);
		register.registerBodyProvider(new EnergyCounterProvider(), TileEntityEnergyCounter.class);
		register.registerBodyProvider(new AverageCounterProvider(), TileEntityAverageCounter.class);
	}

}
