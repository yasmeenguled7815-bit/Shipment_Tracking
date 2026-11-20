import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

let stompClient = null;

export const connectWebSocket = (
  onLocationUpdate,
  onConnected,
  onDisconnected
) => {

  stompClient = new Client({

    webSocketFactory: () => {
      return new SockJS("http://localhost:8080/ws");
    },

    reconnectDelay: 5000,

    debug: (message) => {
      console.log(message);
    },

    onConnect: () => {

      console.log("WebSocket Connected");

      if (onConnected) {
        onConnected();
      }

      stompClient.subscribe(
        "/topic/location",
        (message) => {

          const location = JSON.parse(message.body);

          console.log(
            "Location received:",
            location
          );

          onLocationUpdate(location);
        }
      );
    },

    onWebSocketClose: () => {

      console.log(
        "WebSocket disconnected. Reconnecting..."
      );

      if (onDisconnected) {
        onDisconnected();
      }
    },

    onStompError: (frame) => {

      console.error(
        "STOMP Error:",
        frame.headers["message"]
      );
    }
  });

  stompClient.activate();
};


export const disconnectWebSocket = () => {

  if (stompClient) {

    stompClient.deactivate();

    stompClient = null;
  }
};