package msg;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/message")
public class MesgControlleur {

    @Autowired
    private  MesgService mesgService;

    @PostMapping("/createMsg")
    @ResponseStatus(HttpStatus.CREATED)
    public Message createMessage(@RequestBody Message msg){
        return  mesgService.addMessage(msg);
    }

    @GetMapping("/allmesg")
    public List<Message> getAllMessge(){
        return mesgService.findAllMessages();
    }

    @GetMapping("/{mesgId}")
    public Message getMessage(@PathVariable String mesgId){
        return mesgService.getMessageById(mesgId);
    }


    @PutMapping("/updatemessage")
    public Message updateMessage(Message msgRequest){
        return mesgService.updateMessage(msgRequest);
    }

    @DeleteMapping("/deleteUpdate")
    public  void  deleteMesg(String mesgId){
        mesgService.deleteMesg( mesgId);
    }






    //Save
    //Delete
    //Update
    //read


}
