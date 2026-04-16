<template>
  <div class="market-way-container">
    <el-card>
      <div class="header flex justify-between">
        <span>{{ $t('marketWay.title') }}</span>
      </div>

      <el-tabs v-model="activeTab" @tab-click="handleTabClick(activeTab)">
        <el-tab-pane :label="$t('marketWay.tabText')" name="marketWayText">
          <market-way-text ref="marketWayText" />
        </el-tab-pane>
        <el-tab-pane :label="$t('marketWay.tabPic')" name="marketWayPic">
          <market-way-pic ref="marketWayPic" />
        </el-tab-pane>
        <el-tab-pane :label="$t('marketWay.tabGoods')" name="marketWayGoods">
          <market-way-goods ref="marketWayGoods" />
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <view-image ref="viewImage" />
  </div>
</template>

<script>
import MarketWayText from '@/components/market/MarketWayText'
import MarketWayPic from '@/components/market/MarketWayPic'
import MarketWayGoods from '@/components/market/MarketWayGoods'
import ViewImage from '@/components/community/viewImage'

export default {
  name: 'MarketWayList',
  components: {
    MarketWayText,
    MarketWayPic,
    MarketWayGoods,
    ViewImage
  },
  data() {
    return {
      activeTab: 'marketWayText'
    }
  },
  methods: {
    handleTabClick(tab) {
      setTimeout(() => {
        this.$refs[tab].refreshList()

      }, 500);
    }
  }
}
</script>

<style lang="scss" scoped>
.market-way-container {
  padding: 20px;

  .header {
    margin-bottom: 20px;
    text-align: center;
  }
}
</style>