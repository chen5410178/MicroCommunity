<template>
  <div class="animated fadeInRight ecommerce">
    <el-row :gutter="20">
      <el-col :span="3">
        <div class="border-radius white-bg">
          <div class="margin-xs-r treeview">
            <ul class="list-group text-center border-radius">
              <li v-for="(item, index) in auditAuthOwnerInfo.states" :key="index" @click="_swatchState(item)"
                :class="{ 'vc-node-selected': auditAuthOwnerInfo.conditions.state == item.statusCd }"
                class="list-group-item node-orgTree">
                {{ item.name }}
              </li>
            </ul>
          </div>
        </div>
      </el-col>
      <el-col :span="21">
        <el-card class="box-card">
          <div slot="header" class="clearfix flex justify-between">
            <div>{{ $t('auditAuthOwner.searchTitle') }}</div>
          </div>
          <div class="">
            <el-row :gutter="20">
              <el-col :span="6">
                <el-input :placeholder="$t('auditAuthOwner.appUserNamePlaceholder')"
                  v-model="auditAuthOwnerInfo.conditions.appUserName" clearable />
              </el-col>
              <el-col :span="6">
                <el-input :placeholder="$t('auditAuthOwner.idCardPlaceholder')"
                  v-model="auditAuthOwnerInfo.conditions.idCard" clearable />
              </el-col>
              <el-col :span="6">
                <el-input :placeholder="$t('auditAuthOwner.linkPlaceholder')" v-model="auditAuthOwnerInfo.conditions.link"
                  clearable />
              </el-col>
              <el-col :span="6">
                <el-button type="primary" size="small" @click="_queryAuditAuthOwnerMethod">
                  {{ $t('common.search') }}
                </el-button>
                <el-button type="info" size="small" @click="_resetAuditAuthOwnerMethod">
                  {{ $t('common.reset') }}
                </el-button>
              </el-col>
            </el-row>
          </div>
        </el-card>

        <el-card class="box-card mt-20">
          <div slot="header" class="clearfix flex justify-between">
            <div>{{ $t('auditAuthOwner.authTitle') }}</div>
          </div>
          <div class="">
            <el-table :data="auditAuthOwnerInfo.appUsers" border style="width: 100%" v-loading="loading">
              <el-table-column prop="communityName" :label="$t('auditAuthOwner.communityName')" align="center" />
              <el-table-column :label="$t('auditAuthOwner.owner')" align="center">
                <template slot-scope="scope">
                  <div>{{ scope.row.appUserName }}</div>
                  <div v-if="scope.row.ownerId">({{ scope.row.ownerId }})</div>
                </template>
              </el-table-column>
              <el-table-column prop="link" :label="$t('auditAuthOwner.phone')" align="center" />
              <el-table-column prop="roomName" :label="$t('auditAuthOwner.room')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.roomName || '-' }}
                </template>
              </el-table-column>
              <el-table-column prop="ownerTypeCdName" :label="$t('auditAuthOwner.ownerType')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.ownerTypeCdName || '-' }}
                </template>
              </el-table-column>
              <el-table-column prop="idCard" :label="$t('auditAuthOwner.idCard')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.idCard || '-' }}
                </template>
              </el-table-column>
              <el-table-column prop="stateName" :label="$t('auditAuthOwner.state')" align="center" />
              <el-table-column prop="remark" :label="$t('auditAuthOwner.remark')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.remark || '-' }}
                </template>
              </el-table-column>
              <el-table-column prop="createTime" :label="$t('auditAuthOwner.createTime')" align="center" />
              <el-table-column prop="appTypeName" :label="$t('auditAuthOwner.appType')" align="center" />
              <el-table-column :label="$t('common.operation')" align="center" width="220">
                <template slot-scope="scope">
                  <el-button v-if="scope.row.state == '10000'" size="mini" type="primary"
                    @click="_openAuditAuthOwnerModel(scope.row)">
                    {{ $t('auditAuthOwner.audit') }}
                  </el-button>
                  <el-button size="mini" type="danger" @click="_deleteAppUserBindingOwnerModel(scope.row)">
                    {{ $t('common.delete') }}
                  </el-button>
                  <el-button size="mini" type="warning" @click="_resetUserPwdModel(scope.row)">
                    {{ $t('auditAuthOwner.resetPwd') }}
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

            <el-pagination class="mt-20" @size-change="handleSizeChange" @current-change="handleCurrentChange"
              :current-page="auditAuthOwnerInfo.conditions.page" :page-sizes="[10, 20, 30, 50]"
              :page-size="auditAuthOwnerInfo.conditions.row" layout="total, sizes, prev, pager, next, jumper"
              :total="auditAuthOwnerInfo.total" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <delete-binding-owner-dialog ref="deleteDialog" @delete-success="_deleteSuccess" />
    <reset-pwd-dialog ref="resetPwdDialog" />
  </div>
</template>

<script>
import { listAuditAppUserBindingOwners } from '@/api/owner/auditAuthOwnerApi'
import { getCommunityId } from '@/api/community/communityApi'
import DeleteBindingOwnerDialog from '@/components/owner/DeleteAppUserBindingOwner'
import ResetPwdDialog from '@/components/owner/ResetStaffPwd'

export default {
  name: 'AuditAuthOwnerList',
  components: {
    DeleteBindingOwnerDialog,
    ResetPwdDialog
  },
  data() {
    return {
      loading: false,
      auditAuthOwnerInfo: {
        appUsers: [],
        total: 0,
        conditions: {
          page: 1,
          row: 10,
          appUserName: '',
          idCard: '',
          link: '',
          state: '',
          communityId: ''
        },
        states: [
          { name: this.$t('auditAuthOwner.all'), statusCd: '' },
          { name: this.$t('auditAuthOwner.pending'), statusCd: '10000' },
          { name: this.$t('auditAuthOwner.success'), statusCd: '12000' },
          { name: this.$t('auditAuthOwner.failed'), statusCd: '13000' }
        ]
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.auditAuthOwnerInfo.conditions.communityId = this.communityId
    this._listAuditAuthOwners()
  },
  methods: {
    async _listAuditAuthOwners() {
      try {
        this.loading = true
        const params = { ...this.auditAuthOwnerInfo.conditions }

        // 清理参数
        params.appUserName = params.appUserName.trim()
        params.link = params.link.trim()
        params.idCard = params.idCard.trim()

        const res = await listAuditAppUserBindingOwners(params)
        this.auditAuthOwnerInfo.appUsers = res.data
        this.auditAuthOwnerInfo.total = res.total
      } catch (error) {
        console.error('获取数据失败:', error)
        this.$message.error(this.$t('auditAuthOwner.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _openAuditAuthOwnerModel(appUser) {
      this.$router.push({
        path: '/views/owner/auditAuthOwnerUndo',
        query: {
          appUserId: appUser.appUserId,
          roomId: appUser.roomId,
          appUserName: appUser.appUserName,
          link: appUser.link
        }
      })
    },
    _auditSubmit(auditData) {
      console.log(auditData)
    },
    _queryAuditAuthOwnerMethod() {
      this.auditAuthOwnerInfo.conditions.page = 1
      this._listAuditAuthOwners()
    },
    _resetAuditAuthOwnerMethod() {
      this.auditAuthOwnerInfo.conditions = {
        page: 1,
        row: 10,
        appUserName: '',
        idCard: '',
        link: '',
        state: '',
        communityId: this.communityId
      }
      this._listAuditAuthOwners()
    },
    _deleteAppUserBindingOwnerModel(row) {
      this.$refs.deleteDialog.open(row)
    },
    _resetUserPwdModel(staff) {
      this.$refs.resetPwdDialog.open(staff)
    },
    _swatchState(item) {
      this.auditAuthOwnerInfo.conditions.state = item.statusCd
      this._listAuditAuthOwners()
    },
    _deleteSuccess() {
      this._listAuditAuthOwners()
    },
    handleSizeChange(val) {
      this.auditAuthOwnerInfo.conditions.row = val
      this._listAuditAuthOwners()
    },
    handleCurrentChange(val) {
      this.auditAuthOwnerInfo.conditions.page = val
      this._listAuditAuthOwners()
    }
  }
}
</script>

<style scoped>
.animated {
  padding: 20px;
}

.border-radius {
  border-radius: 4px;
  overflow: hidden;
}

.list-group {
  padding: 0;
  margin: 0;
  list-style: none;
}

.list-group-item {
  padding: 12px 15px;
  border: 1px solid #ebeef5;
  border-bottom: none;
  cursor: pointer;
  background: #fff;
}

.list-group-item:last-child {
  border-bottom: 1px solid #ebeef5;
}

.list-group-item:hover {
  background: #ecf5ff;
}

.vc-node-selected {
  background: #409eff;
  color: white;
}

.mt-20 {
  margin-top: 20px;
}
</style>