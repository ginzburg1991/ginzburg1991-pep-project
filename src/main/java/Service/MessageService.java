package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.List;


public class MessageService {
    private final MessageDAO messageDAO;

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message createMessage(Message message){
        if(message.getMessage_text() == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Invalid");
        }
        return messageDAO.createMessage(message);
    }

    public Message getMessageById(int id){
        return messageDAO.getMessageById(id);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message updateMessage(Message message){
        if(messageDAO.updateMessage(message)){
            return message;
        }
        throw new RuntimeException("Failed to update");
    }

    public boolean deleteMessage(int id){
        return messageDAO.deleteMessage(id);
    }

    public List<Message> getMessagesByUser(int userId){
        return messageDAO.getMessagesByUser(userId);
    }


}
