package shedar.mods.ic2.nuclearcontrol.gui.controls;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ic2.core.IC2;
import ic2.core.network.NetworkManager;

import java.lang.reflect.Method;

import ic2.api.network.NetworkHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import shedar.mods.ic2.nuclearcontrol.tileentities.TileEntityThermo;

@SideOnly(Side.CLIENT)
public class GuiThermoInvertRedstone extends GuiButton {

    private static final String TEXTURE_FILE = "nuclearcontrol:textures/gui/GUIThermalMonitor.png";
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TEXTURE_FILE);

    TileEntityThermo thermo;
    private boolean checked;

    public GuiThermoInvertRedstone(int id, int x, int y, TileEntityThermo thermo){
        super(id, x, y, 0, 0, "");
        height = 15;
        width = 51;
        this.thermo = thermo;
        checked = thermo.isInvertRedstone();
    }

    @Override
    public void drawButton(Minecraft minecraft, int par2, int par3) {
        if(this.visible){
            minecraft.renderEngine.bindTexture(TEXTURE_LOCATION);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int delta = checked ? 15 : 0;
            drawTexturedModalRect(xPosition, yPosition + 1, 199, delta, 51, 15);
        }
    }

    @Override
    public int getHoverState(boolean flag) {
        return 0;
    }

    @Override
    public boolean mousePressed(Minecraft minecraft, int i, int j) {
        if(super.mousePressed(minecraft, i, j)){
            checked = !checked;
            int value = checked ? -2 : -1;
            thermo.setInvertRedstone(checked);
            ((NetworkManager)IC2.network.get()).initiateClientTileEntityEvent(thermo, value);
            return true;
        }else
            return false;
    }
}
