<template>
  <div class="meter-type-manage-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('meterTypeManage.title') }}</span>
        <div class="header-tools">
          <el-button type="primary" size="small" @click="openAddMeterTypeModal">
            <i class="el-icon-plus"></i>{{ $t('common.add') }}
          </el-button>
          <el-button size="small" @click="goBack">{{ $t('common.back') }}</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="meterTypeList" border style="width: 100%">
        <el-table-column prop="typeId" :label="$t('meterTypeManage.typeId')" align="center" />
        <el-table-column prop="typeName" :label="$t('meterTypeManage.typeName')" align="center" />
        <el-table-column prop="ratio" :label="$t('meterTypeManage.ratio')" align="center" />
        <el-table-column prop="remark" :label="$t('meterTypeManage.remark')" align="center" />
        <el-table-column prop="createTime" :label="$t('meterTypeManage.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="openEditMeterTypeModal(scope.row)">{{ $t('common.edit') }}</el-button>
            <el-button size="mini" type="danger" @click="openDeleteMeterTypeModal(scope.row)">{{ $t('common.delete')
              }}</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="18" class="text-left table-desc">
          <div>{{ $t('meterTypeManage.tip') }}</div>
        </el-col>
        <el-col :span="6">
          <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-col>
      </el-row>
    </el-card>

    <add-meter-type ref="addMeterType" @success="handleSuccess" />
    <edit-meter-type ref="editMeterType" @success="handleSuccess" />
    <delete-meter-type ref="deleteMeterType" @success="handleSuccess" />
  </div>
</template>

<script>
import { listMeterType } from '@/api/fee/meterTypeManageApi'
import AddMeterType from '@/components/fee/addMeterType'
import EditMeterType from '@/components/fee/editMeterType'
import DeleteMeterType from '@/components/fee/deleteMeterType'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'MeterTypeManageList',
  components: {
    AddMeterType,
    EditMeterType,
    DeleteMeterType
  },
  data() {
    return {
      loading: false,
      meterTypeList: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: this.communityId
        }
        const { data, total } = await listMeterType(params)
        this.meterTypeList = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('meterTypeManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    openAddMeterTypeModal() {
      this.$refs.addMeterType.open()
    },
    openEditMeterTypeModal(row) {
      this.$refs.editMeterType.open(row)
    },
    openDeleteMeterTypeModal(row) {
      this.$refs.deleteMeterType.open(row)
    },
    handleSuccess() {
      this.getList()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.meter-type-manage-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .header-tools {
    float: right;
    margin-top: -5px;
  }

  .margin-top {
    margin-top: 20px;
  }
}
</style>