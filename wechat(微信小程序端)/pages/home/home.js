// pages/home/home.js
const app = getApp();
Page({
  tuichu() {
    wx.setStorageSync('token', null)
    app.globalData.token = null
    wx.reLaunch({
      url: "/pages/login/login"
    })
  },
  xgname() {
    if (!app.tokenexam()) {
      return
    }
    let nr;
    wx.showModal({
      title: "修改名称",
      editable: true,
      placeholderText: "请输入要设置为的新名称", success: (res) => {
        if (res.confirm) {
          if (res.content.trim() === '') {
            return
          } else {
            nr = res.content;
            wx.request({
              url: app.globalData.baseUrl + '/updatename',
              method: "post", header: {
                'content-type': 'application/x-www-form-urlencoded'
              }, data: { name: nr, token: wx.getStorageSync('token') },
              success: (res) => {
                if (res.data.status === 200) {
                  wx.setStorageSync('token', res.data.data)
                  this.hqyh()
                  wx.showToast(
                    {
                      title: "成功修改名称",
                      duration: 2000,
                      icon: "success"
                    }
                  )
                }
                else {
                  wx.showToast(
                    {
                      title: "名称已存在",
                      duration: 2000,
                      icon: "error"
                    }
                  )
                }
              }, fail: (err) => {
                console.log(err)
              }
            })
          }
        }
        else { return }
      }
    })
  },
  xgpho() {
    if (!app.tokenexam()) {
      return
    }
    let pho;
    wx.chooseImage({
      count: 1,
      success: (res) => {
        pho = res.tempFilePaths[0]
        wx.uploadFile({
          filePath: pho,
          name: "file",
          url: app.globalData.baseUrl + '/updatephoto',
          method: "post",
          formData: {
            token: wx.getStorageSync('token')
          },
          success: (res) => {
            if (JSON.parse(res.data).status === 200) {
              wx.setStorageSync('token', JSON.parse(res.data).data)
              this.hqyh()
              wx.showToast(
                {
                  title: "成功修改头像",
                  duration: 2000,
                  icon: "success"
                }
              )
            }
            else {
              wx.showToast(
                {
                  title: "修改头像失败",
                  duration: 2000,
                  icon: "error"
                }
              )
            }
          }, fail: (err) => {
            console.log(err)
          }
        })
      }
    })
  },
  myaddress() {
    if (!app.tokenexam()) {
      return
    }
    wx.navigateTo({
      url: '/pages/address/address',
    })
  },
  connect() {
    if (!app.tokenexam()) {
      return
    }
    wx.navigateTo({
      url: '/pages/sj/sj'
    })

  },
  log() {
    app.globalData.token = null
    wx.reLaunch({
      url: "/pages/login/login"
    })
  },
  hqyh() {
    if (wx.getStorageSync('token') !== null) {
      wx.request({
        url: app.globalData.baseUrl + '/getyh', method: "get", data: { token: wx.getStorageSync('token') },
        success: (res) => {
          if (res.data.status === 200) {
            this.setData({
              yhlist: res.data.data
            })
          }
          else {
            this.setData({
              yhlist: []
            })
            console.log("检测到token不正确")
          }
        },
        fail: (err) => {
          console.log(err);
        }
      })
    }
    else {
    }
  },
  /**
   * 页面的初始数据
   */
  data: {
    yhlist: [],
    zt: true
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.hqyh()
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
    if (app.globalData.token !== '' && app.globalData.token !== null) {
      this.setData({
        zt: true
      })
    }
    else {
      this.setData({
        zt: false
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