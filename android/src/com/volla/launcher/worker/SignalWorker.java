package com.volla.launcher.worker;

import androidnative.SystemDispatcher;
import android.app.Activity;
import android.util.Log;
import java.util.Map;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import org.qtproject.qt5.android.QtNative;
import androidx.lifecycle.ViewModelProvider;
import com.volla.launcher.repository.MessageRepository;
import com.volla.launcher.repository.MainViewModel;
import com.volla.launcher.storage.Message;

public class SignalWorker {

   private static final String TAG = "SignalWorkerl";

    public static final String GET_SIGNAL_MESSAGES = "volla.launcher.signalMessagesAction";
    public static final String GOT_SIGNAL_MESSAGES = "volla.launcher.signalMessagesResponse";
    public static final String GET_SIGNAL_THREADS = "volla.launcher.signalThreadsAction";
    public static final String GOT_SIGNAL_THREADS = "volla.launcher.signalThreadsResponse";

    static {
        SystemDispatcher.addListener(new SystemDispatcher.Listener() {

            public void onDispatched(String type, Map dmessage) {
                final Activity activity = QtNative.activity();
                final Map message = dmessage;
                ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(QtNative.activity().getApplication());
                

                if (type.equals(GET_SIGNAL_MESSAGES)) {
                    // todo: implement (use a separate thread)
                } else if (type.equals(GET_SIGNAL_THREADS)) {
                    // todo: implement (use a separate thread)
                    retriveMessageThreads(message, activity);
                }
            }
        });
    }

    static void retriveMessageThreads(Map message, Activity activity){
        MessageRepository repository = new MessageRepository(QtNative.activity().getApplication());
        ArrayList<Map> messageList = new ArrayList();
        String threadId = (String) message.get("threadAge");
        repository.getAllSendersName().subscribe(it -> {
            for (Message m : it) {
                Map reply = new HashMap();
                reply.put("id", m.getId());
                reply.put("thread_id", m.getUuid());
                reply.put("body", m.getText());
                reply.put("person", m.getTitle());
                reply.put("address", "7653456789");
                reply.put("date", m.getTimeStamp());
                reply.put("read", true);
                reply.put("isSent", true);
                reply.put("image", m.getLargeIcon());
                reply.put("attachments", "");

                Log.e("ThreadMessage", "Sender Name: " + m);
                Log.e("ThreadMessage", m.getNotificationData().toJson());
                messageList.add(reply);
            }
            Map result = new HashMap();
            result.put("messages", messageList );
            result.put("messagesCount", messageList.size() );
            SystemDispatcher.dispatch(GOT_SIGNAL_THREADS, result);
        });
    }
}
