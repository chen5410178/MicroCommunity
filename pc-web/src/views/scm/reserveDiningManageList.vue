<template>
  <div class="reserve-dining-manage-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card class="border-radius">
          <div class="treeview attendance-staff">
            <ul class="list-group text-center border-radius"
              v-if="reserveDiningManageInfo.catalogs && reserveDiningManageInfo.catalogs.length > 0">
              <li class="list-group-item node-orgTree" v-for="(item, index) in reserveDiningManageInfo.catalogs"
                :key="index" @click="swatchReserveCatalog(item)"
                :class="{ 'vc-node-selected': reserveDiningManageInfo.conditions.catalogId == item.catalogId }">
                {{ item.name }}
              </li>
            </ul>
            <ul class="list-group text-center border-radius" v-else>
              <li class="list-group-item node-orgTree" @click="_addCatalog()">
                {{ $t('reserveDiningManage.addCatalog') }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="20">
        <el-card>
          <div slot="header" class="text-left">
            <span>{{ $t('reserveDiningManage.searchTitle') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input v-model="reserveDiningManageInfo.conditions.goodsId"
                :placeholder="$t('reserveDiningManage.goodsIdPlaceholder')" clearable />
            </el-col>
            <el-col :span="6">
              <el-input v-model="reserveDiningManageInfo.conditions.goodsName"
                :placeholder="$t('reserveDiningManage.goodsNamePlaceholder')" clearable />
            </el-col>
            <el-col :span="6">
              <el-select v-model="reserveDiningManageInfo.conditions.state"
                :placeholder="$t('reserveDiningManage.statePlaceholder')" style="width:100%" clearable>
                <el-option :label="$t('reserveDiningManage.stateNotOnline')" value="1001" />
                <el-option :label="$t('reserveDiningManage.stateOnline')" value="2002" />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="_queryReserveDiningMethod()" icon="el-icon-search">
                {{ $t('common.search') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="margin-top">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('reserveDiningManage.listTitle') }}</span>
            <div style="float: right;">
              <el-button type="primary" size="small" @click="_doOwnerDining()">
                {{ $t('reserveDiningManage.diningStatistics') }}
              </el-button>
              <el-button type="primary" size="small" @click="_doDining()">
                {{ $t('reserveDiningManage.dining') }}
              </el-button>
              <el-button type="primary" size="small" @click="_openAddReserveDiningModal()">
                {{ $t('common.add') }}
              </el-button>
            </div>
          </div>

          <el-table :data="reserveDiningManageInfo.reserveDinings" border style="width: 100%" v-loading="loading">
            <el-table-column :label="$t('reserveDiningManage.goodsCover')" align="center">
              <template slot-scope="scope">
                <el-image style="width: 60px; height: 60px; border-radius: 5px;"
                  :src="scope.row.imgUrl || '/img/noPhoto.jpg'" fit="cover" />
              </template>
            </el-table-column>
            <el-table-column :label="$t('reserveDiningManage.goodsName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.goodsName }}({{ scope.row.goodsId }})
              </template>
            </el-table-column>
            <el-table-column prop="paramsName" :label="$t('reserveDiningManage.params')" align="center" />
            <el-table-column :label="$t('reserveDiningManage.reserveWay')" align="center">
              <template slot-scope="scope">
                {{ scope.row.paramWay == '1' ? $t('reserveDiningManage.day') : $t('reserveDiningManage.week') }}
              </template>
            </el-table-column>
            <el-table-column prop="maxQuantity" :label="$t('reserveDiningManage.reserveCount')" align="center" />
            <el-table-column prop="hoursMaxQuantity" :label="$t('reserveDiningManage.reserveQuantity')"
              align="center" />
            <el-table-column prop="price" :label="$t('reserveDiningManage.price')" align="center" />
            <el-table-column :label="$t('reserveDiningManage.validDate')" align="center">
              <template slot-scope="scope">
                {{ scope.row.startDate }}~{{ scope.row.endDate }}
              </template>
            </el-table-column>
            <el-table-column prop="startTime" :label="$t('reserveDiningManage.reserveStartTime')" align="center" />
            <el-table-column prop="sort" :label="$t('reserveDiningManage.sort')" align="center" />
            <el-table-column :label="$t('reserveDiningManage.state')" align="center">
              <template slot-scope="scope">
                {{ scope.row.state == '1001' ? $t('reserveDiningManage.stateNotOnline') :
                  $t('reserveDiningManage.stateOnline')}}
              </template>
            </el-table-column>
            <el-table-column :label="$t('common.operation')" align="center" width="250">
              <template slot-scope="scope">
                <el-button size="mini" @click="_openReserveDiningPersonModel(scope.row)">
                  {{ $t('reserveDiningManage.reserveDining') }}
                </el-button>
                <el-button size="mini" type="primary" @click="_openEditReserveDiningModel(scope.row)">
                  {{ $t('common.edit') }}
                </el-button>
                <el-button size="mini" type="danger" @click="_openDeleteReserveDiningModel(scope.row)">
                  {{ $t('common.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination class="pagination" @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            layout="total, sizes, prev, pager, next, jumper" :total="page.total" />
        </el-card>
      </el-col>
    </el-row>

    <add-reserve-dining-person ref="addReserveDiningPerson" @success="handleSuccess" />
    <delete-reserve-dining ref="deleteReserveDining" @success="handleSuccess" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listReserveGoods, listReserveCatalog } from '@/api/scm/reserveDiningManageApi'
import AddReserveDiningPerson from '@/components/scm/addReserveDiningPerson'
import DeleteReserveDining from '@/components/scm/deleteReserveDining'

export default {
  name: 'ReserveDiningManageList',
  components: {
    AddReserveDiningPerson,
    DeleteReserveDining
  },
  data() {
    return {
      loading: false,
      reserveDiningManageInfo: {
        reserveDinings: [],
        catalogs: [],
        conditions: {
          goodsId: '',
          goodsName: '',
          type: '1001',
          state: '',
          catalogId: '',
          communityId: getCommunityId(),
          page: 1,
          row: 10
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
    this._listReserveCatalogs()
  },
  methods: {
    async _listReserveDinings(page, rows) {
      try {
        this.loading = true
        this.reserveDiningManageInfo.conditions.page = page || this.page.current
        this.reserveDiningManageInfo.conditions.row = rows || this.page.size

        const { data, total } = await listReserveGoods(this.reserveDiningManageInfo.conditions)
        this.reserveDiningManageInfo.reserveDinings = data
        this.page.total = total
      } catch (error) {
        console.error(error)
        this.$message.error(this.$t('reserveDiningManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async _listReserveCatalogs() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: getCommunityId(),
          type: '1001'
        }

        const { data } = await listReserveCatalog(params)
        this.reserveDiningManageInfo.catalogs = data
        if (data && data.length > 0) {
          this.swatchReserveCatalog(data[0])
        }
      } catch (error) {
        console.error(error)
      }
    },
    swatchReserveCatalog(item) {
      this.reserveDiningManageInfo.conditions.catalogId = item.catalogId
      this._listReserveDinings()
    },
    _queryReserveDiningMethod() {
      this.page.current = 1
      this._listReserveDinings()
    },
    _openAddReserveDiningModal() {
      if (!this.reserveDiningManageInfo.conditions.catalogId) {
        this.$message.warning(this.$t('reserveDiningManage.addCatalogFirst'))
        return
      }
      this.$router.push(`/views/scm/addReserveDining?catalogId=${this.reserveDiningManageInfo.conditions.catalogId}`)
    },
    _openEditReserveDiningModel(reserveDining) {
      this.$router.push(`/views/scm/editReserveDining?goodsId=${reserveDining.goodsId}`)
    },
    _openDeleteReserveDiningModel(reserveDining) {
      this.$refs.deleteReserveDining.open(reserveDining)
    },
    _openReserveDiningPersonModel(reserveDining) {
      this.$refs.addReserveDiningPerson.open(reserveDining)
    },
    _addCatalog() {
      this.$router.push('/views/scm/reserveCatalogManage?tab=reserveCatalog')
    },
    _doDining() {
      if (!this.reserveDiningManageInfo.reserveDinings || this.reserveDiningManageInfo.reserveDinings.length < 1) {
        this.$message.warning(this.$t('reserveDiningManage.addFirst'))
        return
      }
      this.$router.push('/views/scm/doDining')
    },
    _doOwnerDining() {
      this.$router.push('/views/scm/ownerDining')
    },
    handleSuccess() {
      this._listReserveDinings()
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listReserveDinings()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listReserveDinings()
    }
  }
}
</script>

<style lang="scss" scoped>
.reserve-dining-manage-container {
  padding: 20px;

  .border-radius {
    border-radius: 4px;
  }

  .margin-top {
    margin-top: 20px;
  }

  .pagination {
    margin-top: 20px;
    text-align: right;
  }

  .list-group {
    list-style: none;
    padding: 0;
    margin: 0;

    .list-group-item {
      padding: 10px 15px;
      border: 1px solid #ddd;
      margin-bottom: -1px;
      cursor: pointer;

      &:hover {
        background-color: #f5f5f5;
      }
    }

    .vc-node-selected {
      background-color: #409EFF;
      color: white;
    }
  }
}
</style>