<template>
  <div class="owner-detail-app-user-container">
    <el-table :data="appUsers" border style="width: 100%">
      <el-table-column prop="communityName" :label="$t('ownerDetailAppUser.communityName')" align="center"></el-table-column>
      <el-table-column prop="appUserName" :label="$t('ownerDetailAppUser.appUserName')" align="center"></el-table-column>
      <el-table-column prop="idCard" :label="$t('ownerDetailAppUser.idCard')" align="center"></el-table-column>
      <el-table-column prop="link" :label="$t('ownerDetailAppUser.link')" align="center"></el-table-column>
      <el-table-column prop="stateName" :label="$t('ownerDetailAppUser.state')" align="center"></el-table-column>
      <el-table-column prop="createTime" :label="$t('ownerDetailAppUser.createTime')" align="center"></el-table-column>
      <el-table-column prop="appTypeName" :label="$t('ownerDetailAppUser.appType')" align="center">
        <template slot-scope="scope">
          {{ scope.row.appTypeName || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="openId" :label="$t('ownerDetailAppUser.openId')" align="center"></el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" width="150">
        <template slot-scope="scope">
          <el-button 
            v-if="scope.row.ownerId && scope.row.ownerId !== '-1'"
            size="mini" 
            @click="toOwnerDetail(scope.row)">
            {{ $t('ownerDetailAppUser.ownerDetail') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="page.current"
      :page-sizes="[10, 20, 30, 50]"
      :page-size="page.size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="page.total">
    </el-pagination>
  </div>
</template>

<script>
import { listAdminAppUserOwners, updateAppUserBindingOwner } from '@/api/staff/systemUserDetailApi'

export default {
  name: 'AOwnerDetailAppUser',
  props: {
    userId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      appUsers: [],
      currentAppUserId: '',
      ownerId: '',
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(params) {
      this.ownerId = params.ownerId
      this.loadData()
    },
    async loadData() {
      try {
        const params = {
          page: this.page.current,
          row: this.page.size,
          systemUserId: this.userId,
          ownerId: this.ownerId
        }
        const res = await listAdminAppUserOwners(params)
        if (res.code === 0) {
          this.appUsers = res.data
          this.page.total = res.total
        }
      } catch (error) {
        this.$message.error(this.$t('common.loadFailed'))
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadData()
    },
    toOwnerDetail(row) {
      window.open(`/#/views/aCommunity/adminOwnerDetail?ownerId=${row.ownerId}`)
    },
    openAuditModal(appUserId) {
      this.currentAppUserId = appUserId
      this.$emit('openAuditModal')
    },
    async handleAudit(auditInfo) {
      try {
        const data = {
          ...auditInfo,
          appUserId: this.currentAppUserId
        }
        const res = await updateAppUserBindingOwner(data)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.loadData()
        } else {
          this.$message.error(res.msg || this.$t('common.handleFailed'))
        }
      } catch (error) {
        this.$message.error(this.$t('common.handleFailed'))
      }
    }
  }
}
</script>

<style scoped>
.owner-detail-app-user-container {
  margin-top: 20px;
}
</style>