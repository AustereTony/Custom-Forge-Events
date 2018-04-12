package ru.austeretony.events.main;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ru.austeretony.events.proxy.CommonProxy;
import ru.austeretony.events.test.TestClass;

@Mod(modid = EventsMain.MODID, name = EventsMain.NAME, version = EventsMain.VERSION)
public class EventsMain {
	
    public static final String 
    MODID = "events",
    NAME = "Events",
    VERSION = "1.0";

    private static Logger logger;
    
    //Создание экземпляра трансформируемого класса.
    private TestClass testClass = new TestClass();
    
	@Instance(EventsMain.MODID)
	public static EventsMain instance;	
    
    @SidedProxy(clientSide = "ru.austeretony.events.proxy.ClientProxy", serverSide = "ru.austeretony.events.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	
        this.logger = event.getModLog();
        
        this.proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    	
        this.proxy.init(event);
    }
    
    public static Logger logger() {
    	
    	return logger;
    }
}
