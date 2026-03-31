const socket =new WebSocket("ws://localhost:8080/websocket/888")
socket.onopen=()=>{
}
socket.onmessage=(event)=>{
}
socket.onclose=()=>{

}
socket.onerror=()=>{
    console.log("websocket连接错误")
}
export default socket