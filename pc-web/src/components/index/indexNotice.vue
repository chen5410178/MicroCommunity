<template>
  <div class="index-1-right-1">
    <div class="index-title text-left">
      <span>{{ $t('propertyIndex.ownerFeedback') }}</span>
    </div>
    <ul id="pool" class="notice-list">
      <li v-if="!indexNoticeInfo.notices || indexNoticeInfo.notices.length < 1" 
          class="flex justify-between padding-top-sm no-data">
        <div class="vc-index-notice-title">
          {{ $t('propertyIndex.noFeedback') }}
        </div>
      </li>
      <li v-else v-for="(item, index) in indexNoticeInfo.notices" :key="index" 
          class="flex justify-between padding-top-sm notice-item">
        <div class="vc-index-notice-title">
          {{ item.objName }}({{ item.roomName }}){{ $t('propertyIndex.feedback') }}{{ item.title }}
          ({{ item.state === 'F' ? $t('propertyIndex.completed') : $t('propertyIndex.followingUp') }}<span v-if="item.thridId">-{{ $t('propertyIndex.transferRepair') }}</span>)
        </div>
        <div class="vc-index-notice-time">
          {{ item.createTime }}
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import { getNotepadList } from '@/api/index/propertyIndexApi'

export default {
  name: 'IndexNotice',
  data() {
    return {
      indexNoticeInfo: {
        notices: [],
        scrollInterval: null,
        scrollTopValue: 0,
        scrollDirection: true,
      }
    }
  },
  created() {
  },
  mounted() {
    this.$nextTick(() => {
      window.addEventListener('resize', this.checkPoolScroll)
    })
  },
  beforeDestroy() {
    if (this.indexNoticeInfo.scrollInterval) {
      clearInterval(this.indexNoticeInfo.scrollInterval)
    }
    window.removeEventListener('resize', this.checkPoolScroll)
  },
  methods: {
    async _loadPropertyIndexNotices() {
      const param = {
        page: 1,
        row: 10,
        communityId: this.getCommunityId()
      }

      try {
        const res = await getNotepadList(param)
        this.indexNoticeInfo.notices = res.data
        this.$nextTick(() => {
          this.checkPoolScroll()
        })
      } catch (error) {
        console.error('请求失败处理', error)
      }
    },
    checkPoolScroll() {
      const element = document.getElementById('pool')
      if (!element) return
      
      const clientHeight = element.clientHeight
      const scrollHeight = element.scrollHeight
      
      if (scrollHeight > clientHeight) {
        if (!this.indexNoticeInfo.scrollInterval) {
          this.indexNoticeInfo.scrollInterval = setInterval(this.poolScroll, 5000)
        }
      } else {
        clearInterval(this.indexNoticeInfo.scrollInterval)
        this.indexNoticeInfo.scrollInterval = null
      }
    },
    poolScroll() {
      const element = document.getElementById('pool')
      if (!element) return
      
      const clientHeight = element.clientHeight
      const scrollHeight = element.scrollHeight
      const canScrollHeight = scrollHeight - clientHeight
      
      if (this.indexNoticeInfo.scrollTopValue <= 0) {
        this.indexNoticeInfo.scrollDirection = true
      }
      
      if (this.indexNoticeInfo.scrollDirection) {
        this.indexNoticeInfo.scrollTopValue += canScrollHeight / 1200
      } else {
        this.indexNoticeInfo.scrollTopValue -= canScrollHeight / 1200
      }
      
      if (canScrollHeight <= this.indexNoticeInfo.scrollTopValue) {
        this.indexNoticeInfo.scrollDirection = !this.indexNoticeInfo.scrollDirection
      }
      
      element.scrollTop = this.indexNoticeInfo.scrollTopValue
    }
  }
}
</script>

<style lang="scss" scoped>
.index-1-right-1 {
  background: #fff;
  border-radius: 4px;
  padding: 15px;
  height: 100%;
  
  .index-title {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 15px;
    color: #333;
  }
  
  .notice-list {
    height: 300px;
    overflow-y: auto;
    padding: 0;
    margin: 0;
    list-style: none;
    
    .no-data {
      color: #999;
      text-align: center;
      padding: 20px 0;
    }
    
    .notice-item {
      padding: 8px 0;
      border-bottom: 1px solid #eee;
      
      &:last-child {
        border-bottom: none;
      }
      
      .vc-index-notice-title {
        font-size: 14px;
        color: #333;
        width: 70%;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        text-align: left;
      }
      
      .vc-index-notice-time {
        font-size: 12px;
        color: #999;
        width: 30%;
        text-align: right;
      }
    }
  }
  
  .padding-top-sm {
    padding-top: 5px;
  }
}
</style>