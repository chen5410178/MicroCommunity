<template>
  <div>

    <el-card class="box-card">
      <div class="button-group">
        <el-button type="primary" size="small" @click="handleAdd">
          <i class="el-icon-plus"></i>{{ $t('dataPrivilege.add') }}
        </el-button>
        <el-button type="primary" size="small" @click="handleEdit" :disabled="!currentDataPrivilege.dpId">
          <i class="el-icon-edit"></i>{{ $t('dataPrivilege.edit') }}
        </el-button>
        <el-button type="primary" size="small" @click="handleDelete" :disabled="!currentDataPrivilege.dpId">
          <i class="el-icon-delete"></i>{{ $t('dataPrivilege.delete') }}
        </el-button>
      </div>

      <div class="data-privilege-list">
        <ul>
          <li v-for="(item, index) in dataPrivileges" :key="index"
            :class="{ active: item.dpId === currentDataPrivilege.dpId }" @click="handleSelect(item)">
            {{ item.name }}
          </li>
        </ul>
      </div>
    </el-card>
    <!-- 添加数据权限对话框 -->
    <add-data-privilege ref="addDataPrivilege" @success="handleRefresh" />
    <!-- 编辑数据权限对话框 -->
    <edit-data-privilege ref="editDataPrivilege" @success="handleRefresh" />
    <!-- 删除数据权限对话框 -->
    <delete-data-privilege ref="deleteDataPrivilege" @success="handleRefresh" />
  </div>
</template>

<script>
import { listDataPrivilege } from '@/api/org/dataPrivilegeManageApi'
import { getCommunityId } from '@/api/community/communityApi'
import AddDataPrivilege from '@/components/org/addDataPrivilege'
import EditDataPrivilege from '@/components/org/editDataPrivilege'
import DeleteDataPrivilege from '@/components/org/deleteDataPrivilege'
export default {
  name: 'DataPrivilegeDiv',
  components: {
    AddDataPrivilege,
    EditDataPrivilege,
    DeleteDataPrivilege
  },
  data() {
    return {
      dataPrivileges: [],
      currentDataPrivilege: {},
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.loadData()
  },
  methods: {
    handleRefresh() {
      this.loadData()
    },
    async loadData() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const { data } = await listDataPrivilege(params)
        this.dataPrivileges = data
        if (data.length > 0) {
          this.currentDataPrivilege = data[0]
          this.$emit('switch-data-privilege', data[0])
        }
      } catch (error) {
        console.error('Failed to load data privileges:', error)
      }
    },
    handleSelect(item) {
      this.currentDataPrivilege = item
      this.$emit('switch-data-privilege', item)
    },
    handleAdd() {
      this.$refs.addDataPrivilege.open()
    },
    handleEdit() {
      this.$refs.editDataPrivilege.open(this.currentDataPrivilege)
    },
    handleDelete() {
      this.$refs.deleteDataPrivilege.open(this.currentDataPrivilege)
    }
  }
}
</script>

<style lang="scss" scoped>
.box-card {
  height: 100%;

  .button-group {
    margin-bottom: 15px;
  }

  .data-privilege-list {
    ul {
      list-style: none;
      padding: 0;
      margin: 0;

      li {
        padding: 10px;
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          background-color: #f5f5f5;
        }

        &.active {
          background-color: #e6f7ff;
          color: #1890ff;
        }
      }
    }
  }
}
</style>