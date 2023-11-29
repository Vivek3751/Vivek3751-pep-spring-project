package com.example.controller;

import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
//@RequestMapping("/api")
public class SocialMediaController {
    
    private AccountService accountService;
    private MessageService messageService;
    MessageRepository messageRepository;
    AccountRepository accountRepository;
    public SocialMediaController(MessageService messageService){
        this.messageService = messageService;
    }
    /*1: Our API should be able to process new User registrations. */
    @PostMapping("/register")
    public ResponseEntity<Object> newUserAccountRegister(@RequestBody Account account) {
        Account registeredAccount = accountService.getAccountByUsername(account.getUsername());
        if(registeredAccount != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Account saveAcc=accountService.saveAccount(account);
        return new ResponseEntity<>(saveAcc,HttpStatus.OK);
    }
    /*2: Our API should be able to process User logins. */
    @PostMapping("/login")
    public ResponseEntity<Object> verifyUserLogin(@RequestBody Account account) {
        String storedUsername = accountService.getUserNameString(account);
        if (storedUsername != null && storedUsername.equals(account.getUsername())) {
            // User exists, now check if the password matches
            Account storedAcc = accountService.getAccountByUsername(account.getUsername());
    
            if (storedAcc != null && storedAcc.getPassword().equals(account.getPassword())) {
                // Login successful
                return new ResponseEntity<>(storedAcc,HttpStatus.OK);
            }
            else {
                // Password doesn't match
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        else {
            // User doesn't exist
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    /*3: Our API should be able to process the creation of new messages. */
    @PostMapping("/messages")
    public ResponseEntity<Message> addNewMessageController(@RequestBody Message message){
        if((!message.getMessage_text().isBlank()) && (message.getMessage_text().length()<=255)){
            Message m = messageService.createNewMessageService(message);
            if(m!=null)
                return new ResponseEntity<>(m, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    /* 4: Our API should be able to retrieve all messages.*/
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();

        if (messages.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK); // Return 200 with an empty response body
        } else {
            return new ResponseEntity<>(messages, HttpStatus.OK); // Return 200 with the list of messages
        }
    }
    /*5: Our API should be able to retrieve a message by its ID.  */
    @GetMapping("/messages/{message_id}")
    public Message getMsgById(@PathVariable int message_id){
       return messageService.getMessageById(message_id);
    }
    /*6: Our API should be able to delete a message identified by a message ID.*/
    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Object> deletedMsgByMsgID(@PathVariable int message_id){
        int rowsUpdated = messageService.deleteMessageById(message_id);
        if(rowsUpdated == 1) {
            return new ResponseEntity<>(rowsUpdated, HttpStatus.OK);//Message deleted succesfully
        } else {
            return new ResponseEntity<>(HttpStatus.OK); // Message did not exist
        }
    } 
    /*7: Our API should be able to update a message text identified by a message ID. */
    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMsgByMsgId(@PathVariable int message_id, @RequestBody Message replacement){
        if((!replacement.getMessage_text().isBlank()) && (replacement.getMessage_text().length()<=255)){
            int i = messageService.updateMessageById(message_id, replacement);
            if(i==1)
            return new ResponseEntity<>(i,HttpStatus.OK);              
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
    }
    /* 8: Our API should be able to retrieve all messages written by a particular user. */
    @GetMapping("accounts/{account_id}/messages")
    public List<Message> getMessagesOfaUser(@PathVariable int account_id){
        return messageService.getMessagesOfaUser(account_id);
    }
}

