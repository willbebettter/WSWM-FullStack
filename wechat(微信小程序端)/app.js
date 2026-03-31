App({
  onLaunch() {
    if (!wx.getStorageSync('msglist') || wx.getStorageSync('msglist') === null) {
      wx.setStorageSync('msglist', [])
    } else {
      this.globalData.msglist = wx.getStorageSync('msglist')
    }
    this.globalData.baseUrl = 'http://localhost:8080/app';
    this.globalData.token = wx.getStorageSync('token')
    this.getuser()
    this.tokenexam()
    wx.request({
      url: this.globalData.baseUrl + '/tokenjy', method: "get",
      data: { token: this.globalData.token }, success: (res) => {
        if (res.data.status === 200) {
          return;
        } else {
          wx.setStorageSync('token', null)
          this.globalData.token = null
          wx.reLaunch({
            url: "/pages/login/login"
          })
        }
      }, fail: (err) => {
        this.globalData.token = null
        wx.reLaunch({
          url: "/pages/login/login"
        })
      }
    })
  },
  getuser() {
    wx.request({
      url: this.globalData.baseUrl + '/yhm', method: "get", data: { token: wx.getStorageSync('token') },
      success: (res => {
        if (res.data.status === 200) {
          this.globalData.userid = res.data.data
          this.initSocket();
        } else {
          wx.setStorageSync('token', null)
          this.globalData.token = null
          wx.reLaunch({
            url: "/pages/login/login"
          })
        }
      })
    })
  }
  ,
  tokenexam() {
    const token1 = wx.getStorageSync('token')
    if (token1 === '' || token1 === null || !token1) {
      wx.reLaunch({
        url: '/pages/login/login',
      })
      return false
    }
    return true
  }
  , initSocket() {
    // 全局变量或页面 data 中存储 task
    this.globalData.socketTask = wx.connectSocket({
      url: 'ws://localhost:8080/websocket/' + this.globalData.userid, // 替换为你的服务器地址和用户ID
      success: (res) => {
      },
      fail: (err) => {
        console.error('WebSocket 连接失败', err);
      }
    });

    // 监听连接打开
    this.globalData.socketTask.onOpen((res) => {
    });

    // 监听消息接收
    this.globalData.socketTask.onMessage((res) => {
      if (res.data === "ping") {
        this.globalData.socketTask.send({ data: "pong" })
      }
      else {
        this.globalData.msglist.push("商家: " + res.data)
        wx.setStorageSync("msglist", this.globalData.msglist)
        // 如果当前页面是sj页面，直接更新数据
        if (this.globalData.currentSjPage) {
          this.globalData.currentSjPage.setData({
            msglist: this.globalData.msglist
          })
        }
      }
    })
    // 监听连接关闭
    this.globalData.socketTask.onClose((res) => {
      wx.showToast({
        title:"断线了",
        duration:2000,
        icon:"error"
      })
      wx.reLaunch({
        url:"/pages/index/index"
      })
    });

    // 监听错误
    this.globalData.socketTask.onError((err) => {
      console.error('WebSocket 错误：', err);
    });
  },
  login() {
    wx.login({
      success: (res) => {
        if (res.code) {
          wx.request({
            url: this.globalData.baseUrl + '/givetoken',
            method: "post", data: { code: res.code }, header: {
              'content-type': 'application/x-www-form-urlencoded'
            }
            , success: (res) => {
              if (res.data.status === 200) {
                this.globalData.token = res.data.data
                wx.setStorageSync("token", this.globalData.token)

              }
              else if (res.data.status === 201) {
                this.globalData.token = res.data.data
                wx.setStorageSync("token", this.globalData.token)

              }
              else {
                console.log("登录失败", res.data.data)
                this.globalData.token = ''

              }
            }, fail: (err) => {
              console.log("获取失败", err);
              this.globalData.token = ''
            }
          })
        }
      }
    })
  },
  // 全局数据（所有页面都能读取）
  globalData: {
    baseUrl: '',
    userInfo: null,
    token: null,
    dz: '',
    alllist: [],
    socketTask: null,
    userid: '',
    msglist: []
  },
})