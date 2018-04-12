package ru.austeretony.events.coremod;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions({"ru.austeretony.events.coremod"})
@MCVersion("1.12.2")
public class FMLCustomEventsPlugin implements IFMLLoadingPlugin {
		
    @Override
    public String[] getASMTransformerClass() {
    	
    	//Передача полного имени класса-трансформера.
        return new String[] {"ru.austeretony.events.coremod.CustomEventsClassTransformer"};
    }

    @Override
    public String getModContainerClass() {
    	
        return null;
    }

    @Override
    public String getSetupClass() {
    	
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
    	
        return null;
    }
}
