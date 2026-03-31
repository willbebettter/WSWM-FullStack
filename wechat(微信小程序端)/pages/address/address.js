// pages/address/address.js
const app = getApp()
Page({
  dxk(e) {
    app.globalData.dz = this.data.addlist[e.detail.value]
    wx.navigateBack({
      delta: 1
    })
  },
  sr(e) {
    this.setData({
      shuru: e.detail.value
    })
  },
  del(e) {
    this.data.addlist.splice(e.currentTarget.dataset.id, 1)
    this.setData({
      addlist: this.data.addlist
    })
    wx.setStorageSync('address', this.data.addlist)
  },
  add() {
    if (this.data.shuru.trim() === '') {
      return
    }
    this.data.addlist.unshift(this.data.shuru.trim())
    this.setData({
      addlist: this.data.addlist,
      shuru: ''
    })
    wx.setStorageSync('address', this.data.addlist)
  },
  /**
   * 页面的初始数据
   */
  data: {
    shuru: '',
    addlist: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad() {
    const x = wx.getStorageSync('address') || []
    this.setData({ addlist: x })
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