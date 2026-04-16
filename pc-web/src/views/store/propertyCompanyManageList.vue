<template>
  <div class="property-company-container">
    <!-- 查询条件 -->
    <el-card>
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('propertyCompanyManage.queryCondition') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.storeId" :placeholder="$t('propertyCompanyManage.enterStoreId')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.name" :placeholder="$t('propertyCompanyManage.enterName')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.tel" :placeholder="$t('propertyCompanyManage.enterTel')" clearable />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('propertyCompanyManage.search') }}
          </el-button>
          <el-button @click="handleReset">
            {{ $t('propertyCompanyManage.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 物业公司列表 -->
    <el-card>
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('propertyCompanyManage.propertyCompany') }}</span>
        <div>
          <el-button type="primary" size="small" @click="handleAdd">
            {{ $t('propertyCompanyManage.add') }}
          </el-button>
        </div>
      </div>
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="storeId" :label="$t('propertyCompanyManage.storeId')" align="center" />
        <el-table-column prop="name" :label="$t('propertyCompanyManage.name')" align="center" />
        <el-table-column prop="address" :label="$t('propertyCompanyManage.address')" align="center" />
        <el-table-column prop="tel" :label="$t('propertyCompanyManage.tel')" align="center" />
        <el-table-column prop="corporation" :label="$t('propertyCompanyManage.corporation')" align="center" />
        <el-table-column prop="foundingTime" :label="$t('propertyCompanyManage.foundingTime')" align="center" />
        <el-table-column prop="nearbyLandmarks" :label="$t('propertyCompanyManage.landmark')" align="center" />
        <el-table-column prop="createTime" :label="$t('propertyCompanyManage.createTime')" align="center" />
        <el-table-column :label="$t('propertyCompanyManage.operation')" align="center" width="300">
          <template #default="scope">
            <el-button-group>
              <el-button size="mini" @click="handleEdit(scope.row)">
                {{ $t('propertyCompanyManage.edit') }}
              </el-button>
              <el-button size="mini" @click="handleDelete(scope.row)">
                {{ $t('propertyCompanyManage.delete') }}
              </el-button>
              <el-button size="mini" @click="handleManageCommunity(scope.row)">
                {{ $t('propertyCompanyManage.manageCommunity') }}
              </el-button>
            </el-button-group>
            <el-button-group style="margin-top: 5px">
              <el-button size="mini" @click="handleAdminLogin(scope.row)">
                {{ $t('propertyCompanyManage.login') }}
              </el-button>
              <el-button size="mini" v-if="scope.row.state !== '48002'" @click="handleUpdateState(scope.row, '48002')">
                {{ $t('propertyCompanyManage.restrictLogin') }}
              </el-button>
              <el-button size="mini" v-else @click="handleUpdateState(scope.row, '48001')">
                {{ $t('propertyCompanyManage.restoreLogin') }}
              </el-button>
              <el-button size="mini" @click="handleResetPwd(scope.row)">
                {{ $t('propertyCompanyManage.resetPwd') }}
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <el-row>
        <el-col :span="12">
          <div class="tip">{{ $t('propertyCompanyManage.tip') }}</div>
        </el-col>
        <el-col :span="12">
          <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handlePageChange" />
        </el-col>
      </el-row>
    </el-card>

    <!-- 组件 -->
    <add-property-company ref="addPropertyCompany" @success="handleSuccess" />
    <edit-property-company ref="editPropertyCompany" @success="handleSuccess" />
    <delete-property-company ref="deletePropertyCompany" @success="handleSuccess" />
    <admin-login-property ref="adminLoginProperty" @success="handleLoginSuccess" />
    <update-store-state ref="updateStoreState" @success="handleSuccess" />
    <reset-staff-pwd ref="resetStaffPwd" @success="handleSuccess" />
  </div>
</template>

<script>
import { getPropertyList, getPropertyStaffs } from '@/api/store/propertyCompanyManageApi'

export default {
  name: 'PropertyCompanyManageList',
  components: {
    AddPropertyCompany: () => import('@/components/store/AddPropertyCompany'),
    EditPropertyCompany: () => import('@/components/store/EditPropertyCompany'),
    DeletePropertyCompany: () => import('@/components/store/DeletePropertyCompany'),
    AdminLoginProperty: () => import('@/components/store/AdminLoginProperty'),
    UpdateStoreState: () => import('@/components/store/UpdateStoreState'),
    ResetStaffPwd: () => import('@/components/store/ResetStaffPwd')
  },
  data() {
    return {
      searchForm: {
        storeId: '',
        name: '',
        tel: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.searchForm
        }
        const { data, total } = await getPropertyList(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('propertyCompanyManage.fetchError'))
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        storeId: '',
        name: '',
        tel: ''
      }
      this.handleSearch()
    },
    handleAdd() {
      this.$refs.addPropertyCompany.open()
    },
    handleEdit(row) {
      this.$refs.editPropertyCompany.open(row)
    },
    handleDelete(row) {
      this.$refs.deletePropertyCompany.open(row)
    },
    handleManageCommunity(row) {
      this.$router.push(`/views/community/propertyCommunity?storeId=${row.storeId}`)
    },
    async handleAdminLogin(row) {
      try {
        const { data } = await getPropertyStaffs({
          page: 1,
          row: 1,
          relCd: '600311000001',
          storeId: row.storeId
        })
        this.$refs.adminLoginProperty.open({
          ...row,
          staffInfo: data[0]
        })
      } catch (error) {
        this.$message.error(this.$t('propertyCompanyManage.fetchStaffError'))
      }
    },
    handleUpdateState(row, state) {
      this.$refs.updateStoreState.open({
        storeInfo: row,
        state
      })
    },
    async handleResetPwd(row) {
      try {
        const { data } = await getPropertyStaffs({
          page: 1,
          row: 1,
          relCd: '600311000001',
          storeId: row.storeId
        })
        this.$refs.resetStaffPwd.open({
          ...row,
          staffInfo: data[0]
        })
      } catch (error) {
        this.$message.error(this.$t('propertyCompanyManage.fetchStaffError'))
      }
    },
    handleSuccess() {
      this.getList()
    },
    handleLoginSuccess() {
      this.$router.push('/')
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.getList()
    },
    handlePageChange(page) {
      this.pagination.current = page
      this.getList()
    }
  }
}
</script>

<style scoped>
.property-company-container {
  padding: 20px;
}

.el-card {
  margin-bottom: 20px;
}

.tip {
  padding: 10px;
  color: #999;
  font-size: 12px;
}

.el-button-group {
  margin-bottom: 5px;
}
</style>