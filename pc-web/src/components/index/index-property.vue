<template>
  <div class="property-index-container">
    <div class="vc-index-nav">
      <span><i class="el-icon-s-home margin-right-sm"></i>{{ $t('propertyIndex.home') }}</span>
      <span class="margin-left-sm margin-right-sm">/</span>
      <span>{{ $t('propertyIndex.dashboard') }}</span>
    </div>

    <el-row class="vc-index-1" :gutter="20">
      <el-col :span="15">
        <index-community ref="indexCommunity" />
      </el-col>
      <el-col :span="9">
        <index-notice ref="indexNotice" />
      </el-col>
    </el-row>

    <el-row class="vc-index-1" :gutter="20">
      <el-col :span="15">
        <index-repair-complaint ref="indexRepairComplaint" />
      </el-col>
      <el-col :span="9">
        <index-owner-room ref="indexOwnerRoom" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import IndexCommunity from '@/components/index/indexCommunity'
import IndexNotice from '@/components/index/indexNotice'
import IndexRepairComplaint from '@/components/index/indexRepairComplaint'
import IndexOwnerRoom from '@/components/index/indexOwnerRoom'

export default {
  name: 'PropertyIndexList',
  components: {
    IndexCommunity,
    IndexNotice,
    IndexRepairComplaint,
    IndexOwnerRoom
  },
  mounted() {
    this.initData()
  },
  methods: {
    initData() {
      this.$refs.indexCommunity._loadPropertyIndexAssets()
      this.$refs.indexNotice._loadPropertyIndexNotices()
      this.$refs.indexRepairComplaint.initData()
      this.$refs.indexOwnerRoom._loadIndexOwnerRegisterData()
    }
  }
}
</script>

<style lang="scss" scoped>
.property-index-container {

  padding: 0 10px; // 添加左右内边距

  .vc-index-nav {
    padding: 10px;
    font-size: 16px;
    text-align: left;
  }

  .margin-right-sm {
    margin-right: 5px;
  }

  .margin-left-sm {
    margin-left: 5px;
  }

  .margin-top-lg {
    margin-top: 15px;
  }

  .margin-bottom {
    margin-bottom: 20px;
  }

  .vc-index-1 {
    margin-bottom: 20px;
    // 修复 Element UI 栅格系统的 gutter 问题
  

    .index-1-left,
    .index-1-right {
      height: 100%;
    }
  }
}
</style>