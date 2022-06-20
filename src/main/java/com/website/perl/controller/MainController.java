package com.website.perl.controller;
import com.website.perl.data.Message;
import com.website.perl.data.User;
import com.website.perl.repository.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class MainController {

      @Autowired
      private MessageRepo messageRepo;

      @Value("${upload.path}")
      private String uploadPath;

      @GetMapping("/")
      public String greeting( Model model){

          return "greeting";
      }

    @GetMapping("/main")
    public String main(@RequestParam(required = false,defaultValue = "") String filter,  Model model){
        Iterable<Message> messages = messageRepo.findAll();

        if(filter !=null && !filter.isEmpty()){
            messages = messageRepo.findByTag(filter);
        }else{
            messages = messageRepo.findAll();
        }
        model.addAttribute("messages",messages);
        model.addAttribute("filter",filter);
        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag, Model model,
            @RequestParam("file") MultipartFile file
    )throws IOException {

        Message message = new Message(text,tag,user);

        if (file !=null && !file.getOriginalFilename().isEmpty()){
           File uploadDir = new File(uploadPath);

           if(!uploadDir.exists()){
               uploadDir.mkdir();
           }
            String uuidFile = UUID.randomUUID().toString();
           String resultFileName = uuidFile + "." + file.getOriginalFilename();
           file.transferTo(new File(uploadPath + "/" +    resultFileName));
            message.setFilename(resultFileName);
        }
        messageRepo.save(message);
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages",messages);
        return "main";
    }




}