<template>
  <el-dialog :title="$t('role.community')" :visible.sync="visible" width="70%" @close="resetForm">
    <div class="text-right">
      <el-input v-model="searchForm.communityName" :placeholder="$t('role.communityName')"
        style="width: 300px; margin-right: 10px" />
      <el-button type="primary" @click="queryCommunities">
        {{ $t('common.search') }}
      </el-button>
    </div>

    <el-table :data="communities" style="width: 100%; margin-top: 15px" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="communityId" :label="$t('role.communityId')" align="center" />
      <el-table-column prop="communityName" :label="$t('role.communityName')" align="center" />
    </el-table>

    <el-pagination :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size" :total="page.total"
      layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('role.cancel') }}</el-button>
      <el-button type="primary" @click="submit">{{ $t('role.save') }}</el-button>
    </span>
  </el-dialog>
</template>
  
<script>
import { listOrgNoCommunities, saveRoleCommunity } from '@/api/role/roleApi'

export default {
  name: 'AddRoleCommunity',
  props: {
    roleId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      visible: false,
      searchForm: {
        communityName: ''
      },
      communities: [],
      selectedCommunities: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    show() {
      this.visible = true
      this.queryCommunities()
    },
    async queryCommunities() {
      try {
        const params = {
          page: this.page.current,
          row: this.page.size,
          name: this.searchForm.communityName,
          roleId: this.roleId
        }
        const res = await listOrgNoCommunities(params)
        this.communities = res.data
        this.page.total = res.total
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    handleSelectionChange(val) {
      this.selectedCommunities = val
    },
    async submit() {
      if (this.selectedCommunities.length === 0) {
        this.$message.warning(this.$t('role.selectCommunity'))
        return
      }

      try {
        const communities = this.selectedCommunities.map(item => ({
          communityId: item.communityId,
          communityName: item.name
        }))

        await saveRoleCommunity({
          roleId: this.roleId,
          communities
        })

        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    handleSizeChange(size) {
      this.page.size = size
      this.queryCommunities()
    },
    handleCurrentChange(current) {
      this.page.current = current
      this.queryCommunities()
    },
    resetForm() {
      this.searchForm.communityName = ''
      this.selectedCommunities = []
    }
  }
}
</script>