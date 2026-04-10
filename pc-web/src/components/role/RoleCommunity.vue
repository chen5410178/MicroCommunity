<template>
  <div class="role-community">
    <div class="header">
      <el-button type="primary" size="small" @click="openAddModal">
        {{ $t('role.community') }}
      </el-button>
    </div>

    <el-table :data="communities" border style="width: 100%">
      <el-table-column prop="communityId" :label="$t('role.communityId')" align="center" />
      <el-table-column prop="communityName" :label="$t('role.communityName')" align="center" />
      <el-table-column :label="$t('role.operation')" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="openDeleteModal(scope.row)">
            {{ $t('role.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size" :total="page.total"
      layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />

    <add-role-community ref="addRoleCommunity" :role-id="pgId" @success="loadCommunities" />
    <delete-role-community ref="deleteRoleCommunity" @success="loadCommunities" />
  </div>
</template>
  
<script>
import { listRoleCommunity } from '@/api/role/roleApi'
import AddRoleCommunity from './AddRoleCommunity'
import DeleteRoleCommunity from './DeleteRoleCommunity'

export default {
  name: 'RoleCommunity',
  components: {
    AddRoleCommunity,
    DeleteRoleCommunity
  },
  props: {
    pgId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      communities: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.loadCommunities()
  },
  methods: {
    async loadCommunities() {
      try {
        const params = {
          page: this.page.current,
          row: this.page.size,
          roleId: this.pgId
        }
        const res = await listRoleCommunity(params)
        this.communities = res.data
        this.page.total = res.total
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    openAddModal() {
      this.$refs.addRoleCommunity.show()
    },
    openDeleteModal(community) {
      this.$refs.deleteRoleCommunity.show(community)
    },
    handleSizeChange(size) {
      this.page.size = size
      this.loadCommunities()
    },
    handleCurrentChange(current) {
      this.page.current = current
      this.loadCommunities()
    }
  }
}
</script>
  
<style scoped>
.role-community {
  margin:15px;
  .header {
    margin-bottom: 15px;
    text-align: right;
  }

  .el-pagination {
    margin-top: 15px;
    text-align: right;
  }
}
</style>