package siongsng.rpmtwupdatemod.Proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.LanguageDefinition;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Language;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;

@Environment(EnvType.CLIENT)
public class LangManergerProxy extends LanguageManager {
	private static LangManergerProxy proxy;
	  
	private static LanguageManager origLangMgr;
	  
	public static boolean sameLang;
	
	public LangManergerProxy(String languageCode) {
		super(languageCode);
	}

	public static void init() {
		MinecraftClient mc = MinecraftClient.getInstance();
	    origLangMgr = mc.getLanguageManager();
	    proxy = new LangManergerProxy(mc.options.language);
	    ReloadableResourceManager resmgr = (ReloadableResourceManager)mc.getResourceManager();
	    resmgr.registerReloader((ResourceReloader)proxy);
	  }
	  
	  public void method_14491(ResourceManager resourceManager) {
	    String currentLangCode = (MinecraftClient.getInstance()).options.language;
	    String additionalLangCode = RPMTWConfig.config.additionalLanguage;
	    LanguageDefinition lang = origLangMgr.getLanguage(additionalLangCode);
	   
	    if (lang == null) {
	    	RpmtwUpdateMod.LOGGER.warn("additionalLanguage 不存在(" + RPMTWConfig.config.additionalLanguage + "). additionalLanguage is set to en_us.");
	      additionalLangCode = "en_us";
	    } 
	    if (additionalLangCode.equals(currentLangCode)) {
	      sameLang = true;
	    } else {
	      sameLang = false;
	      LanguageDefinition language = origLangMgr.getLanguage("en_us");
	      List<LanguageDefinition> list = new ArrayList<LanguageDefinition>();
	      list.add(language);
	      if (!"en_us".equals(additionalLangCode)) {
	        language = origLangMgr.getLanguage(additionalLangCode);
	        list.add(language);
	      } 
	      TranslationStorage clientLanguageMapUs = TranslationStorage.load(resourceManager, list);
	     // LangMapProxy.applyNewProxy((Language)clientLanguageMapUs);
	    } 
	  }
	  
	  public void setLanguage(LanguageDefinition currentLanguageIn) {
	    origLangMgr.setLanguage(currentLanguageIn);
	  }
	  
	  public LanguageDefinition getLanguage() {
	    return origLangMgr.getLanguage();
	  }
	  
	  public SortedSet<LanguageDefinition> getAllLanguages() {
	    return origLangMgr.getAllLanguages();
	  }
	  
	  public LanguageDefinition getLanguage(String str) {
	    return origLangMgr.getLanguage(str);
	  }
}
