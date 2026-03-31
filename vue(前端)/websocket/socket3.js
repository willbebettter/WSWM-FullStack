const socket3 = new WebSocket("ws://localhost:8080/websocket/555")
socket3.onopen = () => {
}
socket3.onmessage = () => {
}
socket3.onclose = () => {
}
socket3.onerror = () => {
    console.log("websocket连接错误")
}
export default socket3