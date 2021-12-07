export default {
  namespaced: true,
  state: {
    // 页面文档可视高度(随窗口改变大小)
    documentClientHeight: 0,
    // 内容, 是否需要刷新
    contentIsNeedRefresh: false
  },
  mutations: {
    updateDocumentClientHeight (state, height) {
      state.documentClientHeight = height
    },
    updateContentIsNeedRefresh (state, status) {
      state.contentIsNeedRefresh = status
    }
  }
}
