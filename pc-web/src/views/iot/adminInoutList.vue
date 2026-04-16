<template>
  <div class="admin-inout-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <select-admin-community @change-community="handleCommunityChange" />
      </el-col>
      <el-col :span="20">
        <el-card class="box-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminInout.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-select v-model="conditions.machineId" :placeholder="$t('adminInout.search.selectAccessControl')"
                clearable class="w-100">
                <el-option v-for="item in accessControls" :key="item.machineId" :label="item.machineName"
                  :value="item.machineId" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-input v-model="conditions.name" :placeholder="$t('adminInout.search.inputUserName')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model="conditions.tel" :placeholder="$t('adminInout.search.inputPhone')" clearable />
            </el-col>
            <el-col :span="4">
              <el-select v-model="conditions.state" :placeholder="$t('adminInout.search.selectOpenStatus')" clearable
                class="w-100">
                <el-option :label="$t('adminInout.openSuccess')" value="C" />
                <el-option :label="$t('adminInout.openFailed')" value="F" />
              </el-select>
            </el-col>
            <el-col :span="2">
              <el-button type="primary" @click="handleQuery">
                {{ $t('common.search') }}
              </el-button>
            </el-col>
          </el-row>
          <el-row :gutter="20" class="mt-20">
            <el-col :span="4">
              <el-date-picker v-model="conditions.queryStartTime" type="datetime"
                :placeholder="$t('adminInout.search.inputStartTime')" class="w-100" />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="conditions.queryEndTime" type="datetime"
                :placeholder="$t('adminInout.search.inputEndTime')" class="w-100" />
            </el-col>
          </el-row>
        </el-card>

        <el-card class="box-card mt-20">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminInout.list.title') }}</span>
          </div>
          <el-table v-loading="loading" :data="adminInouts" border style="width: 100%">
            <el-table-column :label="$t('adminInout.table.face')" align="center">
              <template slot-scope="scope">
                <el-image style="width: 60px; height: 60px; cursor: pointer;"
                  :src="scope.row.facePath || '/img/noPhoto.jpg'" :preview-src-list="[scope.row.facePath]" fit="cover" />
              </template>
            </el-table-column>
            <el-table-column prop="communityName" :label="$t('adminInout.table.communityName')" align="center" />
            <el-table-column prop="inoutId" :label="$t('adminInout.table.number')" align="center" />
            <el-table-column prop="machineName" :label="$t('adminInout.table.deviceName')" align="center" />
            <el-table-column prop="machineCode" :label="$t('adminInout.table.deviceCode')" align="center" />
            <el-table-column prop="name" :label="$t('adminInout.table.userName')" align="center" />
            <el-table-column :label="$t('adminInout.table.openType')" align="center">
              <template slot-scope="scope">
                {{ scope.row.openTypeCd === '1000' ? $t('adminInout.faceOpen') : $t('adminInout.otherOpen') }}
              </template>
            </el-table-column>
            <el-table-column prop="tel" :label="$t('adminInout.table.phone')" align="center">
              <template slot-scope="scope">
                {{ scope.row.tel || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="idCard" :label="$t('adminInout.table.idCard')" align="center">
              <template slot-scope="scope">
                {{ scope.row.idCard || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="similar" :label="$t('adminInout.table.similarity')" align="center" />
            <el-table-column :label="$t('adminInout.table.openStatus')" align="center">
              <template slot-scope="scope">
                {{ scope.row.state === 'C' ? $t('adminInout.openSuccess') : $t('adminInout.openFailed') }}
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('adminInout.table.openTime')" align="center" />
          </el-table>
          <el-pagination class="mt-20" :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]"
            :page-size="page.size" :total="page.total" layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import SelectAdminCommunity from '@/components/community/selectAdminCommunity'
import { listAdminAccessControlInout, listAdminAccessControl } from '@/api/iot/adminInoutApi'

export default {
  name: 'AdminInoutList',
  components: {
    SelectAdminCommunity
  },
  data() {
    return {
      loading: false,
      adminInouts: [],
      accessControls: [],
      conditions: {
        machineId: '',
        name: '',
        tel: '',
        state: '',
        queryStartTime: '',
        queryEndTime: '',
        communityId: '',
        page: 1,
        row: 10
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.listAccessControls()
  },
  methods: {
    async listAdminInouts() {
      try {
        this.loading = true
        const res = await listAdminAccessControlInout(this.conditions)
        this.adminInouts = res.data
        this.page.total = res.total
      } catch (error) {
        console.error('Failed to load inout records:', error)
      } finally {
        this.loading = false
      }
    },
    async listAccessControls() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.conditions.communityId
        }
        const res = await listAdminAccessControl(params)
        this.accessControls = res.data
      } catch (error) {
        console.error('Failed to load access controls:', error)
      }
    },
    handleCommunityChange(community) {
      this.conditions.communityId = community.communityId
      this.listAccessControls()
      this.listAdminInouts()
    },
    handleQuery() {
      this.conditions.page = 1
      this.listAdminInouts()
    },
    handleSizeChange(size) {
      this.conditions.row = size
      this.listAdminInouts()
    },
    handleCurrentChange(current) {
      this.conditions.page = current
      this.listAdminInouts()
    }
  }
}
</script>

<style scoped>
.admin-inout-container {
  padding: 20px;
}

.mt-20 {
  margin-top: 20px;
}

.w-100 {
  width: 100%;
}
</style>