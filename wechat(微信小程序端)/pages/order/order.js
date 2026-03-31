const app = getApp()
Page({
  cd(e) {
    wx.request({
      url: app.globalData.baseUrl + '/cd', method: "post",
      data: { uuid: e.currentTarget.dataset.id, token: wx.getStorageSync('token') }, header: { 'content-type': 'application/x-www-form-urlencoded' }, success: (res) => {
        if (res.data.status === 200) {
          wx.showToast({
            title: "已成功向商家催单",
            icon: "success",
            duration: 2000
          })
        }
      }
    })
  },
  pj(e) {
    wx.showModal({
      title: "评价",
      editable: true,
      placeholderText: "请输入您的评价",
      success: (res) => {
        if (res.confirm) {
          if (res.content.trim() !== '') {
            wx.request({
              url: app.globalData.baseUrl + '/pj', method: "post", data: { uuid: e.currentTarget.dataset.id, pj: res.content.trim() }, header: { 'content-type': 'application/x-www-form-urlencoded' }, success: (res) => {
                if (res.data.status === 200) {
                  wx.showToast({
                    title: "成功评价",
                    duration: 2000,
                    icon: "success"
                  })
                  this.allorders()
                }
              }
            })
          }
        }
      }
    })
  },
  activebtn(e) {
    this.setData({
      activeid1: e.currentTarget.dataset.id
    })
    switch (e.currentTarget.dataset.id) {
      case "1": this.setData({
        receivelist: this.data.ddlist
      })
        break;
      case "2": this.setData({
        receivelist: this.data.ddlist.filter(item => {
          return item.status === 1 || item.status === 0 || item.status === 2
        })
      })
        break;
      case "3": this.setData({
        receivelist: this.data.ddlist.filter(item => {
          return item.status === 3
        })
      })
        break;
      case "4": this.setData({
        receivelist: this.data.ddlist.filter(item => {
          return item.status === 5
        })
      })
        break;
    }
  },
  tk(e) {
    let nr = "";
    wx.showModal({
      title: '申请退款',
      placeholderText: "请说明退款原因(可选)",
      editable: true,
      success: (res) => {
        if (res.confirm) {
          if (res.content.trim() !== '') {
            nr = "用户未说明原因"
          }
          else { nr = res.content }
          wx.request({
            url: app.globalData.baseUrl + '/tk', method: "post", header: { 'content-type': 'application/x-www-form-urlencoded' },
            data: { uuid: e.currentTarget.dataset.id },
            success: (res) => {
              if (res.data.status == 200) {
                this.allorders();
                wx.showToast({
                  title: '退款成功',
                  duration: 2000,
                  icon: "success"
                })
              } else {
                wx.showToast({
                  title: '退款失败',
                  duration: 2000,
                  icon: "error"
                })
              }
            }, fail: (err) => {
              wx.showToast({
                title: '未知错误',
                duration: 2000,
                icon: "error"
              })
            }
          })
        }
      }
    })
  },
  allorders() {
    wx.request({
      url: app.globalData.baseUrl + '/allorders', method: "get",
      data: { token: wx.getStorageSync('token') }, success: (res) => {
        if (res.data.status === 200) {
          this.setData({
            receivelist: res.data.data,
            ddlist: res.data.data
          })
        }
        else {
          this.setData({
            receivelist: []
          })
          console.log("状态码不正确")
        }
      }, fail: (err) => {
        this.setData({
          receivelist: []
        })
        console.log("失败", err)
      }
    })
  },
  /**
   * 页面的初始数据
   */
  data: {
    activeid1: "1",
    ddlist: [],
    receivelist: [],
    btnzt: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.allorders();
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
    this.allorders();
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