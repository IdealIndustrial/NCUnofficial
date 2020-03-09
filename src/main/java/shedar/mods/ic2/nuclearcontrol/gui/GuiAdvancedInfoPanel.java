package shedar.mods.ic2.nuclearcontrol.gui;

import ic2.api.network.NetworkHelper;
import ic2.core.IC2;
import ic2.core.network.NetworkManager;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import shedar.mods.ic2.nuclearcontrol.IC2NuclearControl;
import shedar.mods.ic2.nuclearcontrol.api.IAdvancedCardSettings;
import shedar.mods.ic2.nuclearcontrol.api.ICardGui;
import shedar.mods.ic2.nuclearcontrol.api.ICardSettingsWrapper;
import shedar.mods.ic2.nuclearcontrol.api.ICardWrapper;
import shedar.mods.ic2.nuclearcontrol.api.IPanelDataSource;
import shedar.mods.ic2.nuclearcontrol.api.IPanelMultiCard;
import shedar.mods.ic2.nuclearcontrol.api.PanelSetting;
import shedar.mods.ic2.nuclearcontrol.gui.controls.GuiInfoPanelCheckBox;
import shedar.mods.ic2.nuclearcontrol.gui.controls.IconButton;
import shedar.mods.ic2.nuclearcontrol.panel.CardSettingsWrapperImpl;
import shedar.mods.ic2.nuclearcontrol.panel.CardWrapperImpl;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityAdvancedInfoPanel;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAdvancedInfoPanel extends GuiInfoPanel{
	private static final String TEXTURE_FILE = "nuclearcontrol:textures/gui/GUIAdvancedInfoPanel.png";
	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);

	private static final int ID_LABELS = 1;
	private static final int ID_SLOPE = 2;
	private static final int ID_COLORS = 3;
	private static final int ID_POWER = 4;
	private static final int ID_SETTINGS = 5;

	private byte activeTab;
	private boolean initialized;

	public GuiAdvancedInfoPanel(Container container){
		super(container);
		ySize = 212;
		activeTab = 0;
		initialized = false;
		name = StatCollector.translateToLocal("tile.blockAdvancedInfoPanel.name");
		isColored = this.container.panel.getColored();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3){
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(TEXTURE_LOCATION);
		int left = (width - xSize) / 2;
		int top = (height - ySize) / 2;
		drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
		drawTexturedModalRect(left + 24, top + 62 + activeTab * 14, 182, 0, 1, 15);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initControls()
	{
		ItemStack card = getActiveCard(); 
		if ((card == null && prevCard == null && initialized) || (card!=null  && card.equals(prevCard)))
			return;
		initialized = true;
		int h = fontRendererObj.FONT_HEIGHT + 1;
		buttonList.clear();
		prevCard = card;

		//labels
		buttonList.add(new IconButton(ID_LABELS, guiLeft + 83 , guiTop + 42, 16, 16, TEXTURE_LOCATION, 192-16, getIconLabelsTopOffset(container.panel.getShowLabels())));
		//slope
		buttonList.add(new IconButton(ID_SLOPE, guiLeft + 83 + 17*1, guiTop + 42, 16, 16, TEXTURE_LOCATION, 192, 15));
		//colors
		buttonList.add(new IconButton(ID_COLORS, guiLeft + 83 + 17*2, guiTop + 42, 16, 16, TEXTURE_LOCATION, 192, 15 + 16));
		//power
		buttonList.add(new IconButton(ID_POWER, guiLeft + 83 + 17*3, guiTop + 42, 16, 16, TEXTURE_LOCATION, 192-16, 
				getIconPowerTopOffset(((TileEntityAdvancedInfoPanel)container.panel).getPowerMode())));

		if (card != null && card.getItem() instanceof IPanelDataSource)
		{
			byte slot = container.panel.getIndexOfCard(card);
			IPanelDataSource source = (IPanelDataSource)card.getItem();
			if (source instanceof IAdvancedCardSettings)
			{
				//settings
				buttonList.add(new IconButton(ID_SETTINGS, guiLeft + 83 + 17*4, guiTop + 42, 16, 16, TEXTURE_LOCATION, 192, 15 + 16*2));
			}
			List<PanelSetting> settingsList = null;
			if (card.getItem() instanceof IPanelMultiCard)
			{
				settingsList = ((IPanelMultiCard)source).getSettingsList(new CardWrapperImpl(card, activeTab));
			}
			else
			{
				settingsList = source.getSettingsList();
			}
            int hy = fontRendererObj.FONT_HEIGHT + 1;
            int y=1;
			int x = guiLeft+24;
			int hpos = guiTop+55;
            if (settingsList != null)
				for (PanelSetting panelSetting : settingsList) {
					if (y<=6) {
						buttonList.add(new GuiInfoPanelCheckBox(0, x + 4,   hpos + hy * y, panelSetting, container.panel, slot, fontRendererObj));
					} else if (y>=7 && y<=12) {
						buttonList.add(new GuiInfoPanelCheckBox(0, x + 22,  hpos - 6*hy + hy * y, panelSetting, container.panel, slot, fontRendererObj));
					} else if (y>=13 && y<=18) {
						buttonList.add(new GuiInfoPanelCheckBox(0, x + 44,  hpos - 12*hy + hy * y, panelSetting, container.panel, slot, fontRendererObj));
					} else if (y>=19 && y<=24) {
						buttonList.add(new GuiInfoPanelCheckBox(0, x + 68,  hpos - 18*hy + hy * y, panelSetting, container.panel, slot, fontRendererObj));
					} else if (y>=25 && y<=32) {
						buttonList.add(new GuiInfoPanelCheckBox(0, x + 92,  hpos - 24*hy + hy * y, panelSetting, container.panel, slot, fontRendererObj));
					} else if (y>=31 && y<=38) {
						buttonList.add(new GuiInfoPanelCheckBox(0, x + 114, hpos - 32*hy + hy * y, panelSetting, container.panel, slot, fontRendererObj));
					} else if (y>=37 && y<=44) {
						buttonList.add(new GuiInfoPanelCheckBox(0, x + 136, hpos - 38*hy + hy * y, panelSetting, container.panel, slot, fontRendererObj));
					} else {
						buttonList.add(new GuiInfoPanelCheckBox(0, x + 158, hpos - 44*hy + hy * y, panelSetting, container.panel, slot, fontRendererObj));
					}

					y++;
				}
			if (!modified)
			{
				textboxTitle = new GuiTextField(fontRendererObj, 7, 16, 162, 18);
				textboxTitle.setFocused(true);
				textboxTitle.setText(new CardWrapperImpl(card, activeTab).getTitle());
			}
		}
		else
		{
			modified = false;
			textboxTitle = null;
		}
	}

	@Override
	protected ItemStack getActiveCard()
	{
		return container.panel.getCards().get(activeTab);
	}

	@Override
	public void setWorldAndResolution(net.minecraft.client.Minecraft par1Minecraft, int par2, int par3) 
	{
		initialized = false;
		super.setWorldAndResolution(par1Minecraft, par2, par3);
	}

	private int getIconLabelsTopOffset(boolean checked)
	{
		return checked?15:31;
	}

	private int getIconPowerTopOffset(byte mode)
	{
		switch (mode)
		{
		case TileEntityAdvancedInfoPanel.POWER_REDSTONE:
			return 15 + 16*2;
		case TileEntityAdvancedInfoPanel.POWER_INVERTED:
			return 15 + 16*3;
		case TileEntityAdvancedInfoPanel.POWER_ON:
			return 15 + 16*4;
		case TileEntityAdvancedInfoPanel.POWER_OFF:
			return 15 + 16*5;
		}
		return 15 + 16*2;
	}

	@Override
	protected void actionPerformed(GuiButton button){
		switch(button.id){
		case ID_COLORS:
			GuiScreen colorGui = new GuiScreenColor(this, container.panel);
			mc.displayGuiScreen(colorGui);
			break;
		case ID_SETTINGS:
			ItemStack card = getActiveCard();
			if (card == null) return;
			if (card != null && card.getItem() instanceof IAdvancedCardSettings){
				ICardWrapper helper = new CardWrapperImpl(card, activeTab);
				Object guiObject = ((IAdvancedCardSettings)card.getItem()).getSettingsScreen(helper);
				if (!(guiObject instanceof GuiScreen)){
					IC2NuclearControl.logger.warn("Invalid card, getSettingsScreen method should return GuiScreen object");
					return;
				}
				GuiScreen gui = (GuiScreen)guiObject;
				ICardSettingsWrapper wrapper = new CardSettingsWrapperImpl(card, container.panel, this, activeTab);
				((ICardGui)gui).setCardSettingsHelper(wrapper);
				mc.displayGuiScreen(gui);
			}
			break;
		case ID_LABELS:
			boolean checked = !container.panel.getShowLabels();
			if (button instanceof IconButton){
				IconButton iButton = (IconButton)button;
				iButton.textureTop = getIconLabelsTopOffset(checked);
			}
			int value = checked ? -1 : -2;
			container.panel.setShowLabels(checked);
			((NetworkManager)IC2.network.get()).initiateClientTileEntityEvent(container.panel, value);
			break;
		case ID_POWER:
			byte mode = ((TileEntityAdvancedInfoPanel)container.panel).getNextPowerMode();
			if(button instanceof IconButton){
				IconButton iButton = (IconButton)button;
				iButton.textureTop = getIconPowerTopOffset(mode);
			}
			((NetworkManager)IC2.network.get()).initiateClientTileEntityEvent(container.panel, mode);
			break;
		case ID_SLOPE:
			GuiPanelSlope slopeGui = new GuiPanelSlope(this, (TileEntityAdvancedInfoPanel)container.panel);
			mc.displayGuiScreen(slopeGui);
			break;
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int par3)
	{
		super.mouseClicked(x, y, par3);
		if (x >= guiLeft+7 && x <= guiLeft + 24 && y >= guiTop + 62 && y <= guiTop + 104)
		{
			byte newTab = (byte) ((y - guiTop - 62) / 14);
			if (newTab > 2)
				newTab = 2;
			if (newTab != activeTab && modified)
				updateTitle();
			activeTab = newTab;

		}
	}
}