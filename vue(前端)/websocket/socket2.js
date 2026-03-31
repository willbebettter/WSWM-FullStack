const socket2 = new WebSocket("ws://localhost:8080/websocket/999")
socket2.onopen = () => {
}
socket2.onmessage = () => {
}
socket2.onclose = () => {
}
socket2.onerror = () => {
    console.log("websocket连接错误")
}
export default socket2