<template>
  <div class="component-condition-manage-container">

    <el-card>
      <div slot="header" class="clearfix">
        <span>{{ componentConditionManageInfo.componentName }} {{ $t('componentConditionManage.condition') }}</span>
        <div style="float: right;">
          <el-button size="small" @click="_goBack">{{ $t('common.back') }}</el-button>
          <el-button type="primary" size="small" @click="_openAddComponentConditionModal">
            <i class="el-icon-plus"></i>{{ $t('common.add') }}
          </el-button>
        </div>
      </div>

      <el-table :data="componentConditionManageInfo.componentConditions" border style="width: 100%">
        <el-table-column prop="conditionId" :label="$t('componentConditionManage.conditionId')" align="center" />
        <el-table-column prop="name" :label="$t('componentConditionManage.name')" align="center" />
        <el-table-column prop="holdpace" :label="$t('componentConditionManage.holdpace')" align="center" />
        <el-table-column prop="param" :label="$t('componentConditionManage.param')" align="center" />
        <el-table-column :label="$t('componentConditionManage.type')" align="center">
          <template slot-scope="scope">
            {{ scope.row.type == 'text' ? $t('componentConditionManage.text') : $t('componentConditionManage.date') }}
          </template>
        </el-table-column>
        <el-table-column prop="seq" :label="$t('componentConditionManage.seq')" align="center" />
        <el-table-column prop="remark" :label="$t('componentConditionManage.remark')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="_openEditComponentConditionModel(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="_openDeleteComponentConditionModel(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" style="margin-top: 20px;" />
    </el-card>

    <add-component-condition ref="addComponentCondition" @success="handleSuccess" />
    <edit-component-condition ref="editComponentCondition" @success="handleSuccess" />
    <delete-component-condition ref="deleteComponentCondition" @success="handleSuccess" />
  </div>
</template>

<script>
import { listReportCustomComponentCondition } from '@/api/report/componentConditionManageApi'
import AddComponentCondition from '@/components/report/AddComponentCondition'
import EditComponentCondition from '@/components/report/EditComponentCondition'
import DeleteComponentCondition from '@/components/report/DeleteComponentCondition'

export default {
  name: 'ComponentConditionManageList',
  components: {
    AddComponentCondition,
    EditComponentCondition,
    DeleteComponentCondition
  },
  data() {
    return {
      componentConditionManageInfo: {
        componentConditions: [],
        componentName: '',
        conditions: {
          componentId: '',
          name: '',
          param: ''
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.componentConditionManageInfo.conditions.componentId = this.$route.query.componentId
    this.componentConditionManageInfo.componentName = this.$route.query.componentName
    this._listComponentConditions()
  },
  methods: {
    async _listComponentConditions() {
      try {
        const params = {
          page: this.page.current,
          row: this.page.size,
          componentId: this.componentConditionManageInfo.conditions.componentId
        }
        const { data, total } = await listReportCustomComponentCondition(params)
        this.componentConditionManageInfo.componentConditions = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('componentConditionManage.fetchError'))
      }
    },
    _openAddComponentConditionModal() {
      this.$refs.addComponentCondition.open({
        componentId: this.componentConditionManageInfo.conditions.componentId
      })
    },
    _openEditComponentConditionModel(row) {
      this.$refs.editComponentCondition.open(row)
    },
    _openDeleteComponentConditionModel(row) {
      this.$refs.deleteComponentCondition.open(row)
    },
    _goBack() {
      this.$router.go(-1)
    },
    handleSuccess() {
      this._listComponentConditions()
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listComponentConditions()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listComponentConditions()
    }
  }
}
</script>

<style lang="scss" scoped>
.component-condition-manage-container {
  padding: 20px;
}
</style>