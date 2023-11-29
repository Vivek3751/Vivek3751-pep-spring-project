package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@Service
@Transactional
public class MessageService {
    MessageRepository messageRepository;
    AccountRepository accountRepository;
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }
    /*3..Our API should be able to process the creation of new messages.

As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages. The request body will contain a JSON representation of a message, which should be persisted to the database, but will not contain a message_id.

- The creation of the message will be successful if and only if the message_text is not blank, is under 255 characters, and posted_by refers to a real, existing user. If successful, the response body should contain a JSON of the message, including its message_id. The response status should be 200, which is the default. The new message should be persisted to the database.
- If the creation of the message is not successful, the response status should be 400. (Client error) */
    public Message createNewMessageService(Message message){
        Optional<Account> messageInAccountOptional = accountRepository.findById(message.getPosted_by());
        if(messageInAccountOptional.isPresent())
            return messageRepository.save(message);
        return null;
    }
    
    public Message saveMessage(Message message){
       return messageRepository.save(message);
    }
    /*6: Our API should be able to delete a message identified by a message ID.
As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{message_id}.
- The deletion of an existing message should remove an existing message from the database.
If the message existed, the response body should contain the number of rows updated (1).
The response status should be 200, which is the default.
- If the message did not exist, the response status should be 200, but the response body should be empty.
This is because the DELETE verb is intended to be idempotent, ie,
multiple calls to the DELETE endpoint should respond with the same type of response.*/
    public int deleteMessageById(int message_id){
        Optional<Message> existingMessage = messageRepository.findById(message_id);
        if(existingMessage.isPresent()){
            //Message deletedMessage = messageRepository.getById(messageID);
            messageRepository.deleteById(message_id);
            return 1;// that 1 row (message) was updated (deleted)
        }
        return 0;
    }
    /*5: Our API should be able to retrieve a message by its ID.
As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{message_id}.
- The response body should contain a JSON representation of the message identified by the message_id.
It is expected for the response body to simply be empty if there is no such message.
The response status should always be 200, which is the default. */ 
    public Message getMessageById(int message_id) {
        Optional<Message> existingMess = messageRepository.findById(message_id);
        if(existingMess.isPresent()){
            return existingMess.get();
        }
        return null;
    }
    /*4..Our API should be able to retrieve all messages.

As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.

- The response body should contain a JSON representation of a list containing all messages retrieved from the database. It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.
 */
    public List<Message> getAllMessages(){
        //List<Message> allMsgs = messageRepository.findAll();
        return messageRepository.findAll();
    }
    /*7: Our API should be able to update a message text identified by a message ID.
As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}. The request body should contain a new message_text values to replace the message identified by message_id.
The request body can not be guaranteed to contain any other information.
- The update of a message should be successful if and only if the message id already exists and the new message_text is not blank and is not over 255 characters. If the update is successful, the response body should contain the number of rows updated (1), and the response status should be 200, which is the default. The message existing on the database should have the updated message_text.
- If the update of the message is not successful for any reason, the response status should be 400. (Client error) */
    public int updateMessageById(int message_id, Message replacement){
        Optional<Message> messageOptional = messageRepository.findById(message_id);
        if((messageOptional.isPresent())){
            Message message = messageOptional.get();
            message.setMessage_text(replacement.getMessage_text());
            return 1;            
        }
        return 0;
    }
    /* 8: Our API should be able to retrieve all messages written by a particular user. 
As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{account_id}/messages.
- The response body should contain a JSON representation of a list containing all messages posted by a particular user,
which is retrieved from the database. It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.*/
    public List<Message> getMessagesOfaUser(int account_id){
        Optional<List<Message>> listOptional = messageRepository.getPostedBy(account_id);
        if(listOptional.isPresent()){
            return listOptional.get();
        }
        return new ArrayList<>();
    }
}
