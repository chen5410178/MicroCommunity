<template>
  <div class="padding">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('repairReturnVisit.queryTitle') }}</span>
          </div>
          <el-form :model="searchForm" label-width="0">
            <el-row :gutter="15">
              <el-col :sm="6" :md="4">
                <el-input v-model.trim="searchForm.repairId" :placeholder="$t('repairReturnVisit.repairIdPlaceholder')"
                  clearable />
              </el-col>
              <el-col :sm="6" :md="4">
                <el-select v-model="searchForm.repairType" :placeholder="$t('repairReturnVisit.repairTypePlaceholder')"
                  clearable style="width:100%">
                  <el-option v-for="item in repairSettings" :key="item.repairType" :label="item.repairTypeName"
                    :value="item.repairType" />
                </el-select>
              </el-col>
              <el-col :sm="6" :md="4">
                <el-input v-model.trim="searchForm.repairName"
                  :placeholder="$t('repairReturnVisit.repairNamePlaceholder')" clearable />
              </el-col>
              <el-col :sm="6" :md="4">
                <el-input v-model.trim="searchForm.tel" :placeholder="$t('repairReturnVisit.telPlaceholder')"
                  clearable />
              </el-col>
              <el-col :sm="6" :md="4">
                <el-select v-model="searchForm.state" :placeholder="$t('repairReturnVisit.statePlaceholder')" clearable
                  style="width:100%">
                  <el-option v-for="item in states" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
              </el-col>
              <el-col :sm="6" :md="4">
                <el-button type="primary" icon="el-icon-search" @click="queryRepairPool">
                  {{ $t('common.search') }}
                </el-button>
                <el-button icon="el-icon-refresh" @click="resetRepairPool">
                  {{ $t('common.reset') }}
                </el-button>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('repairReturnVisit.listTitle') }}</span>
          </div>
          <el-table v-loading="loading" :data="repairPools" border stripe style="width: 100%">
            <el-table-column prop="repairId" :label="$t('repairReturnVisit.repairId')" align="center" />
            <el-table-column prop="repairObjName" :label="$t('repairReturnVisit.location')" align="center" />
            <el-table-column prop="repairTypeName" :label="$t('repairReturnVisit.repairType')" align="center" />
            <el-table-column prop="repairName" :label="$t('repairReturnVisit.repairName')" align="center" />
            <el-table-column prop="tel" :label="$t('repairReturnVisit.contact')" align="center" />
            <el-table-column prop="appointmentTime" :label="$t('repairReturnVisit.appointmentTime')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="180">
              <template slot-scope="scope">
                <el-button v-if="!scope.row.visitId" type="primary" size="mini" @click="openEditOwnerRepair(scope.row)">
                  {{ $t('repairReturnVisit.visit') }}
                </el-button>
                <el-button size="mini" @click="openRepairDetail(scope.row)">
                  {{ $t('repairReturnVisit.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination class="mt-20" :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>

    <visit-owner-repair ref="visitModal" @success="listRepairPools" />
  </div>
</template>

<script>
import { getRepairReturnVisitList, listRepairSettings } from '@/api/work/repairReturnVisitApi'
import VisitOwnerRepair from '@/components/work/visitOwnerRepair'

export default {
  name: 'RepairReturnVisitList',
  components: { VisitOwnerRepair },
  data() {
    return {
      loading: false,
      searchForm: {
        repairId: '',
        repairType: '',
        repairName: '',
        tel: '',
        state: 'waiting'
      },
      repairPools: [],
      repairSettings: [],
      states: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  async created() {
    await this.loadDictData()
    this.listRepairPools()
    this.listRepairSettings()
  },
  methods: {
    async loadDictData() {
      try {
        this.states = [{
          value: 'waiting',
          label: '待回访'
        }, {
          value: 'finish',
          label: '已回访'
        }]
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async listRepairPools() {
      try {
        this.loading = true
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: this.getCommunityId()
        }

        // 清理空值
        Object.keys(params).forEach(key => {
          if (params[key] === '') delete params[key]
        })

        const { data, total } = await getRepairReturnVisitList(params)
        this.repairPools = data
        this.pagination.total = total
      } catch (error) {
        console.error('查询报修回访列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    async listRepairSettings() {
      try {
        const { data } = await listRepairSettings({
          page: 1,
          row: 50,
          communityId: this.getCommunityId()
        })
        this.repairSettings = data
      } catch (error) {
        console.error('查询报修类型设置失败:', error)
      }
    },
    queryRepairPool() {
      this.pagination.current = 1
      this.listRepairPools()
    },
    resetRepairPool() {
      this.searchForm = {
        repairId: '',
        repairType: '',
        repairName: '',
        tel: '',
        state: ''
      }
      this.queryRepairPool()
    },
    openRepairDetail(repairPool) {
      this.$router.push({
        path: '/views/work/repairDetail',
        query: { repairId: repairPool.repairId }
      })
    },
    openEditOwnerRepair(repairPool) {
      this.$refs.visitModal.open(repairPool)
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.listRepairPools()
    },
    handleCurrentChange(current) {
      this.pagination.current = current
      this.listRepairPools()
    }
  }
}
</script>

<style scoped>
.mt-20 {
  margin-top: 20px;
}
</style>