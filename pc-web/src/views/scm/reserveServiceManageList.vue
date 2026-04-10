<template>
  <div class="reserve-service-manage-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card class="tree-card">
          <div class="treeview attendance-staff">
            <ul class="list-group text-center border-radius"
              v-if="reserveServiceManageInfo.catalogs && reserveServiceManageInfo.catalogs.length > 0">
              <template v-for="(item, index) in reserveServiceManageInfo.catalogs">
                <li class="list-group-item node-orgTree" :key="index" @click="swatchReserveCatalog(item)"
                  :class="{ 'vc-node-selected': reserveServiceManageInfo.conditions.catalogId == item.catalogId }">
                  {{ item.name }}
                </li>
              </template>
            </ul>
            <ul class="list-group text-center border-radius" v-else>
              <li class="list-group-item node-orgTree" @click="_addCatalog()">
                {{ $t('reserveServiceManage.addCatalog') }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="20">
        <el-card>
          <div slot="header" class="text-left">
            <span>{{ $t('reserveServiceManage.searchCondition') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input :placeholder="$t('reserveServiceManage.goodsIdPlaceholder')"
                v-model="reserveServiceManageInfo.conditions.goodsId">
              </el-input>
            </el-col>
            <el-col :span="6">
              <el-input :placeholder="$t('reserveServiceManage.goodsNamePlaceholder')"
                v-model="reserveServiceManageInfo.conditions.goodsName">
              </el-input>
            </el-col>
            <el-col :span="6">
              <el-select v-model="reserveServiceManageInfo.conditions.state"
                :placeholder="$t('reserveServiceManage.statePlaceholder')" style="width:100%">
                <el-option :label="$t('reserveServiceManage.stateOffShelf')" value="1001">
                </el-option>
                <el-option :label="$t('reserveServiceManage.stateOnShelf')" value="2002">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="_queryReserveServiceMethod()">
                {{ $t('common.search') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card style="margin-top:20px">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('reserveServiceManage.reserveService') }}</span>
            <el-button type="primary" size="mini" @click="_openAddReserveServiceModal()">
              <i class="el-icon-plus"></i>
              {{ $t('reserveServiceManage.addService') }}
            </el-button>
          </div>

          <el-table :data="reserveServiceManageInfo.reserveServices" border style="width:100%">
            <el-table-column :label="$t('reserveServiceManage.serviceCover')" align="center">
              <template slot-scope="scope">
                <el-image v-if="scope.row.imgUrl" style="width:60px;height:60px;border-radius:5px"
                  :src="scope.row.imgUrl" fit="cover">
                </el-image>
                <el-image v-else style="width:60px;height:60px;border-radius:5px" src="/img/noPhoto.jpg" fit="cover">
                </el-image>
              </template>
            </el-table-column>
            <el-table-column :label="$t('reserveServiceManage.nameAndId')" align="center">
              <template slot-scope="scope">
                {{ scope.row.goodsName }}({{ scope.row.goodsId }})
              </template>
            </el-table-column>
            <el-table-column prop="paramsName" :label="$t('reserveServiceManage.params')" align="center">
            </el-table-column>
            <el-table-column prop="paramWay" :label="$t('reserveServiceManage.reserveWay')" align="center">
              <template slot-scope="scope">
                {{ scope.row.paramWay == '1' ? $t('reserveServiceManage.day') : $t('reserveServiceManage.week') }}
              </template>
            </el-table-column>
            <el-table-column prop="maxQuantity" :label="$t('reserveServiceManage.reserveCount')" align="center">
            </el-table-column>
            <el-table-column prop="hoursMaxQuantity" :label="$t('reserveServiceManage.reserveQuantity')" align="center">
            </el-table-column>
            <el-table-column prop="price" :label="$t('reserveServiceManage.doorFee')" align="center">
            </el-table-column>
            <el-table-column :label="$t('reserveServiceManage.validity')" align="center">
              <template slot-scope="scope">
                {{ scope.row.startDate }}~{{ scope.row.endDate }}
              </template>
            </el-table-column>
            <el-table-column prop="startTime" :label="$t('reserveServiceManage.reserveStartTime')" align="center">
            </el-table-column>
            <el-table-column prop="sort" :label="$t('reserveServiceManage.sort')" align="center">
            </el-table-column>
            <el-table-column :label="$t('reserveServiceManage.state')" align="center">
              <template slot-scope="scope">
                {{ scope.row.state == '1001' ? $t('reserveServiceManage.offShelf') : $t('reserveServiceManage.onShelf')
                }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('common.operation')" align="center" width="200">
              <template slot-scope="scope">
                <el-button size="mini" @click="_openReserveServicePersonModel(scope.row)">
                  {{ $t('reserveServiceManage.reserveService') }}
                </el-button>
                <el-button size="mini" type="primary" @click="_openEditReserveServiceModel(scope.row)">
                  {{ $t('common.edit') }}
                </el-button>
                <el-button size="mini" type="danger" @click="_openDeleteReserveServiceModel(scope.row)">
                  {{ $t('common.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            layout="total, sizes, prev, pager, next, jumper" :total="page.total">
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>

    <add-reserve-service-person ref="addReserveServicePerson"></add-reserve-service-person>
    <delete-reserve-dining ref="deleteReserveDining" @success="handleDeleteSuccess"></delete-reserve-dining>
  </div>
</template>

<script>
import { listReserveGoods, listReserveCatalog } from '@/api/scm/reserveServiceManageApi'
import { getCommunityId } from '@/api/community/communityApi'
import AddReserveServicePerson from '@/components/scm/addReserveServicePerson'
import DeleteReserveDining from '@/components/scm/deleteReserveDining'

export default {
  name: 'ReserveServiceManageList',
  components: {
    AddReserveServicePerson,
    DeleteReserveDining
  },
  data() {
    return {
      reserveServiceManageInfo: {
        reserveServices: [],
        catalogs: [],
        conditions: {
          goodsId: '',
          goodsName: '',
          type: '2002',
          state: '',
          catalogId: '',
          communityId: ''
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.reserveServiceManageInfo.conditions.communityId = getCommunityId()
    this._listReserveCatalogs()
  },
  methods: {
    async _listReserveServices(page, rows) {
      try {
        this.reserveServiceManageInfo.conditions.page = page
        this.reserveServiceManageInfo.conditions.row = rows
        const { data, total } = await listReserveGoods(this.reserveServiceManageInfo.conditions)
        this.reserveServiceManageInfo.reserveServices = data
        this.page.total = total
      } catch (error) {
        console.error('获取预约服务列表失败:', error)
      }
    },
    async _listReserveCatalogs() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.reserveServiceManageInfo.conditions.communityId,
          type: '2002'
        }
        const { data } = await listReserveCatalog(params)
        this.reserveServiceManageInfo.catalogs = data
        if (data && data.length > 0) {
          this.swatchReserveCatalog(data[0])
        }
      } catch (error) {
        console.error('获取预约目录失败:', error)
      }
    },
    _openAddReserveServiceModal() {
      if (!this.reserveServiceManageInfo.conditions.catalogId) {
        this.$message.warning(this.$t('reserveServiceManage.addCatalogFirst'))
        return
      }
      this.$router.push(`/views/scm/addReserveService?catalogId=${this.reserveServiceManageInfo.conditions.catalogId}`)
    },
    _openEditReserveServiceModel(reserveService) {
      this.$router.push(`/views/scm/editReserveService?goodsId=${reserveService.goodsId}`)
    },
    _openDeleteReserveServiceModel(reserveService) {
      this.$refs.deleteReserveDining.open(reserveService)
    },
    _queryReserveServiceMethod() {
      this._listReserveServices(1, this.page.size)
    },
    _openReserveServicePersonModel(reserveService) {
      this.$refs.addReserveServicePerson.open(reserveService)
    },
    swatchReserveCatalog(item) {
      this.reserveServiceManageInfo.conditions.catalogId = item.catalogId
      this._listReserveServices(1, this.page.size)
    },
    _addCatalog() {
      this.$router.push('/pages/reserve/reserveCatalogManage?tab=reserveCatalog')
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listReserveServices(this.page.current, val)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listReserveServices(val, this.page.size)
    },
    handleDeleteSuccess() {
      this._listReserveServices(1, this.page.size)
    }
  }
}
</script>

<style scoped>
.reserve-service-manage-container {
  padding: 20px;
}

.tree-card {
  height: 100%;
}

.list-group-item {
  padding: 10px;
  cursor: pointer;
  border: 1px solid #ebeef5;
  margin-bottom: 5px;
}

.list-group-item:hover {
  background-color: #f5f7fa;
}

.vc-node-selected {
  background-color: #ecf5ff;
  color: #409eff;
  border-color: #d9ecff;
}

.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>