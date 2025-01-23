package Controller;

import Model.Account;
import Model.Message;
import DAO.AccountDAOImpl;
import DAO.MessageDAOImpl;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    private final AccountService accountService;
    private final MessageService messageService;
     

    public SocialMediaController() {
        AccountDAOImpl accountDAO = new AccountDAOImpl();
        MessageDAOImpl messageDAO = new MessageDAOImpl();

        this.accountService = new AccountService(accountDAO);
        this.messageService = new MessageService(messageDAO);
    }

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.get("/accounts", this::getAllAccounts);
        app.post("/accounts", this::createAccount);
        app.get("/messages", this::getAllMessages);
        app.get("/accounts/{id}/messages", this::getMessagesByUser);
        app.post("/messages", this::createMessage);
        app.delete("/messages/{id}",this::deleteMessage);
        app.get("/messages/{id}", this::getMessageById);
        app.patch("/messages/{id}", this::updateMessage);
        app.post("/login", this::login);
        app.post("/register", this::registerUser);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllAccounts(Context content) {
        List<Account> accounts = accountService.getAllAccounts();
        content.json(accounts);
    }

    private void createAccount(Context content){
        Account account = content.bodyAsClass(Account.class);
        Account createAccount = accountService.createAccount(account);
        content.json(createAccount);
    }

    private void getAllMessages(Context content){
        List<Message> messages = messageService.getAllMessages();
        content.json(messages);
    }

    private void createMessage(Context content){
        try{
        Message message = content.bodyAsClass(Message.class);


        if(message.getMessage_text() == null || message.getMessage_text().isBlank() || message.getMessage_text().length() > 255 || accountService.getAccountById((message.getPosted_by())) == null){
            content.status(400);
            return;
        }

        Message createdtMessage = messageService.createMessage(message);
        content.json(createdtMessage);

        } catch(Exception e){
            content.status(500);
            content.result("error has occured");
        }
    }

    private void deleteMessage(Context content){
        try{
        int messageId = Integer.parseInt(content.pathParam("id"));

        Message messageToDelete = messageService.getMessageById(messageId);
            if(messageToDelete == null){
                content.status(200);
                return;
            }

        boolean isDeleted = messageService.deleteMessage(messageId);

            if(isDeleted){
                content.status(200);
                content.json(messageToDelete);
            }else{
                content.status(500);
                content.result("Failed to delete");
        }

        } catch(NumberFormatException e){
        content.status(400);
        content.result("error has occured");
        } catch(Exception e){
            content.status(500);
            content.result("error has occured");
        }
    }

    private void getMessagesByUser(Context content){
        try{
            int account_id = Integer.parseInt(content.pathParam("id"));
            List<Message> messages = messageService.getMessagesByUser(account_id);
            content.status(200);
            content.json(messages);

        }catch(NumberFormatException e){
            content.status(400);
            content.result("Invalid account");
        }catch(Exception e){
            content.status(500);
            content.result("error occurred");
        }
        
    }

    private void getMessageById(Context content){
        try{
            int message_id = Integer.parseInt(content.pathParam("id"));

            Message message = messageService.getMessageById(message_id);

            if(message == null){
                content.status(200);
                return;
            }

            content.status(200);
            content.json(message);
        }catch(NumberFormatException e){
            content.status(400);
            content.result("Wrong message ID");
        } catch(Exception e){
            content.status(500);
            content.result("error occurred");
        }
    }

    private void updateMessage(Context content){
        try{
            int message_id = Integer.parseInt(content.pathParam("id"));

            Message existingMessage = messageService.getMessageById(message_id);
            Message updateMessage = content.bodyAsClass(Message.class);
            String newMessageText = updateMessage.getMessage_text();

            if(existingMessage == null || newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255){
                content.status(400);
                return;
            }

            existingMessage.setMessage_text(newMessageText);
            Message resultMessage = messageService.updateMessage(existingMessage);

            content.status(200);
            content.json(resultMessage);
        }catch(NumberFormatException e){
            content.status(400);
        }catch(Exception e){
            content.status(500);
            content.result("error occurred");
        }
    }

    private void login(Context content){
        try{
            Account loginDetails = content.bodyAsClass(Account.class);

            Account account = accountService.validateLogin(loginDetails.getUsername(), loginDetails.getPassword());

            if(account == null){
                content.status(401);
                return;
            }

            content.status(200);
            content.json(account);
        } catch (Exception e){
            content.status(500);
            content.result("error occurred");
        }
    }

    private void registerUser(Context content){
        try{
            Account newAccount = content.bodyAsClass(Account.class);
            if(newAccount.getUsername() == null || newAccount.getUsername().isBlank() || newAccount.getPassword() == null || newAccount.getPassword().length() < 4 || accountService.getAccountByUsername(newAccount.getUsername()) != null){
                System.out.println("failed");
                content.status(400);
                return;
            }
            
            Account createdAccount = accountService.createAccount(newAccount);
            content.status(200);
            content.json(createdAccount);
        }catch(Exception e){
            e.printStackTrace();
            content.status(500);
            content.result("error occurred");
        }
    }

}