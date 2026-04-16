<template>
  <div class="storehouse-manage-container">
    <el-row>
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <div>{{ $t('storehouseManage.searchTitle') }}</div>
          </div>
          <el-form :model="conditions" label-width="auto" class="search-form">
            <el-row :gutter="20">
              <el-col :span="6">
                <el-form-item>
                  <el-input v-model="conditions.shName" :placeholder="$t('storehouseManage.placeholders.shName')" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item>
                  <el-select v-model="conditions.isShow" :placeholder="$t('storehouseManage.placeholders.isShow')"
                    style="width:100%">
                    <el-option :label="$t('storehouseManage.options.all')" value=""></el-option>
                    <el-option :label="$t('storehouseManage.options.yes')" value="true"></el-option>
                    <el-option :label="$t('storehouseManage.options.no')" value="false"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item>
                  <el-input v-model="conditions.shId" :placeholder="$t('storehouseManage.placeholders.shId')" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item>
                  <el-button type="primary" @click="queryStorehouseMethod">
                    <i class="el-icon-search"></i>
                    {{ $t('storehouseManage.buttons.search') }}
                  </el-button>
                  <el-button @click="resetStorehouseMethod">
                    <i class="el-icon-refresh"></i>
                    {{ $t('storehouseManage.buttons.reset') }}
                  </el-button>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row class="margin-top">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <div>{{ $t('storehouseManage.listTitle') }}</div>
            <div style="float: right;">
              <el-button type="primary" size="small" @click="openAddStorehouseModal"
                v-if="hasPrivilege('502021101638940007')">
                <i class="el-icon-plus"></i>
                {{ $t('storehouseManage.buttons.add') }}
              </el-button>
            </div>
          </div>
          <el-table :data="storehouses" border style="width:100%">
            <el-table-column prop="shId" :label="$t('storehouseManage.table.shId')" align="center" />
            <el-table-column prop="shName" :label="$t('storehouseManage.table.shName')" align="center" />
            <el-table-column :label="$t('storehouseManage.table.isShow')" align="center">
              <template slot-scope="scope">
                {{ scope.row.isShow === 'true' ? $t('storehouseManage.options.yes') : $t('storehouseManage.options.no')
                }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('storehouseManage.table.allowPurchase')" align="center">
              <template slot-scope="scope">
                {{ scope.row.allowPurchase === 'ON' ? $t('storehouseManage.options.yes') :
                  $t('storehouseManage.options.no') }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('storehouseManage.table.allowUse')" align="center">
              <template slot-scope="scope">
                {{ scope.row.allowUse === 'ON' ? $t('storehouseManage.options.yes') : $t('storehouseManage.options.no')
                }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('storehouseManage.table.purchaseSwitch')" align="center">
              <template slot-scope="scope">
                {{ scope.row.purchaseSwitch === 'ON' ? scope.row.purchaseFlowName :
                  $t('storehouseManage.options.noAudit')
                }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('storehouseManage.table.useSwitch')" align="center">
              <template slot-scope="scope">
                {{ scope.row.useSwitch === 'ON' ? scope.row.useFlowName : $t('storehouseManage.options.noAudit') }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('storehouseManage.table.allocationSwitch')" align="center">
              <template slot-scope="scope">
                {{ scope.row.allocationSwitch === 'ON' ? scope.row.allocationFlowName :
                  $t('storehouseManage.options.noAudit') }}
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('storehouseManage.table.createTime')" align="center" />
            <el-table-column :label="$t('storehouseManage.table.shDesc')" align="center">
              <template slot-scope="scope">
                {{ scope.row.shDesc && scope.row.shDesc !== '-1' ? scope.row.shDesc : '--' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('storehouseManage.table.operations')" align="center" fixed="right" width="250">
              <template slot-scope="scope">
                <el-button v-if="hasPrivilege('502021101197710001')" size="mini" type="primary"
                  @click="openEditStorehouseModel(scope.row)">
                  {{ $t('storehouseManage.buttons.edit') }}
                </el-button>
                <el-button v-if="hasPrivilege('502021122129210001')" size="mini" type="danger"
                  @click="openDeleteStorehouseModel(scope.row)">
                  {{ $t('storehouseManage.buttons.delete') }}
                </el-button>
                <el-button size="mini" @click="openDetailStorehouseModel(scope.row)">
                  {{ $t('storehouseManage.buttons.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="currentPage" :page-sizes="[10, 20, 30, 50]" :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="total" class="pagination">
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>

    <!-- 组件 -->
    <add-storehouse ref="addStorehouse" @success="listStorehouses" />
    <edit-storehouse ref="editStorehouse" @success="listStorehouses" />
    <delete-storehouse ref="deleteStorehouse" @success="listStorehouses" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listStorehouses } from '@/api/resource/storehouseManageApi'
import AddStorehouse from '@/components/resource/AddStorehouse'
import EditStorehouse from '@/components/resource/EditStorehouse'
import DeleteStorehouse from '@/components/resource/DeleteStorehouse'

export default {
  name: 'StorehouseManageList',
  components: {
    AddStorehouse,
    EditStorehouse,
    DeleteStorehouse
  },
  data() {
    return {
      currentPage: 1,
      pageSize: 10,
      total: 0,
      conditions: {
        shName: '',
        isShow: '',
        shId: '',
        communityId: ''
      },
      storehouses: []
    }
  },
  created() {
    this.conditions.communityId = getCommunityId()
    this.listStorehouses()
  },
  methods: {
    async listStorehouses() {
      try {
        const params = {
          page: this.currentPage,
          row: this.pageSize,
          ...this.conditions
        }
        const response = await listStorehouses(params)
        this.storehouses = response.data
        this.total = response.total
      } catch (error) {
        this.$message.error(this.$t('storehouseManage.messages.fetchError'))
      }
    },
    queryStorehouseMethod() {
      this.currentPage = 1
      this.listStorehouses()
    },
    resetStorehouseMethod() {
      this.conditions = {
        shName: '',
        isShow: '',
        shId: '',
        communityId: getCommunityId()
      }
      this.queryStorehouseMethod()
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.listStorehouses()
    },
    handleCurrentChange(page) {
      this.currentPage = page
      this.listStorehouses()
    },
    openAddStorehouseModal() {
      this.$refs.addStorehouse.open()
    },
    openEditStorehouseModel(row) {
      this.$refs.editStorehouse.open(row)
    },
    openDeleteStorehouseModel(row) {
      this.$refs.deleteStorehouse.open(row)
    },
    openDetailStorehouseModel(row) {
      this.$router.push(`/pages/common/resourceStoreManage?shId=${row.shId}&flag=1`)
    },
  }
}
</script>

<style scoped>
.storehouse-manage-container {
  padding: 20px;
}

.search-form {
  margin-bottom: -20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>