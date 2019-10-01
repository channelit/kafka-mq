package biz.cits.message;

import org.springframework.stereotype.Component;

import java.util.AbstractMap;

@Component
public class MsgParser {

    public static AbstractMap.SimpleEntry<String, String> parse(String msg) {
        String[] splitMsg = msg.split(",");
        AbstractMap.SimpleEntry<String, String> out
                = new AbstractMap.SimpleEntry<>(splitMsg[0], splitMsg[1]);
        return out;
    }

}
