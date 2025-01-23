package DAO;

import Model.Message;
import java.util.List;


public interface MessageDAO {
    Message createMessage(Message message);
    Message getMessageById(int id);
    List<Message> getAllMessages();
    boolean updateMessage(Message message);
    boolean deleteMessage(int id);
    List<Message> getMessagesByUser(int userId);
}
