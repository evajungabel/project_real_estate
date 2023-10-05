package hu.progmasters.moovsmart.controller;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import hu.progmasters.moovsmart.service.ChatGPTClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/openai")
public class ChatController {

    @Value("${openai.api.chat.default.role}")
    private String defaultRole;

    @Autowired
    private ChatGPTClientService chatGPTClientService;

    @GetMapping("/chat/{prompt}")
    public String chatGptRequest(@PathVariable String prompt) {

        final OpenAiService service = chatGPTClientService.getOpenAiService();
        final ChatCompletionRequest chatRequest = chatGPTClientService.getChatCompletionRequest(List.of(new ChatMessage(defaultRole, prompt)));

        return service.createChatCompletion(chatRequest).getChoices().get(0).getMessage().getContent();
    }

}