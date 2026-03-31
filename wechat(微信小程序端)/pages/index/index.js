const app = getApp()
Page({
  qzf() {
    if (!app.tokenexam()) {
      return
    }
    app.globalData.alllist = this.data.alllist
    wx.navigateTo({
      url: '/pages/pay/pay',
    })
  }
  ,
  count() {
    let zj = this.data.alllist.reduce((sum, item) => sum = sum + item.price * item.count, 0)
    this.setData({
      countprice: zj
    })
  },
  gwadd(e) {
    for (let i = 0; i < this.data.alllist.length; i++) {
      if (this.data.alllist[i].id === e.currentTarget.dataset.id) {
        this.data.alllist[i].count++
        this.setData({
          alllist: this.data.alllist,
        })
        for (let j = 0; j < this.data.currentlist.length; j++) {
          if (this.data.alllist[i].id === this.data.currentlist[j].id) {
            this.data.currentlist[j].count = this.data.alllist[i].count
            this.setData({
              currentlist: this.data.currentlist
            })
          }
        }
        this.count()
      }
    }
  }
  ,
  gwsub(e) {
    for (let i = 0; i < this.data.alllist.length; i++) {
      if (this.data.alllist[i].id === e.currentTarget.dataset.id) {
        if (this.data.alllist[i].count === 0) {
          return
        }
        this.data.alllist[i].count--
        this.setData({
          alllist: this.data.alllist
        })
        for (let j = 0; j < this.data.currentlist.length; j++) {
          if (this.data.alllist[i].id === this.data.currentlist[j].id) {
            this.data.currentlist[j].count = this.data.alllist[i].count
            this.setData({
              currentlist: this.data.currentlist
            })
          }
        }
        this.count()
      }
    }
  },
  shuru(e) {
    this.setData({
      shuru: e.detail.value
    })
  },
  fsmsg() {
    if (!app.tokenexam()) {
      return
    }
    if (this.data.shuru.trim() === '') {
      console.log("未获取到内容")
      this.setData({
        shuru: ''
      })
    }
    else {
      wx.request({
        url: app.globalData.baseUrl + '/fbpl', method: "post", data: { token: wx.getStorageSync('token'), content: this.data.shuru.trim() }, header: {
          'content-type': 'application/x-www-form-urlencoded'
        }, success: (res) => {
          if (res.data.status === 200) {
            wx.request({
              url: app.globalData.baseUrl + '/pl', method: 'get', success: (res) => {
                if (res.data.status === 200) {
                  this.setData({
                    pllist: res.data.data
                  })
                } else {
                  console.log('获取失败' + res.data.msg)
                }
              }, fail: (err) => {
                console.log("请求失败")
                this.setData({
                  pllist: []
                })
              }
            })
          }
          else { console.log("发表评论数据失败") }
        }
      });
      this.setData({
        shuru: ''
      })
    }
  },
  topdj(e) {
    this.setData({
      activeid2: e.currentTarget.dataset.id
    });
    if (e.currentTarget.dataset.id === "2") {
      this.setData({
        dc: true
      })
      wx.request({
        url: app.globalData.baseUrl + '/pl', method: 'get', success: (res) => {
          if (res.data.status === 200) {
            this.setData({
              pllist: res.data.data
            })
          } else {
            console.log('获取失败' + res.data.msg)
          }
        }, fail: (err) => {
          console.log("请求失败")
          this.setData({
            pllist: []
          })
        }
      })
    }
    else {
      this.setData({
        dc: false
      })
    }
  },
  getgoods() {
    return new Promise((resolve, reject) => {
      wx.request({
        url: app.globalData.baseUrl + "/getgoods",
        method: "get", success: (res) => {
          resolve();
          if (res.data.status === 200) {
            this.setData({
              list1: res.data.data
            })
          }
          else {
            console.log("获取商品信息列表失败")
          }
        }, fail: (err) => {
          reject();
          console.log("发送请求失败,请重试")
        }
      })
    })
  },
  getmix() {
    return new Promise((resol, rej) => {
      wx.request({
        url: app.globalData.baseUrl + "/getmix",
        method: "get", success: (res) => {
          resol();
          if (res.data.status === 200) {
            this.setData({
              list2: res.data.data
            })
          }
          else {
            console.log("获取套餐信息列表失败")
          }
        }, fail: (err) => {
          rej();
          console.log("发送请求失败,请重试")
        }
      })
    })
  },// 注意：域名必须是 HTTPS/wss，本地测试可以配置不校验合法域名
  initSocket() {
    // 全局变量或页面 data 中存储 task
    this.data.socketTask = wx.connectSocket({
      url: 'ws://localhost:8080/websocket/666', // 替换为你的服务器地址和用户ID
      success: (res) => {
      },
      fail: (err) => {
        console.error('WebSocket 连接失败', err);
      }
    });

    // 监听连接打开
    this.data.socketTask.onOpen((res) => {
      wx.request({
        url: app.globalData.baseUrl + '/web', method: "get"
      })
    });

    // 监听消息接收
    this.data.socketTask.onMessage((res) => {
      if (res.data === "0") {
        this.setData({
          sjstatus: false
        })
      }
      else if (res.data === "1") {
        this.setData({
          sjstatus: true
        })
      } else if (res.data === "ping") {
        this.data.socketTask.send({ data: "pong" })
      }
      else { console.log(res.data) }
    });

    // 监听连接关闭
    this.data.socketTask.onClose((res) => {
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
    this.data.socketTask.onError((err) => {
      console.error('WebSocket 错误：', err);
    });
  },
  wzlist() {
    let new1 = [], new2 = []
    new1 = this.data.list1.map(item => ({ id: item.id, name1: item.good_name, type: item.type_name, text: item.good_text, price: item.good_price, photo: item.good_photo, sale: item.good_sale }))
    new2 = this.data.list2.map(item => ({ id: item.id, name1: item.mix_name, type: item.type_name, text: item.mix_text, price: item.mix_price, photo: item.mix_photo, sale: item.mix_sale }))
    let ll = [...new1, ...new2]
    for (let i = 0; i < ll.length; i++) {
      ll[i].count = 0
    };
    this.setData({
      alllist: ll,
      currentlist: ll
    })
  },
  cbdj(e) {
    let all = this.data.alllist
    this.setData({
      activeid: e.currentTarget.dataset.id,
      activeid2: "1"
    });
    switch (e.currentTarget.dataset.id) {
      case "1": {
        all = all.filter(item => {
          return item.id < 100000
        });
        this.setData({
          currentlist: all
        })
        break;
      }
      case "2": {
        all = all.filter(item => {
          return item.type === "主食类"
        });
        this.setData({
          currentlist: all
        })
        break;
      }
      case "3": {
        all = all.filter(item => {
          return item.type === "饮品类"
        });
        this.setData({
          currentlist: all
        })
        break;
      }
      case "4": {
        all = all.filter(item => {
          return item.type === "炸串小吃"
        });
        this.setData({
          currentlist: all
        })
        break;
      }
      case "5": {
        all = all.filter(item => {
          return item.type === "菜品"
        });
        this.setData({
          currentlist: all
        })
        break;
      }
      case "6": {
        all = all.filter(item => {
          return item.id >= 100000
        });
        this.setData({
          currentlist: all
        })
        break;
      }
      case "7": {
        all = all.filter(item => {
          return item.type === "拼好饭"
        });
        this.setData({
          currentlist: all
        })
        break;
      }
      case "8": {
        all = all.filter(item => {
          return item.type === "热销组合"
        });
        this.setData({
          currentlist: all
        })
        break;
      }
      case "9": {
        all = all.filter(item => {
          return item.type === "超值套餐"
        });
        this.setData({
          currentlist: all
        })
        break;
      }
      case "10": {
        all = all.filter(item => {
          return item.type === "疯狂星期四"
        });
        this.setData({
          currentlist: all
        })
        break;
      }
      case "11": {
        all = this.data.alllist
        this.setData({
          currentlist: all
        })
        break;
      }
    }
  },
  data: {
    goodslist: [],
    mixlist: [],
    activeid: "11",
    activeid2: "1",
    dc: false,
    pllist: [],
    shuru: "",
    list1: [],
    list2: [],
    alllist: [],
    currentlist: [],
    countprice: 0,
    sjstatus: false,
    socketTask: null
  },
  /**
   * 生命周期函数--监听页面加载
   */
  async onLoad() {
    await this.getgoods();
    await this.getmix();
    this.wzlist();
    this.initSocket();
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  async onShow() {
    await this.getgoods();
    await this.getmix();
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})
