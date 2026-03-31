// index.js
const defaultAvatarUrl = 'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0'
const app = getApp()
Page({
  onLoad: () => {
  },
  tc() {
    app.login()
    wx.switchTab({
      url: '/pages/index/index',
      success: () => {
        wx.showToast({
          title: '登录成功',
          duration: 2000,
          icon: "success"
        })
      },
      fail: () => {
        wx.showToast({
          title: '请稍后再试',
          duration: 2000,
          icon: "error"
        })
      },
    })
  },
})