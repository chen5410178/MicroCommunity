<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-card class="box-card">
      <div slot="header" class=" flex justify-between">
        <span>{{ $t('communitySpacePersonManage.searchConditions') }}</span>
        <el-button type="text" style="float: right; padding: 3px 0" @click="moreCondition = !moreCondition">{{
          moreCondition ? $t('communitySpacePersonManage.hide') : $t('communitySpacePersonManage.more') }}</el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-date-picker v-model="conditions.appointmentTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"            :placeholder="$t('communitySpacePersonManage.appointmentTimePlaceholder')" style="width:100%;"
             />
        </el-col>
        <el-col :span="6">
          <el-input v-model="conditions.personName"
            :placeholder="$t('communitySpacePersonManage.appointmentPersonPlaceholder')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="conditions.personTel"
            :placeholder="$t('communitySpacePersonManage.appointmentTelPlaceholder')" clearable />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="_queryCommunitySpacePersonMethod">{{ $t('communitySpacePersonManage.search')
          }}</el-button>
          <el-button @click="_resetCommunitySpacePersonMethod">{{ $t('communitySpacePersonManage.reset') }}</el-button>
        </el-col>
      </el-row>
      <el-row v-show="moreCondition" :gutter="20" style="margin-top: 20px;">
        <el-col :span="6">
          <el-select v-model="conditions.state" :placeholder="$t('communitySpacePersonManage.statePlaceholder')" clearable
            style="width:100%;">
            <el-option v-for="(value, key) in stateOptions" :key="key" :label="value" :value="key" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="conditions.spaceId" :placeholder="$t('communitySpacePersonManage.spacePlaceholder')"
            clearable style="width:100%;">
            <el-option v-for="item in spaces" :key="item.spaceId" :label="item.name" :value="item.spaceId" />
          </el-select>
        </el-col>
      </el-row>
    </el-card>

    <!-- 预约订单列表 -->
    <el-card class="box-card" style="margin-top:20px;">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('communitySpacePersonManage.appointmentOrder') }}</span>
      </div>
      <el-table v-loading="loading" :data="communitySpacePersons" border style="width: 100%">
        <el-table-column prop="cspId" :label="$t('communitySpacePersonManage.orderId')" align="center" />
        <el-table-column prop="venueName" :label="$t('communitySpacePersonManage.venue')" align="center" />
        <el-table-column prop="spaceName" :label="$t('communitySpacePersonManage.space')" align="center" />
        <el-table-column prop="personName" :label="$t('communitySpacePersonManage.appointmentPerson')" align="center" />
        <el-table-column prop="personTel" :label="$t('communitySpacePersonManage.appointmentTel')" align="center" />
        <el-table-column prop="appointmentTime" :label="$t('communitySpacePersonManage.appointmentDate')"
          align="center" />
        <el-table-column :label="$t('communitySpacePersonManage.appointmentTime')" align="center">
          <template slot-scope="scope">
            <div v-for="(time, index) in scope.row.times" :key="index">
              <span> {{ time.hours }}({{ time.state == 'W'? $t('communitySpacePersonManage.stateWaitConfirm'): $t('communitySpacePersonManage.stateConfirm') }})</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="receivableAmount" :label="$t('communitySpacePersonManage.receivableAmount')"
          align="center" />
        <el-table-column prop="receivedAmount" :label="$t('communitySpacePersonManage.receivedAmount')" align="center" />
        <el-table-column prop="payWayName" :label="$t('communitySpacePersonManage.payWay')" align="center" />
        <el-table-column prop="stateName" :label="$t('communitySpacePersonManage.state')" align="center" />
        <el-table-column prop="createTime" :label="$t('communitySpacePersonManage.createTime')" align="center" />
        <el-table-column prop="remark" :label="$t('communitySpacePersonManage.remark')" align="center" />
        <el-table-column :label="$t('communitySpacePersonManage.operation')" align="center" width="150">
          <template slot-scope="scope">
            <el-button v-if="scope.row.state === 'S'" type="text" size="small"
              @click="_openDeleteCommunitySpacePersonModel(scope.row)">
              {{ $t('communitySpacePersonManage.cancelAppointment') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        layout="total, sizes, prev, pager, next, jumper" :total="page.total" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 删除确认对话框 -->
    <delete-community-space-person ref="deleteDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import DeleteCommunitySpacePerson from '@/components/community/deleteCommunitySpacePerson'
import { listCommunitySpacePerson, listCommunitySpace } from '@/api/community/communitySpacePersonManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CommunitySpacePersonManageList',
  components: {
    DeleteCommunitySpacePerson
  },
  data() {
    return {
      loading: false,
      moreCondition: false,
      conditions: {
        spaceId: '',
        personName: '',
        personTel: '',
        appointmentTime: '',
        state: '',
        communityId: ''
      },
      communitySpacePersons: [],
      spaces: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      stateOptions: {
        'S': this.$t('communitySpacePersonManage.stateOptions.S'),
        'F': this.$t('communitySpacePersonManage.stateOptions.F'),
        'W': this.$t('communitySpacePersonManage.stateOptions.W'),
        'P': this.$t('communitySpacePersonManage.stateOptions.P')
      }
    }
  },
  created() {
    this.conditions.communityId = getCommunityId()
    this._listCommunitySpacePersonCommunitySpaces()
    this._listCommunitySpacePersons(this.page.current, this.page.size)
  },
  methods: {
    // 获取预约订单列表
    async _listCommunitySpacePersons(page, size) {
      this.loading = true
      try {
        const params = {
          ...this.conditions,
          page,
          row: size
        }
        const res = await listCommunitySpacePerson(params )
        this.communitySpacePersons = res.data
        this.page.total = res.total
        this.page.current = page
      } catch (error) {
        this.$message.error(error.message || this.$t('common.fetchError'))
      } finally {
        this.loading = false
      }
    },
    // 获取场地列表
    async _listCommunitySpacePersonCommunitySpaces() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.conditions.communityId
        }
        const res = await listCommunitySpace( params )
        this.spaces = res.data
      } catch (error) {
        this.$message.error(error.message || this.$t('common.fetchError'))
      }
    },
    // 查询
    _queryCommunitySpacePersonMethod() {
      this.page.current = 1
      this._listCommunitySpacePersons(this.page.current, this.page.size)
    },
    // 重置
    _resetCommunitySpacePersonMethod() {
      this.conditions = {
        spaceId: '',
        personName: '',
        personTel: '',
        appointmentTime: '',
        state: '',
        communityId: this.conditions.communityId
      }
      this._queryCommunitySpacePersonMethod()
    },
    // 打开删除确认模态框
    _openDeleteCommunitySpacePersonModel(row) {
      this.$refs.deleteDialog.open(row)
    },
    // 删除成功后刷新列表
    handleSuccess() {
      this._listCommunitySpacePersons(this.page.current, this.page.size)
    },
    // 分页大小改变
    handleSizeChange(val) {
      this.page.size = val
      this._listCommunitySpacePersons(this.page.current, val)
    },
    // 当前页改变
    handleCurrentChange(val) {
      this.page.current = val
      this._listCommunitySpacePersons(val, this.page.size)
    },
    formatTimes(times) {
      return times.map(t => t.hours).join(', ');
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}

.box-card {
  margin-bottom: 20px;
}

.el-row {
  margin-bottom: 10px;
}
</style>