<template>
  <div class="menu-container">
    <div>
      <div class="menu-group-wrapper" style="height: 220px;">
        <div class="group-header">
          <div class="header-title">选择商户</div>
        </div>
        <div class="group-content">
          <el-menu :default-active="storeType" class="group-menu" @select="handleStoreType">
            <el-menu-item v-for="(item, index) in storeTypes" :key="index" :index="item.value">
              <span>{{ item.label }}</span>
            </el-menu-item>
          </el-menu>
        </div>
      </div>
      <div class="menu-group-wrapper margin-top">
        <div class="group-header">
          <div class="header-title">选择菜单组</div>
        </div>
        <div class="group-content">
          <el-menu :default-active="searchForm.gId" class="group-menu" @select="handleGroup">
            <el-menu-item v-for="(item, index) in menuGroups" :key="index" :index="item.gId">
              <span>{{ item.name }}</span>
            </el-menu-item>
          </el-menu>
        </div>
      </div>
    </div>

    <!-- 右侧菜单列表 -->
    <div class="menu-list-wrapper">
      <!-- 查询条件 -->
      <div class="search-wrapper">
        <div class="search-title">{{ $t('menu.search.title') }}</div>
        <div class="search-content">
          <el-input v-model="searchForm.name" :placeholder="$t('menu.search.name')" class="search-item" clearable
            @keyup.enter.native="handleSearch" />
          <el-select v-model="searchForm.isShow" :placeholder="$t('menu.search.isShow')" class="search-item">
            <el-option label="全部" value="" />
            <el-option label="显示" value="Y" />
            <el-option label="不显示" value="N" />
          </el-select>
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
        </div>
      </div>

      <div class="list-wrapper">
        <div class="list-header">
          <div class="list-title">{{ $t('menu.list.title') }}</div>
          <el-button type="primary" @click="handleAdd" v-if="searchForm.gId">{{ $t('common.add') }}</el-button>
        </div>

        <el-table v-loading="loading" :data="tableData" border style="width: 100%">
          <el-table-column prop="mId" :label="$t('menu.table.mId')" />
          <el-table-column prop="name" :label="$t('menu.table.name')" />
          <el-table-column prop="gId" :label="$t('menu.table.gId')" />
          <el-table-column prop="url" :label="$t('menu.table.url')" />
          <el-table-column prop="seq" :label="$t('menu.table.seq')" />
          <el-table-column prop="description" :label="$t('menu.table.description')" />
          <el-table-column prop="isShowName" :label="$t('menu.table.isShow')" />
          <!-- <el-table-column prop="createTime" :label="$t('menu.table.createTime')" /> -->
          <el-table-column :label="$t('common.operation')" width="200" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="handleEdit(scope.row)">{{ $t('common.edit') }}</el-button>
              <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ $t('common.delete') }}</el-button>
              <el-button size="mini" type="warn" @click="handlePrivilege(scope.row)">{{ $t('menu.privilege') }}</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
          :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </div>
    <AddMenu :visible.sync="addVisible" :g-id="searchForm.gId" :store-type="storeType" @success="handleSuccess" />

    <EditMenu :visible.sync="editVisible" :form-data="currentRow" @success="handleSuccess" />

    <DelMenu :visible.sync="delVisible" :m-id="currentRow.mId" :p-id="currentRow.pId" @success="handleSuccess" />
  </div>
</template>

<script>
import { getMenuList } from '@/api/dev/menuApi'
import { getMenuGroupList } from '@/api/dev/menuGroupApi'

import AddMenu from '@/components/dev/AddMenu'
import EditMenu from '@/components/dev/EditMenu'
import DelMenu from '@/components/dev/DelMenu'

export default {
  name: 'menuList',
  components: {
    AddMenu,
    EditMenu,
    DelMenu
  },
  data() {
    return {
      loading: false,
      menuGroups: [],
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
      storeType: '',
      searchForm: {
        name: '',
        gId: '',
        isShow: '',
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
    this.handleStoreType(this.storeTypes[0].value)
    this.getList()
  },
  methods: {
    async getMenuGroupList() {
      try {
        this.loading = true
        const params = {
          page: 1,
          row: 100,
          storeType: this.storeType,
        }
        const { menuGroups } = await getMenuGroupList(params)
        this.menuGroups = menuGroups
      } catch (error) {
        this.$message.error(this.$t('menuGroup.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          name: this.searchForm.name,
          gId: this.searchForm.gId,
          isShow: this.searchForm.isShow,
        }
        const { menus, total } = await getMenuList(params)
        this.tableData = menus
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('menu.fetchError'))
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
    handleGroup(_gId) {
      this.searchForm.gId = _gId;
      this.getList()
    },
    handleStoreType(storeType) {
      this.storeType = storeType
      this.getMenuGroupList()
    },
    handlePrivilege(row){
      this.$router.push('/views/dev/privilegeList?mId='+row.mId+'&domain='+this.storeType)
    }
  }
}
</script>

<style lang="scss" scoped>
.menu-container {
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