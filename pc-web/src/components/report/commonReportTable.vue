<template>
  <div class="common-report-table">
    <div v-for="(item, index) in commonReportTableInfo.components" :key="index">
      <el-card v-if="item.conditions && item.conditions.length > 0" class="query-card">
        <div slot="header" class="flex justify-between">
          <span>{{ $t('commonReportTable.queryConditions') }}</span>
        </div>
        <el-row :gutter="20">
          <el-col v-for="(conditionItem, conditionIndex) in item.conditions" :key="conditionIndex" :span="6" class="margin-bottom">
              <div class="el-input">
                <input class="el-input__inner"
                  v-model.trim="conditionItem.value"
                  :type="conditionItem.type"
                  :placeholder="conditionItem.holdpace" />
              </div>
          </el-col>
          <el-col :span="6">
            <el-button type="primary" @click="_queryReportTableMethod(item)">
              <i class="el-icon-search"></i>
              {{ $t('commonReportTable.query') }}
            </el-button>
            <el-button @click="_resetReportTableMethod(item)">
              <i class="el-icon-refresh"></i>
              {{ $t('commonReportTable.reset') }}
            </el-button>
          </el-col>
        </el-row>
      </el-card>

      <el-card class="table-card">
        <div slot="header" class="flex justify-between">
          <span>{{ item.componentName }}</span>
          <div style="float: right;">
            <el-button type="primary" size="small" @click="_exportReportTableMethod(item)">
              <i class="el-icon-download"></i>
              {{ $t('commonReportTable.export') }}
            </el-button>
            <el-button type="primary" size="small" @click="_printReportTableMethod(item)">
              <i class="el-icon-printer"></i>
              {{ $t('commonReportTable.print') }}
            </el-button>
          </div>
        </div>

        <el-table :data="item.data" border style="width: 100%" v-loading="loading">
          <el-table-column v-for="(itemTh, indexTh) in item.th" :key="indexTh" :label="itemTh" align="center">
            <template slot-scope="scope">
              <div class="text-auto">
                {{ scope.row[itemTh] }}
              </div>
            </template>
          </el-table-column>
        </el-table>

        <div v-if="item.footer" class="footer-wrapper">
          <el-row :gutter="20">
            <el-col v-for="(tmpItemTh, key) in item.footer" :key="key" :span="4">
              <div class="footer-item">
                {{ key }}: {{ tmpItemTh }}
              </div>
            </el-col>
          </el-row>
        </div>

        <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
          :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </el-card>
    </div>
  </div>
</template>

<script>
import {
  listReportCustomComponentRel,
  listReportCustomComponentCondition,
  listReportCustomComponentData,
  listReportCustomComponentDataFooter,
  exportData
} from '@/api/report/commonReportApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CommonReportTable',
  data() {
    return {
      commonReportTableInfo: {
        components: [],
        customId: ''
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      loading: false,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    handleSwitch(value) {
      this.commonReportTableInfo.customId = value.customId
      this._listReportCustomTableComponent()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this._queryReportTableMethod(this.commonReportTableInfo.components[0])
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this._queryReportTableMethod(this.commonReportTableInfo.components[0])
    },
    async _queryReportTableMethod(item) {
      const condition = {}
      item.conditions.forEach(_item => {
        condition[_item.param] = _item.value
      })
      await this._listReportCustomTableDatas(this.pagination.current, this.pagination.size, item, condition)
      await this._listReportCustomTableFooter(this.pagination.current, this.pagination.size, item, condition)
    },
    _resetReportTableMethod(item) {
      const condition = {}
      item.conditions.forEach(_item => {
        _item.value = ''
        condition[_item.param] = _item.value
      })
      this._listReportCustomTableDatas(1, this.pagination.size, item, condition)
      this._listReportCustomTableFooter(1, this.pagination.size, item, condition)
    },
    async _listReportCustomTableComponent() {
      try {
        this.loading = true
        const params = {
          page: 1,
          row: this.pagination.size,
          customId: this.commonReportTableInfo.customId,
          componentType: '1001'
        }
        const { data } = await listReportCustomComponentRel(params)
        this.commonReportTableInfo.components = data
        for (const item of this.commonReportTableInfo.components) {
          await this._listReportCustomTableConditions(item)
          await this._listReportCustomTableDatas(1, this.pagination.size, item)
          await this._listReportCustomTableFooter(1, this.pagination.size, item)
        }
      } catch (error) {
        console.error('Failed to load report components:', error)
      } finally {
        this.loading = false
      }
    },
    async _listReportCustomTableConditions(component) {
      try {
        const params = {
          page: 1,
          row: this.pagination.size,
          componentId: component.componentId
        }
        const { data } = await listReportCustomComponentCondition(params)
        component.conditions = data
        // 确保每个条件项都包含响应式的 value 字段，避免 v-model 绑定后不显示
        component.conditions.forEach(cond => {
          if (!Object.prototype.hasOwnProperty.call(cond, 'value')) {
            this.$set(cond, 'value', '')
          }
        })
      } catch (error) {
        console.error('Failed to load conditions:', error)
      }
    },
    async _listReportCustomTableDatas(page, row, component, conditions) {
      try {
        this.loading = true
        const params = {
          page,
          row,
          componentId: component.componentId,
          communityId: this.communityId,
          ...conditions
        }
        const { data, total } = await listReportCustomComponentData(params)
        component.th = data.th
        component.data = data.td
        this.pagination.total = total
      } catch (error) {
        console.error('Failed to load table data:', error)
      } finally {
        this.loading = false
      }
    },
    async _listReportCustomTableFooter(page, row, component, conditions) {
      try {
        const params = {
          page,
          row,
          componentId: component.componentId,
          communityId: this.communityId,
          ...conditions
        }
        const { data } = await listReportCustomComponentDataFooter(params)
        component.footer = data
      } catch (error) {
        console.error('Failed to load footer data:', error)
      }
    },
    async _exportReportTableMethod(item) {
      try {
        const condition = {}
        item.conditions.forEach(_item => {
          condition[_item.param] = _item.value
        })
        const params = {
          page: 1,
          row: 10000,
          componentId: item.componentId,
          communityId: this.communityId,
          pagePath: 'exportCustomReportTableData',
          ...condition
        }
        const { code, msg } = await exportData(params)
        this.$message.success(msg)
        if (code === 0) {
          this.$router.push('/pages/property/downloadTempFile?tab=downloadCenter')
        }
      } catch (error) {
        console.error('Export failed:', error)
      }
    },
    _printReportTableMethod(data) {
      localStorage.setItem('printCommonReportTableData', JSON.stringify(data))
      window.open('/#/pages/property/printCommonReportTable')
    }
  }
}
</script>

<style lang="scss" scoped>
.common-report-table {
  .query-card {
    margin-bottom: 20px;
  }

  .table-card {
    margin-bottom: 20px;
  }

  .text-auto {
    max-width: 200px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .footer-wrapper {
    margin-top: 20px;
    padding: 10px;
    background-color: #f5f7fa;
    border-radius: 4px;

    .footer-item {
      padding: 5px 0;
    }
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>