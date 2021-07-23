package siongsng.rpmtwupdatemod.crowdin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;
import siongsng.rpmtwupdatemod.config.ConfigScreen;
import siongsng.rpmtwupdatemod.config.Configer;
import siongsng.rpmtwupdatemod.function.ReloadPack;
import siongsng.rpmtwupdatemod.function.SendMsg;
import siongsng.rpmtwupdatemod.gui.*;

public final class key {
    public static final KeyBinding reloadpack = new KeyBinding("key.rpmtw_update_mod.reloadpack", KeyConflictContext.UNIVERSAL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.categories.rpmtw");
    public static final KeyBinding open_config = new KeyBinding("key.rpmtw_update_mod.open_config", KeyConflictContext.UNIVERSAL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_O, "key.categories.rpmtw");
    public static final KeyBinding Crowdin = new KeyBinding("key.rpmtw_update_mod.open_crowdin", GLFW.GLFW_KEY_UNKNOWN, "key.categories.rpmtw");
    public static final KeyBinding cosmic_chat_send = new KeyBinding("key.rpmtw_update_mod.cosmic_chat_send", GLFW.GLFW_KEY_G, "key.categories.rpmtw");

    private boolean showed = false;

    public key() {
        ClientRegistry.registerKeyBinding(reloadpack);
        ClientRegistry.registerKeyBinding(open_config);
        ClientRegistry.registerKeyBinding(Crowdin);
        ClientRegistry.registerKeyBinding(cosmic_chat_send);
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent e) {
        PlayerEntity p = Minecraft.getInstance().player;
        if (showed) { //防止重複開啟
            try {
                if (!reloadpack.isDown() && !open_config.isDown() && !Crowdin.isDown() && !cosmic_chat_send.isDown()) {
                    showed = false;
                }
            } catch (IndexOutOfBoundsException ex) {
                showed = false;
            }
            return;
        }
        if (Crowdin.consumeClick()) {
            assert p != null;
            Item item = p.getMainHandItem().getItem(); //拿的物品
            String item_key = item.getDescriptionId(); //物品的命名空間

            if (item_key.equals("block.minecraft.air")) {
                SendMsg.send("§4請手持物品後再使用此功能。");
                return;
            } else if (!Configer.isCheck.get()) {
                Minecraft.getInstance().setScreen(new CrowdinLoginScreen());
                return;
            } else {
                SendMsg.send("請稍後，正在開啟物品翻譯界面中...");
                Thread thread = new Thread(() -> {
                    if (CorwidnProcedure.getText() == null && Configer.isCheck.get()) {
                        SendMsg.send("§6由於你目前手持想要翻譯的物品，數據不在資料庫內\n因此無法進行翻譯，想了解更多資訊請前往RPMTW官方Discord群組:https://discord.gg/5xApZtgV2u");
                        return;
                    }
                    Minecraft.getInstance().setScreen(new CrowdinScreen());
                });
                thread.start();
            }

        }

        if (open_config.consumeClick()) {
            Minecraft.getInstance().setScreen(new ConfigScreen());
        }
        if (Configer.rpmtw_reloadpack.get()) {
            if (reloadpack.consumeClick()) {
                new ReloadPack();
            }
        }
        if(cosmic_chat_send.consumeClick()){
            if (!Configer.isChat.get()) return;
            if (Configer.isEULA.get()) {
                Minecraft.getInstance().setScreen(new CosmicChatScreen());
            } else {
                Minecraft.getInstance().setScreen(new EULAScreen());
            }
        }
    }
}