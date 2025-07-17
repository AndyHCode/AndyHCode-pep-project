package Service;
import Model.Message;
import DAO.MessageDAO;
import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    public Message deleteMessageByID(int messageID){
        return messageDAO.deleteMessageByID(messageID);
    }

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message addMessage(Message message){
        return messageDAO.insertMessage(message);
    }
    public Message getMessageByID(int messageID){
        return messageDAO.getMessageByID(messageID);
    }
    
    
}
