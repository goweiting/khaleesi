package movement;

import communication.SDPPort;

/**
 * Created by verden on 27/01/17.
 */
public class movement {

    public static void main(String[] args) {
        MiniPID miniPID = new MiniPID(1, 1, 1);
        SDPPort sdpPort = new SDPPort();
        sdpPort.connect(null, "pang");
        sdpPort.commandSender("motor", 5, 100);

    }


}

