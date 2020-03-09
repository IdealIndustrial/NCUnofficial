package shedar.mods.ic2.nuclearcontrol.crossmod.bigreactors;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.ICardWrapper;
import shedar.mods.ic2.nuclearcontrol.api.PanelSetting;
import shedar.mods.ic2.nuclearcontrol.api.PanelString;
import shedar.mods.ic2.nuclearcontrol.items.ItemCardEnergySensorLocation;
import shedar.mods.ic2.nuclearcontrol.utils.LangHelper;
import shedar.mods.ic2.nuclearcontrol.utils.StringUtils;

public class ItemCardRFSensor extends ItemCardEnergySensorLocation{

	public ItemCardRFSensor() {
		this.setTextureName("nuclearcontrol:cardRFReactor");
	}
	
	
	public static final int DISPLAY_ON = 1;
	public static final int DISPLAY_OUTPUT = 2;
	public static final int DISPLAY_ENERGY = 4;
	public static final int DISPLAY_PERCENTAGE = 8;
	public static final int DISPLAY_TEMP = 16;
	public static final UUID CARD_TYPE1 = new UUID(0, 2);
	
	@Override
	public UUID getCardType() {
		return CARD_TYPE1;
	}
	
	@Override
	public CardState update(TileEntity panel, ICardWrapper card, int range) {
		ChunkCoordinates target = card.getTarget();
		if(target == null) return CardState.NO_TARGET;
		//int targetType = card.getInt("targetType");
		TileEntity check = panel.getWorldObj().getTileEntity(target.posX, target.posY, target.posZ);
		if(check instanceof TileEntityBlockFetcher){
			TileEntityBlockFetcher BF = (TileEntityBlockFetcher) check;
			card.setBoolean("Online", BF.isReactorOnline());
			card.setDouble("storedEnergy", (double) BF.getEnergyStored());
			card.setDouble("createdEnergy", (double) BF.getEnergyGenerated());
			card.setInt("Temp", BF.getTemp());
			card.setDouble("FillPercent", (double) BF.getEnergyOutPercent());
			card.setBoolean("isPassive", BF.getReactorController().isPassivelyCooled());
			if(!BF.getReactorController().isPassivelyCooled()){
				if(BF.getReactorController().getCoolantContainer().getVaporType() != null) {
					card.setString("VaporType", BF.getReactorController().getCoolantContainer().getVaporType().getLocalizedName());
					card.setInt("VaporAmount", BF.getReactorController().getCoolantContainer().getVaporAmount());
				}else{
					card.setString("VaporType", "Empty");
					card.setInt("VaporAmount", 0);
				}
				if(BF.getReactorController().getCoolantContainer().getCoolantType() != null) {
					card.setString("CoolantType", new FluidStack(BF.getReactorController().getCoolantContainer().getCoolantType(), 1).getLocalizedName());
					card.setInt("CoolantAmount", BF.getReactorController().getCoolantContainer().getCoolantAmount());
				}else{
					card.setString("CoolantType", "Empty");
					card.setInt("CoolantAmount", 0);
				}
			}
			return CardState.OK;
		}else{
			return CardState.NO_TARGET;
		}
	}

	@Override
	public CardState update(World world, ICardWrapper card, int range){
		ChunkCoordinates target = card.getTarget();
		if(target == null) return CardState.NO_TARGET;
		//int targetType = card.getInt("targetType");
		TileEntity check = world.getTileEntity(target.posX, target.posY, target.posZ);
		if(check instanceof TileEntityBlockFetcher){
			TileEntityBlockFetcher BF = (TileEntityBlockFetcher) check;
			card.setBoolean("Online", BF.isReactorOnline());
			card.setDouble("storedEnergy", (double) BF.getEnergyStored());
			card.setDouble("createdEnergy", (double) BF.getEnergyGenerated());
			card.setInt("Temp", BF.getTemp());
			card.setDouble("FillPercent", (double) BF.getEnergyOutPercent());
			card.setBoolean("isPassive", BF.getReactorController().isPassivelyCooled());
			if(!BF.getReactorController().isPassivelyCooled()){
				if(BF.getReactorController().getCoolantContainer().getVaporType() != null) {
					card.setString("VaporType", BF.getReactorController().getCoolantContainer().getVaporType().getLocalizedName());
					card.setInt("VaporAmount", BF.getReactorController().getCoolantContainer().getVaporAmount());
				}else{
					card.setString("VaporType", "Empty");
					card.setInt("VaporAmount", 0);
				}
				if(BF.getReactorController().getCoolantContainer().getCoolantType() != null) {
					card.setString("CoolantType", new FluidStack(BF.getReactorController().getCoolantContainer().getCoolantType(), 1).getLocalizedName());
					card.setInt("CoolantAmount", BF.getReactorController().getCoolantContainer().getCoolantAmount());
				}else{
					card.setString("CoolantType", "Empty");
					card.setInt("CoolantAmount", 0);
				}
			}
			return CardState.OK;
		}else{
			return CardState.NO_TARGET;
		}
	}
	
	@Override
	public List<PanelString> getStringData(int displaySettings,
			ICardWrapper card, boolean showLabels) {
		List<PanelString> result = new LinkedList<PanelString>();
		PanelString line;

		
		double PerOut = card.getDouble("FillPercent");
		double energyStored = card.getDouble("storedEnergy");
		double outputlvl = card.getDouble("createdEnergy");
		double coreTemp = card.getDouble("Temp");
		//NCLog.error(on);
		//NCLog.error(PerOut);
		//NCLog.error(energyStored);
		int ioutputlvl = (int) outputlvl;
		int ienergyStored = (int) energyStored;
		boolean passive = card.getBoolean("isPassive");
		if(passive) {
			//Temperature
			if ((displaySettings & DISPLAY_TEMP) > 0) {
				line = new PanelString();
				line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelRF.Temp", coreTemp, showLabels);
				result.add(line);
			}

			//Stored Energy
			if ((displaySettings & DISPLAY_ENERGY) > 0) {
				line = new PanelString();
				line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelRF.EnergyStored", ienergyStored, showLabels);
				result.add(line);
			}

			//Energy Created Frequency
			if ((displaySettings & DISPLAY_OUTPUT) > 0) {
				line = new PanelString();
				line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelRF.CreatedEnergy", ioutputlvl, showLabels);
				result.add(line);
			}

			//Output Percentage
			if ((displaySettings & DISPLAY_PERCENTAGE) > 0) {
				line = new PanelString();
				line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelRF.Percentage", PerOut, showLabels);
				result.add(line);
			}

			//On or Off
			int txtColor = 0;
			String text;
			if ((displaySettings & DISPLAY_ON) > 0) {
				boolean reactorPowered = card.getBoolean("Online");
				if (reactorPowered) {
					txtColor = 0x00ff00;
					text = LangHelper.translate("msg.nc.InfoPanelOn");
				} else {
					txtColor = 0xff0000;
					text = LangHelper.translate("msg.nc.InfoPanelOff");
				}
				if (result.size() > 0) {
					PanelString firstLine = result.get(0);
					firstLine.textRight = text;
					firstLine.colorRight = txtColor;
				} else {
					line = new PanelString();
					line.textLeft = text;
					line.colorLeft = txtColor;
					result.add(line);
				}
			}
		}else{
			//Temperature
			if ((displaySettings & DISPLAY_TEMP) > 0) {
				line = new PanelString();
				line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelRF.Temp", coreTemp, showLabels);
				result.add(line);
			}
			//Energy Created Frequency
			if ((displaySettings & DISPLAY_OUTPUT) > 0) {
				line = new PanelString();
				line.textLeft = StringUtils.getFormatted("msg.nc.InfoPanelRF.SteamOutput", ioutputlvl, showLabels);
				result.add(line);
			}
			//Stored Energy
			if ((displaySettings & DISPLAY_ENERGY) > 0) {
				line = new PanelString();
				line.textLeft =  StringUtils.getFormattedKey("msg.nc.InfoPanelRF.CoolantTank", card.getString("CoolantType"), card.getInt("CoolantAmount"));
				result.add(line);
			}
			//Vapor Tank
			if ((displaySettings & DISPLAY_PERCENTAGE) > 0) {
				line = new PanelString();
				line.textLeft = StringUtils.getFormattedKey("msg.nc.InfoPanelRF.OutputTank", card.getString("VaporType") ,card.getInt("VaporAmount"));
				result.add(line);
			}
			//On or Off
			int txtColor = 0;
			String text;
			if ((displaySettings & DISPLAY_ON) > 0) {
				boolean reactorPowered = card.getBoolean("Online");
				if (reactorPowered) {
					txtColor = 0x00ff00;
					text = LangHelper.translate("msg.nc.InfoPanelOn");
				} else {
					txtColor = 0xff0000;
					text = LangHelper.translate("msg.nc.InfoPanelOff");
				}
				if (result.size() > 0) {
					PanelString firstLine = result.get(0);
					firstLine.textRight = text;
					firstLine.colorRight = txtColor;
				} else {
					line = new PanelString();
					line.textLeft = text;
					line.colorLeft = txtColor;
					result.add(line);
				}
			}

		}
		return result;
	}
	
	@Override
	public List<PanelSetting> getSettingsList() {
		List<PanelSetting> result = new ArrayList<PanelSetting>(5);
		result.add(new PanelSetting(LangHelper.translate("1"), DISPLAY_ON, CARD_TYPE));
		result.add(new PanelSetting(LangHelper.translate("2"), DISPLAY_ENERGY, CARD_TYPE));
		result.add(new PanelSetting(LangHelper.translate("3"), DISPLAY_OUTPUT, CARD_TYPE));
		result.add(new PanelSetting(LangHelper.translate("4"), DISPLAY_TEMP, CARD_TYPE));
		result.add(new PanelSetting(LangHelper.translate("5"), DISPLAY_PERCENTAGE, CARD_TYPE));
		return result;
	}
}
