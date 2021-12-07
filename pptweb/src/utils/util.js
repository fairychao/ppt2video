export default {
  // 此处可以添加公用的工具类方法
  // 首字母大写
  ucfirst (str) {
    str = str.toLowerCase()
    let strarr = str.split(' ')
    let result = ''
    for (let i in strarr) {
      result += strarr[i].substring(0, 1).toUpperCase() + strarr[i].substring(1) + ''
    }
    return result
  },
  // 转换broker数据为map
  generateBrokerMap (brokerServer, clusterAddrTable, brokerAddrTable) {
    let map = {}
    for (let brokerName in brokerServer) {
      let brokerStatusList = brokerServer[brokerName]
      for (let clusterName in clusterAddrTable) {
        let brokerNameList = clusterAddrTable[clusterName]
        if (clusterName) {
          map[clusterName] = []
        }
        brokerNameList.map(clusterBrokerName => {
          clusterBrokerName = this.ucfirst(clusterBrokerName)
          if (clusterBrokerName === brokerName) {
            for (let index in brokerStatusList) {
              let brokerStatus = brokerStatusList[index]
              brokerStatus.split = brokerName
              brokerStatus.index = index
              brokerStatus.address = brokerAddrTable[clusterBrokerName].BrokerAddrs[index]
              brokerStatus.brokerName = brokerAddrTable[clusterBrokerName].BrokerName
              map[clusterName].push(brokerStatus)
            }
          }
        })
      }
    }
    return map
  }
}
