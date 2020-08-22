package com.youedata.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 后端与前端的通信
 */
@ServerEndpoint("/process/websocketWeb/{serviceNumber}")
@Component
public class WebSocketServer {

    private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //保存离线消息
    private static Map<String,List<String>> messageMap = new LinkedHashMap<>();
    //接收id
    String serviceNumber="";
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session, @PathParam("serviceNumber") String serviceNumber) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        log.info("有新窗口开始监听:"+serviceNumber);
        this.serviceNumber=serviceNumber;
        try {
            if (messageMap!=null&&messageMap.size()>0&&messageMap.get(serviceNumber)!=null&&messageMap.get(serviceNumber).size()>0) {
                List<String> messageList = messageMap.get(serviceNumber);
                for (int i=0;i<messageList.size();i++){
                    log.info("发送消息到窗口"+serviceNumber+"，发送内容:"+messageList.get(i));
                    sendMessage(messageList.get(i));
                }
            }

        } catch (IOException e) {
            log.error("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端(app)消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口"+serviceNumber+"的信息:"+message);
        //群发消息
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {

        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     * */
    public static Object sendInfo(String message, @PathParam("serviceNumber") String serviceNumber) throws IOException {
        log.info("推送消息到窗口"+serviceNumber+"，推送内容:"+message);
        boolean b = true;
        for (WebSocketServer item : webSocketSet) {
            if(item.serviceNumber.equals(serviceNumber)){
                item.sendMessage(message);
                b = false;
            }
            continue;
        }
        if(b) {
            List<String> messageList = new LinkedList<>();
            if (messageMap!=null&&messageMap.size()>0&&messageMap.get(serviceNumber)!=null&&messageMap.get(serviceNumber).size()>0){
                messageList = messageMap.get(serviceNumber);
                messageList.add(message);
                messageMap.put(serviceNumber,messageList);
            }else{
                messageList.add(message);
                messageMap.put(serviceNumber,messageList);
            }
            return "success";
        }else{
            return "error";
        }

    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }


}

