package siongsng.rpmtwupdatemod.notice;

import net.minecraft.entity.Entity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import siongsng.rpmtwupdatemod.function.CheckModVersion;
import siongsng.rpmtwupdatemod.function.SendMsg;

public class notice {
    @SubscribeEvent
    public void send(PlayerEvent.PlayerLoggedInEvent e) {
        Entity p = e.getPlayer();
        assert p != null;
        p.sendMessage(new TranslationTextComponent(
                "gui.notice"), p.getUniqueID());
        if (!new CheckModVersion().get()) {
            SendMsg.send("偵測到您目前的§c §eRPMTW繁中化自動更新模組版本過舊§c\n建議您更新版本，以獲得最佳體驗。\n目前版本: " + new CheckModVersion().ver + " 最新版本: " + new CheckModVersion().NewVer() + "\n下載連結:https://bit.ly/33MpXu8");
        }
    }
}
