// pages/pay/pay.js
const app = getApp()
Page({
  btn1() {
    // 防止重复提交
    if (this.data.isSubmitting) {
      return;
    }
    
    if (!app.tokenexam()) {
      return
    }
    if (app.globalData.dz.trim() === '' || app.globalData.dz === null) {
      wx.showToast({
        title: "请正确填写地址",
        duration: 2000,
        icon: "error"
      })
      return;
    }
    else {
      // 设置提交状态为true，禁用按钮
      this.setData({
        isSubmitting: true
      });
      
      wx.request({
        url: app.globalData.baseUrl + '/zd', method: "post",
        data: { token: wx.getStorageSync('token'), list: this.data.paylist, sum: this.data.zj, msg: this.data.bz, address: this.data.shdz }, header: {
          'content-type': 'application/json'
        }, success: (res) => {
          if (res.data.status === 200) {
            wx.request({
              url: app.globalData.baseUrl + '/giveorder',
              method: "post",header:{'content-type': 'application/x-www-form-urlencoded'}, data: { status: 0, uuid: res.data.data }, success: (res) => {
                this.setData({
                  paylist: [],
                  zj: 0,
                  bz: '可向商家/骑手提出菜品与配送要求 >'
                });
                wx.showToast({
                  title: "支付成功"
                  , duration: 2000,
                  icon: "success"
                })
                wx.navigateTo({
                  url: '/pages/paysuccess/paysuccess',
                })
              }, fail: (err) => {
                console.log(err)
                wx.showToast({
                  title: "出现严重错误",
                  icon: "error",
                  duration: 2000
                })
              }, complete: () => {
                // 无论成功失败，都恢复提交状态
                this.setData({
                  isSubmitting: false
                });
              }
            })
          }
          else {
            wx.showToast({
              title: "支付失败"
              , duration: 2000,
              icon: "error"
            })
            console.log(res.data)
            // 恢复提交状态
            this.setData({
              isSubmitting: false
            });
          }
        }, fail: (err) => {
          wx.showToast({
            title: "支付失败,请联系客服"
            , duration: 2000,
            icon: "error"
          })
          // 恢复提交状态
          this.setData({
            isSubmitting: false
          });
        }
      })
    }
  },
  bz() {
    wx.showModal({
      title: "修改备注",
      editable: true,
      placeholderText: "请修改备注内容",
      success: (res) => {
        if (res.content && res.content.trim() !== '') {
          this.setData({
            bz: res.content
          })
        }
      }
    })
  },

  /**
   * 页面的初始数据
   */
  data: {
    shdz: app.globalData.dz || '请选择收货地址 >',
    paylist: [],
    zj: 0,
    bz: '可向商家/骑手提出菜品与配送要求 >',
    isSubmitting: false // 添加提交状态标志，防止重复提交
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.setData({
      shdz: app.globalData.dz || '请选择收货地址 >',
      paylist: app.globalData.alllist.filter((item) => {
        return item.count > 0
      }),
      zj: app.globalData.alllist.filter((item) => {
        return item.count > 0
      }).reduce((sum, item) => {
        return sum + (item.price * item.count)
      }, 5)
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
    if (app.globalData) {
      this.setData({
        shdz: app.globalData.dz || '请选择收货地址 >'
      })
    }
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