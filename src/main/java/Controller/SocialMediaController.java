package Controller;

import static org.mockito.ArgumentMatchers.matches;

import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::postRegister);
        app.post("/login", this::postLogin);
        app.post("/messages", this::postMessages);

        app.get("/messages/{message_id}", this::getMessageByID);
        app.get("/messages", this::getAllMessages);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountID);
        
        app.delete("/messages/{message_id}", this::deleteMessageByID);
        //app.patch("/messages/{message_id}", this::patchMessageByID);




        return app;
    }

    private void getAllMessagesByAccountID (Context ctx){
        int accountID = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByAccountID(accountID);
        ctx.json(messages);
    }

    private void deleteMessageByID(Context ctx){
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessageByID(messageID);
        if (message != null) {
            ctx.json(message);
        } else {
            ctx.json("");
        }
    }

    private void getAllMessages(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessageByID(Context ctx) throws JsonProcessingException {
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageByID(messageID);
        if (message != null) {
            ctx.json(message);
        } else {
            ctx.json("");
        }
    }

    private void postMessages(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        if (message.getMessage_text() == "" || message.getMessage_text().length() > 255) {
            ctx.status(400);
            return;
        }

        Message addedMessage = messageService.addMessage(message);

        if (addedMessage != null) {
            ctx.json(mapper.writeValueAsString(addedMessage));
            return;
        } else {
            ctx.status(400);
        }
    }

    private void postLogin(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        if (account.getUsername() == "" || account.getPassword() == "") {
            ctx.status(401);
            return;
        }
        Account loginUser = accountService.loginAccount(account);

        if (loginUser != null) {
            ctx.json(mapper.writeValueAsString(loginUser));
            return;
        }
        ctx.status(401);
    }

    private void postRegister(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        if (account.getPassword().length() < 4) {
            ctx.status(400);
            return;
        }
        if (account.getUsername() == "") {
            ctx.status(400);
            return;
        }

        // register user here

        Account addedAccount = accountService.addAccount(account);
        //
        if (addedAccount != null) {
            ctx.json(mapper.writeValueAsString(addedAccount));
        } else {
            ctx.status(400);
        }

    }
}