package siongsng.rpmtwupdatemod.CosmicChat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import siongsng.rpmtwupdatemod.RpmtwUpdateMod;
import siongsng.rpmtwupdatemod.config.RPMTWConfig;
import siongsng.rpmtwupdatemod.function.SendMsg;

public class GetMessage {
    public GetMessage() {
        new SocketClient().getSocket().connect().on(("broadcast"), (data) -> {
            try {
                if (Minecraft.getInstance().player == null) return;
                if (!RPMTWConfig.isChat.get()) return;

                JsonParser jp = new JsonParser();
                JsonObject JsonData = (JsonObject) jp.parse(data[0].toString());
                String Type = JsonData.getAsJsonPrimitive("Type").getAsString();
                String UserName = JsonData.getAsJsonPrimitive("UserName").getAsString();
                String IP = JsonData.getAsJsonPrimitive("IP").getAsString();
                String Message = JsonData.getAsJsonPrimitive("Message").getAsString().replaceAll("\\\"", "\"");

                switch (Type) {
                    case "Client":
                        if (UserName.equals("菘菘#8663") || UserName.equals("SiongSng")) {
                            UserName = "§bRPMTW維護者";
                        }
                        SendMsg.send(String.format(("§9[宇宙通訊] §e<§6%s§e> §f%s"), UserName, Message));
                    case "Server":
                        Minecraft mc = Minecraft.getInstance();
                        Player player = mc.player;
                        assert player != null;
                        if (new GetIP().Get().equals(IP) && Message.equals("Ban")) {
                            SendMsg.send("由於您違反了 《RPMTW 宇宙通訊系統終端使用者授權合約》，因此無法發送訊息至宇宙通訊，如認為有誤判請至我們的Discord群組。");
                        }
                }

            } catch (Exception err) {
                RpmtwUpdateMod.LOGGER.warn("接收宇宙通訊訊息時發生未知錯誤，原因: " + err);
            }
        });
    }
}