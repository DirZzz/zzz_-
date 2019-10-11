/**
 * 页面上拉加载
 * 2017-07-17 16:00
 */
import BScroll from 'better-scroll'

let myScroll = {
  // dataEl(数据dom), conEl(容器dom),scroller(组件初始化的滚动事件),callback(回调函数)
  // 初始化
  init (conEl, scroller) {
    if (scroller === '') {
      scroller = new BScroll(conEl, {
        click: true,
        probeType: 2
      })
    } else {
      scroller.refresh()
    }
    return scroller
  },
  // 加载更多
  loadMore (dataEl, conEl, scroller, callback) {
    let dataHeight = dataEl.offsetHeight, // 数据总高度
      eleHeight = conEl.clientHeight // 容器高度
    if (dataHeight < eleHeight) {
      return
    }
    if (scroller !== '') {
      scroller.on('scrollEnd', (pos) => {
        let dataHeight = dataEl.offsetHeight, // 数据总高度
          scrollY = Math.abs(Math.floor(pos.y)) // 滚动条位置
        if (scrollY == dataHeight - eleHeight) { // 滚动高度到达底部
          callback()
        }
      })
    }
  }
}

export default {
  myScroll
}
