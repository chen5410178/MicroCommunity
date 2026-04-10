<template>
  <div class="common-report-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card class="tree-card">
          <ul class="report-list">
            <li v-for="(item, index) in commonReportInfo.reportCustoms" :key="index"
              :class="{ 'selected-item': commonReportInfo.switchValue === item.customId }" @click="swatch(item)">
              {{ item.title }}
            </li>
          </ul>
        </el-card>
      </el-col>
      <el-col :span="20">

        <common-report-table ref="reportTable" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import CommonReportTable from '@/components/report/commonReportTable'
import { listReportCustom } from '@/api/report/commonReportApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CommonReportList',
  components: {
    CommonReportTable
  },
  data() {
    return {
      commonReportInfo: {
        groupId: '',
        switchValue: '',
        reportCustoms: []
      },
      communityId: ''
    }
  },
  watch: {
    '$route'(to, from) {
      // 对路由变化作出响应...
      console.log(to, from)
      if (to.query.groupId !== from.query.groupId) {
        this.commonReportInfo.groupId = to.query.groupId
        this._loadReportCustom()
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.commonReportInfo.groupId = this.$route.query.groupId
    this._loadReportCustom()
  },
  methods: {
    swatch(value) {
      this.commonReportInfo.switchValue = value.customId
      this.$refs.reportTable.handleSwitch(value)
    },
    async _loadReportCustom() {
      try {
        const params = {
          page: 1,
          row: 50,
          groupId: this.commonReportInfo.groupId
        }
        const { data } = await listReportCustom(params)
        this.commonReportInfo.reportCustoms = data
        if (data && data.length > 0) {
          this.swatch(data[0])
        }
      } catch (error) {
        console.error('Failed to load report custom:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.common-report-container {
  padding: 20px;

  .tree-card {
    height: 100%;

    .report-list {
      list-style: none;
      padding: 0;
      margin: 0;

      li {
        padding: 10px;
        margin-bottom: 5px;
        text-align: center;
        cursor: pointer;
        border-radius: 4px;
        transition: all 0.3s;

        &:hover {
          background-color: #f5f7fa;
        }

        &.selected-item {
          background-color: #409eff;
          color: white;
        }
      }
    }
  }
}
</style>