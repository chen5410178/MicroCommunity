<template>
  <div class="menuGroup-container">
    <div class="menu-group-wrapper" style="height: 220px;">
      <div class="group-header">
        <div class="header-title">选择商户</div>
      </div>
      <div class="group-content">
        <el-menu :default-active="searchForm.storeType" class="group-menu" @select="handleStoreType">
          <el-menu-item v-for="(item, index) in storeTypes" :key="index" :index="item.value">
            <span>{{ item.label }}</span>
          </el-menu-item>
        </el-menu>
      </div>
    </div>
    <div class="menu-list-wrapper">
      <!-- 查询条件 -->
      <div class="search-wrapper">
        <div class="search-title">{{ $t('menuGroup.search.title') }}</div>
        <div class="search-content">
          <el-input v-model="searchForm.name" :placeholder="$t('menuGroup.search.name')" class="search-item" clearable
            @keyup.enter.native="handleSearch" />
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
        </div>
      </div>

      <div class="list-wrapper">
        <div class="list-header">
          <div class="list-title">{{ $t('menuGroup.list.title') }}</div>
          <el-button type="primary" @click="handleAdd">{{ $t('common.add') }}</el-button>
        </div>

        <el-table v-loading="loading" :data="tableData" border style="width: 100%">
          <el-table-column prop="gId" :label="$t('menuGroup.table.gId')" />
          <el-table-column prop="name" :label="$t('menuGroup.table.name')" />
          <el-table-column prop="icon" :label="$t('menuGroup.table.icon')" />
          <el-table-column prop="label" :label="$t('menuGroup.table.label')" />
          <el-table-column prop="seq" :label="$t('menuGroup.table.seq')" />
          <el-table-column prop="description" :label="$t('menuGroup.table.description')" />
          <el-table-column prop="groupType" :label="$t('menuGroup.table.groupType')" />
          <el-table-column prop="storeType" :label="$t('menuGroup.table.storeType')" />
          <!-- <el-table-column
          prop="createTime"
          :label="$t('menuGroup.table.createTime')"
        /> -->
          <el-table-column :label="$t('common.operation')" width="200" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="handleEdit(scope.row)">{{ $t('common.edit') }}</el-button>
              <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ $t('common.delete') }}</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
          :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </div>
    <AddMenuGroup :visible.sync="addVisible" @success="handleSuccess" />

    <EditMenuGroup :visible.sync="editVisible" :form-data="currentRow" @success="handleSuccess" />

    <DelMenuGroup :visible.sync="delVisible" :g-id="currentRow.gId" @success="handleSuccess" />
  </div>
</template>

<script>
import { getMenuGroupList } from '@/api/dev/menuGroupApi'
import AddMenuGroup from '@/components/dev/AddMenuGroup'
import EditMenuGroup from '@/components/dev/EditMenuGroup'
import DelMenuGroup from '@/components/dev/DelMenuGroup'

export default {
  name: 'menuGroupList',
  components: {
    AddMenuGroup,
    EditMenuGroup,
    DelMenuGroup
  },
  data() {
    return {
      loading: false,
      storeTypes: [{
        label: '物业',
        value: '800900000003'
      }, {
        label: '运营团队',
        value: '800900000001'
      }, {
        label: '开发团队',
        value: '800900000000'
      }],
      searchForm: {
        name: '',
        icon: '',
        label: '',
        seq: '',
        groupType: '',
        storeType: '',
      },
      tableData: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      addVisible: false,
      editVisible: false,
      delVisible: false,
      currentRow: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          name: this.searchForm.name,
          storeType:this.searchForm.storeType
        }
        const { menuGroups, total } = await getMenuGroupList(params)
        this.tableData = menuGroups
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('menuGroup.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleAdd() {
      this.addVisible = true
    },
    handleEdit(row) {
      this.currentRow = { ...row }
      this.editVisible = true
    },
    handleDelete(row) {
      this.currentRow = { ...row }
      this.delVisible = true
    },
    handleSuccess() {
      this.getList()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getList()
    },
    handleStoreType(storeType){
      this.searchForm.storeType = storeType
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.menuGroup-container {
  padding: 0;
  margin: 0;
  display: flex;
  background-color: #f0f2f5;

  .menu-group-wrapper {
    width: 200px;
    height: calc(100vh - 300px);
    background-color: #fff;
    border-right: 1px solid #e6e6e6;
    display: flex;
    flex-direction: column;

    .group-header {
      padding: 20px;
      border-bottom: 1px solid #e6e6e6;

      .header-title {
        font-size: 16px;
        font-weight: bold;
        color: #333;
      }
    }

    .group-content {
      flex: 1;
      overflow-y: auto;

      .group-menu {
        border-right: none;
      }
    }
  }

  .menu-list-wrapper {
    flex: 1;
    padding-left: 10px;
    overflow-y: auto;

    .search-wrapper {
      background-color: #fff;
      padding: 20px;
      margin-bottom: 20px;
      border-radius: 4px;

      .search-title {
        font-size: 16px;
        font-weight: bold;
        margin-bottom: 20px;
        text-align: left;
        color: #333;
      }

      .search-content {
        display: flex;
        align-items: center;
        gap: 20px;

        .search-item {
          width: 200px;
        }
      }
    }

    .list-wrapper {
      background-color: #fff;
      padding: 20px;
      border-radius: 4px;

      .list-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;

        .list-title {
          font-size: 16px;
          font-weight: bold;
          color: #333;
        }
      }

      .el-pagination {
        margin-top: 20px;
        text-align: right;
      }
    }
  }
}
</style>