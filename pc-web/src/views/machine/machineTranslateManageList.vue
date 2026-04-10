<template>
  <div class="machine-translate-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('machineTranslateManage.search.title') }}</span>
        <!-- <el-button style="float: right; padding: 3px 0" type="text" @click="toggleMoreCondition">
          {{ machineTranslateManageInfo.moreCondition ? $t('common.hide') : $t('common.more') }}
        </el-button> -->
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-select v-model="machineTranslateManageInfo.conditions.typeCd"
            :placeholder="$t('machineTranslateManage.search.typeCd')" style="width: 100%">
            <el-option :label="$t('machineTranslateManage.search.selectType')" value="" />
            <el-option v-for="(item, index) in machineTranslateManageInfo.typeCds" :key="index" :label="item.name"
              :value="item.statusCd" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-input v-model="machineTranslateManageInfo.conditions.objId"
            :placeholder="$t('machineTranslateManage.search.objId')" />
        </el-col>
        <el-col :span="6">
          <el-input v-model="machineTranslateManageInfo.conditions.objName"
            :placeholder="$t('machineTranslateManage.search.objName')" />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="queryMachineTranslateMethod">
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="resetMachineTranslateMethod">
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('machineTranslateManage.list.title') }}</span>
      </div>
      <el-table v-loading="loading" :data="machineTranslateManageInfo.machineTranslates" border style="width: 100%">
        <el-table-column prop="objId" :label="$t('machineTranslateManage.table.objId')" align="center" />
        <el-table-column prop="typeCdName" :label="$t('machineTranslateManage.table.typeCd')" align="center" />
        <el-table-column prop="objName" :label="$t('machineTranslateManage.table.objName')" align="center" />
        <el-table-column prop="machineCmdName" :label="$t('machineTranslateManage.table.machineCmd')" align="center" />
        <el-table-column prop="stateName" :label="$t('machineTranslateManage.table.state')" align="center" />
        <el-table-column prop="remark" :label="$t('machineTranslateManage.table.remark')" align="center" width="80" />
        <el-table-column prop="updateTime" :label="$t('machineTranslateManage.table.updateTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="120">
          <template slot-scope="scope">
            <el-button v-if="scope.row.state !== '20000'" size="mini" type="primary"
              @click="openEditMachineTranslateModel(scope.row)">
              {{ $t('machineTranslateManage.button.resync') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 组件 -->
    <add-machine-translate ref="addMachineTranslate" @success="handleSuccess" />
    <edit-machine-translate ref="editMachineTranslate" @success="handleSuccess" />
  </div>
</template>

<script>
import { listMachineTranslates } from '@/api/machine/machineTranslateManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import AddMachineTranslate from '@/components/machine/addMachineTranslate'
import EditMachineTranslate from '@/components/machine/editMachineTranslate'

export default {
  name: 'MachineTranslateManageList',
  components: {
    AddMachineTranslate,
    EditMachineTranslate
  },
  data() {
    return {
      loading: false,
      communityId: '',
      machineTranslateManageInfo: {
        machineTranslates: [],
        total: 0,
        records: 1,
        moreCondition: false,
        typeCds: [],
        conditions: {
          typeCd: '',
          objName: '',
          objId: '',
          communityId: ''
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
    this.communityId = getCommunityId()
    this.machineTranslateManageInfo.conditions.communityId = this.communityId
    this.initData()
  },
  methods: {
    async initData() {
      try {
        await this.getDictData()
        this.listMachineTranslates()
      } catch (error) {
        console.error('初始化数据失败:', error)
      }
    },
    async getDictData() {
      try {
        const data = await getDict('machine_translate', 'type_cd')
        this.machineTranslateManageInfo.typeCds = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async listMachineTranslates() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          ...this.machineTranslateManageInfo.conditions
        }
        const { machineTranslates, total } = await listMachineTranslates(params)
        this.machineTranslateManageInfo.machineTranslates = machineTranslates
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('machineTranslateManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    queryMachineTranslateMethod() {
      this.page.current = 1
      this.listMachineTranslates()
    },
    resetMachineTranslateMethod() {
      this.machineTranslateManageInfo.conditions = {
        typeCd: '',
        objName: '',
        objId: '',
        communityId: this.communityId
      }
      this.listMachineTranslates()
    },
    toggleMoreCondition() {
      this.machineTranslateManageInfo.moreCondition = !this.machineTranslateManageInfo.moreCondition
    },
    openEditMachineTranslateModel(row) {
      this.$refs.editMachineTranslate.open(row)
    },
    handleSuccess() {
      this.listMachineTranslates()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.listMachineTranslates()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.listMachineTranslates()
    }
  }
}
</script>

<style lang="scss" scoped>
.machine-translate-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: 20px;
    }
  }

  .list-wrapper {
    margin-bottom: 20px;

    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>