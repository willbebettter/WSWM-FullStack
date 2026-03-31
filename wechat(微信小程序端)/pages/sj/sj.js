// pages/sj/sj.js
const app = getApp()
Page({
  update(e) {
    this.setData({
      shuru: e.detail.value
    })
  },
  del() {
    wx.showModal({
      title: "提示",
      content: "确认要清空所有历史消息吗?", success: (res) => {
        if (res.confirm) {
          app.globalData.msglist = []
          this.setData({
            msglist: []
          })
          wx.setStorageSync('msglist', [])
        }
        else {
          return;
        }
      }
    })
  },
  fs() {
    if (this.data.shuru.trim() === '') {
      return
    }
    else {
      this.data.msglist.push("我: " + this.data.shuru.trim())
      this.setData({
        msglist: this.data.msglist
      })
      app.globalData.msglist = this.data.msglist
      wx.setStorageSync('msglist', this.data.msglist)
    }
    wx.request({
      url: app.globalData.baseUrl + '/fs', data: { token: wx.getStorageSync('token'), msg:this.data.shuru.trim() },
      method: "post", header: { 'content-type': 'application/x-www-form-urlencoded' }, success: (res => {
        if (res.data.status === 200) {
          wx.showToast({
            title: '发送消息成功',
            icon: "success",
            duration: 2000
          })

        } else {
          wx.showToast({
            title: '发送消息失败',
            icon: "error",
            duration: 2000
          })
        }
      }), fail: (err => {
        wx.showToast({
          title: '发送消息失败',
          icon: "error",
          duration: 2000
        })
        console.log(err)
      })
    })
    this.setData({
      shuru:''
    })
  },
  /**
   * 页面的初始数据
   */
  data: {
    shuru: '',
    yh: 'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0',
    msglist: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.setData({
      msglist: wx.getStorageSync('msglist')
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    // 保存当前页面实例到全局，方便app.js直接调用
    app.globalData.currentSjPage = this;
    // 页面显示时同步最新的消息列表
    this.setData({
      msglist: app.globalData.msglist
    });
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})