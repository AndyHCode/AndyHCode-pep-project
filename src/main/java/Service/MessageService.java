package Service;
import Model.Message;
import DAO.MessageDAO;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public Message addMessage(Message message){
        return messageDAO.insertMessage(message);
    }
    public Message getMessage(int messageID){
        return messageDAO.getMessage(messageID);
    }
    
    
}
