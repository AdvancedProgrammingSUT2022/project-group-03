package view;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import controller.ChatController;
import model.ChatPayload;
import network.MySocketHandler;

public class ChatMenu extends Menu {
    private final MySocketHandler mySocketHandler;
    private final ChatController chatController;

    public ChatMenu(MySocketHandler mySocketHandler) {
        super(mySocketHandler);
        this.mySocketHandler = mySocketHandler;
        chatController = new ChatController(mySocketHandler);
    }

    @Override
    protected boolean commands(String command) {
        ChatPayload payload;
        try {
            payload = new Gson().fromJson(command, ChatPayload.class);
        } catch (JsonSyntaxException e) {
            System.out.println("JsonSyntaxException: " + command);
            return false;
        }
        switch (payload.getHeader()) {
            case "menu exit" -> {
                ChatPayload respond = new ChatPayload("ok");
                mySocketHandler.send(new Gson().toJson(respond, ChatPayload.class));
                nextMenu = 1;
                return true;
            }
            case "get all chats" -> chatController.sendAllChatsToClient();
//            case "update chats" -> chatController.updateChats(payload.getChats());
            case "update chat" -> chatController.update(payload.getChat());
            default -> {
                System.out.println("invalid request.");
                ChatPayload respond = new ChatPayload("invalid request");
                mySocketHandler.send(new Gson().toJson(respond));
            }
        }
        //return true if you want exit here...
        return false;
    }
}
