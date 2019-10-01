package biz.cits.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

public class MsgGenerator {

    private static String[] clients = new String[]{"ABCDE", "ABCDF", "ABCDG", "ABCDH", "ABCDI", "ABCEF", "ABCEG", "ABCEH", "ABCEI", "ABCFG", "ABCFH", "ABCFI", "ABCGH", "ABCGI", "ABCHI", "ABDEF", "ABDEG", "ABDEH", "ABDEI", "ABDFG", "ABDFH", "ABDFI", "ABDGH", "ABDGI", "ABDHI", "ABEFG", "ABEFH", "ABEFI", "ABEGH", "ABEGI", "ABEHI", "ABFGH", "ABFGI", "ABFHI", "ABGHI", "ACDEF", "ACDEG", "ACDEH", "ACDEI", "ACDFG", "ACDFH", "ACDFI", "ACDGH", "ACDGI", "ACDHI", "ACEFG", "ACEFH", "ACEFI", "ACEGH", "ACEGI", "ACEHI", "ACFGH", "ACFGI", "ACFHI", "ACGHI", "ADEFG", "ADEFH", "ADEFI", "ADEGH", "ADEGI", "ADEHI", "ADFGH", "ADFGI", "ADFHI", "ADGHI", "AEFGH", "AEFGI", "AEFHI", "AEGHI", "AFGHI", "BCDEF", "BCDEG", "BCDEH", "BCDEI", "BCDFG", "BCDFH", "BCDFI", "BCDGH", "BCDGI", "BCDHI", "BCEFG", "BCEFH", "BCEFI", "BCEGH", "BCEGI", "BCEHI", "BCFGH", "BCFGI", "BCFHI", "BCGHI", "BDEFG", "BDEFH", "BDEFI", "BDEGH", "BDEGI", "BDEHI", "BDFGH", "BDFGI", "BDFHI", "BDGHI", "BEFGH", "BEFGI", "BEFHI", "BEGHI", "BFGHI", "CDEFG", "CDEFH", "CDEFI", "CDEGH", "CDEGI", "CDEHI", "CDFGH", "CDFGI", "CDFHI", "CDGHI", "CEFGH", "CEFGI", "CEFHI", "CEGHI", "CFGHI", "DEFGH", "DEFGI", "DEFHI", "DEGHI", "DFGHI", "EFGHI"};

    private static Map<String, Integer> clientMessageIds = Collections.synchronizedMap(new HashMap<>());

    private static String getClient() {
        int rnd = new Random().nextInt(clients.length);
        return clients[rnd];
    }

    public static Map<String, String> getMmessages(int numMessage) {
        Map<String, String> messages = new HashMap<>();
        IntStream.range(0, numMessage)
                .peek(i -> {
                    if (i + 1 % 100 == 0) {
                        System.out.println("message number " + i + " started.");
                    }
                }).forEach(i -> {
            String client = getClient();
            messages.put(client, getClientMessage(client));
        });
        return messages;
    }

    private static String getClientMessage(String client) {
        clientMessageIds.putIfAbsent(client, 1);
        clientMessageIds.compute(client, (k, v) -> (v == null) ? 1 : v + 1);
        return client + "," + clientMessageIds.get(client);
    }
}
