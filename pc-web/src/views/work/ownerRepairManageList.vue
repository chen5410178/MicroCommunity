<template>
  <div class="animated fadeInRight">
    <el-row :gutter="20">
      <el-col :span="3" class="left-panel">
        <el-card class="parking-area-card">
          <div class="parking-area-list-wrapper">
            <ul class="parking-area-list">
              <li class="parking-area-item" :class="{ 'selected': selectedRepairType === '' }"
                @click="switchRepairType('')">
                {{ $t('ownerRepairManage.all') }}
              </li>
              <li v-for="(item, index) in ownerRepairManageInfo.repairTypes" :key="index"
                class="parking-area-item"
                :class="{ 'selected': selectedRepairType === item.repairType }"
                @click="switchRepairType(item.repairType)">
                {{ item.repairTypeName }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>

      <el-col :span="21" class="right-panel">
        <el-card shadow="hover">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('ownerRepairManage.queryCondition') }}</span>
          </div>
          <div class="search-content">
            <el-row :gutter="20">
              <el-col :span="4">
                <el-input v-model.trim="ownerRepairManageInfo.conditions.repairId"
                  :placeholder="$t('ownerRepairManage.placeholder.repairId')" clearable />
              </el-col>
              <el-col :span="4">
                <el-input v-model.trim="ownerRepairManageInfo.conditions.repairName"
                  :placeholder="$t('ownerRepairManage.placeholder.repairName')" clearable />
              </el-col>
              <el-col :span="4">
                <el-input v-model.trim="ownerRepairManageInfo.conditions.tel"
                  :placeholder="$t('ownerRepairManage.placeholder.tel')" clearable />
              </el-col>
              <el-col :span="4">
                <el-select v-model="ownerRepairManageInfo.conditions.state"
                  :placeholder="$t('ownerRepairManage.placeholder.state')" style="width:100%" clearable>
                  <el-option v-for="(item, index) in ownerRepairManageInfo.states" :key="index" :label="item.name"
                    :value="item.statusCd" />
                </el-select>
              </el-col>
      
              <el-col :span="4">
                <el-button type="primary" icon="el-icon-search" @click="_queryOwnerRepairMethod">{{ $t('common.search')
                }}</el-button>
                <el-button type="info" icon="el-icon-refresh" @click="_resetOwnerRepairMethod">{{ $t('common.reset')
                }}</el-button>
              </el-col>
            </el-row>
          </div>
        </el-card>

        <el-card shadow="hover" class="mt-20">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('ownerRepairManage.phoneRepair') }}</span>
            <el-button v-if="ownerRepairManageInfo.conditions.roomId != ''" size="small" type="primary" icon="el-icon-plus"
              style="float:right" @click="_openAddOwnerRepairModal">{{ $t('ownerRepairManage.register') }}</el-button>
          </div>
          <el-table :data="ownerRepairManageInfo.ownerRepairs" border stripe style="width: 100%">
            <el-table-column prop="repairId" :label="$t('ownerRepairManage.table.repairId')" align="center" />
            <el-table-column prop="repairObjName" :label="$t('ownerRepairManage.table.location')" align="center" />
            <el-table-column prop="repairTypeName" :label="$t('ownerRepairManage.table.repairType')" align="center" />
            <el-table-column prop="repairName" :label="$t('ownerRepairManage.table.repairName')" align="center" />
            <el-table-column prop="tel" :label="$t('ownerRepairManage.table.tel')" align="center" />
            <el-table-column prop="appointmentTime" :label="$t('ownerRepairManage.table.appointmentTime')" align="center" />
            <el-table-column prop="timeout" :label="$t('ownerRepairManage.table.timeout')" align="center" />
            <el-table-column prop="createTime" :label="$t('ownerRepairManage.table.createTime')" align="center" />
            <el-table-column :label="$t('ownerRepairManage.table.state')" align="center">
              <template slot-scope="scope">
                <span
                  v-if="scope.row.state == '1800' && (scope.row.returnVisitFlag == '001' || scope.row.returnVisitFlag == '002')">
                  {{ scope.row.stateName }}{{ $t('ownerRepairManage.table.scheduledTask') }}
                </span>
                <span v-else>{{ scope.row.stateName }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('common.operation')" align="center" fixed="right" width="200">
              <template slot-scope="scope">
                <el-button
                  v-if="hasPrivilege('502021012067300023') && (scope.row.state == '1000' || scope.row.state == '1200')"
                  size="mini" type="primary" @click="_openEditOwnerRepairModel(scope.row)">{{ $t('common.edit')
                  }}</el-button>
                <el-button
                  v-if="hasPrivilege('502021012051410024') && (scope.row.state == '1000' || scope.row.state == '1200')"
                  size="mini" type="danger" @click="_openDeleteOwnerRepairModel(scope.row)">{{ $t('common.delete')
                  }}</el-button>
              </template>
            </el-table-column>
          </el-table>

          <div style="margin-top: 20px">
            <el-row>
              <el-col :span="12" class="text-left table-desc">
                <div>{{ $t('ownerRepairManage.tip1') }}</div>
                <div>{{ $t('ownerRepairManage.tip2') }}</div>
              </el-col>
              <el-col :span="12">
                <el-pagination :current-page="ownerRepairManageInfo.page" :page-sizes="[10, 20, 30, 50]"
                  :page-size="ownerRepairManageInfo.rows" layout="total, sizes, prev, pager, next, jumper"
                  :total="ownerRepairManageInfo.total" @size-change="handleSizeChange"
                  @current-change="handleCurrentChange" style="text-align:right" />
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <add-owner-repair ref="addOwnerRepair" @success="_queryOwnerRepairMethod" />
    <edit-owner-repair ref="editOwnerRepair" @success="_queryOwnerRepairMethod" />
    <delete-owner-repair ref="deleteOwnerRepair" @success="_queryOwnerRepairMethod" />
  </div>
</template>

<script>
import * as ownerRepairManageApi from '@/api/work/ownerRepairManageApi'
import AddOwnerRepair from '@/components/work/addOwnerRepair'
import EditOwnerRepair from '@/components/work/editOwnerRepair'
import DeleteOwnerRepair from '@/components/work/deleteOwnerRepair'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'OwnerRepairManageList',
  components: {
    AddOwnerRepair,
    EditOwnerRepair,
    DeleteOwnerRepair
  },
  data() {
    return {
      selectedRepairType: '',
      ownerRepairManageInfo: {
        ownerRepairs: [],
        total: 0,
        page: 1,
        rows: 10,
        moreCondition: false,
        repairTypes: [],
        states: [],
        conditions: {
          repairId: '',
          repairName: '',
          tel: '',
          repairType: '',
          state: '',
          reqSource: 'pc_mobile'
        }
      }
    }
  },
  created() {
    this._listOwnerRepairs(1, this.ownerRepairManageInfo.rows)
    this._listRepairTypes(1, 50)
    this._loadStates()
  },
  methods: {
    _loadStates() {
      getDict('r_repair_pool', 'state').then(data => {
        this.ownerRepairManageInfo.states = data
      })
    },
    async _listRepairTypes(page, rows) {
      const param = {
        page: page,
        row: rows,
        communityId: this.getCommunityId()
      }
      const { data } = await ownerRepairManageApi.listRepairSettings(param)
      this.ownerRepairManageInfo.repairTypes = data

    },
    switchRepairType(repairType) {
      this.selectedRepairType = repairType
      this.ownerRepairManageInfo.conditions.repairType = repairType
      this._listOwnerRepairs(1, this.ownerRepairManageInfo.rows)
    },
    _listOwnerRepairs(page, rows) {
      this.ownerRepairManageInfo.conditions.page = page
      this.ownerRepairManageInfo.conditions.row = rows
      this.ownerRepairManageInfo.conditions.communityId = this.getCommunityId()
      ownerRepairManageApi.listOwnerRepairs(this.ownerRepairManageInfo.conditions).then(res => {
        if (res.code === 0) {
          this.ownerRepairManageInfo.ownerRepairs = res.data
          this.ownerRepairManageInfo.total = res.total
          this.ownerRepairManageInfo.page = page
          this.ownerRepairManageInfo.rows = rows
        }
      })
    },
    _resetListOwnerRepairs() {
      this.ownerRepairManageInfo.conditions.repairId = ''
      this.ownerRepairManageInfo.conditions.repairName = ''
      this.ownerRepairManageInfo.conditions.tel = ''
      this.ownerRepairManageInfo.conditions.state = ''
      this._listOwnerRepairs(1, this.ownerRepairManageInfo.rows)
    },
    _openAddOwnerRepairModal() {
      this.$refs.addOwnerRepair.open()
    },
    _openEditOwnerRepairModel(row) {
      this.$refs.editOwnerRepair.open(row)
    },
    _openDeleteOwnerRepairModel(row) {
      this.$refs.deleteOwnerRepair.open(row)
    },
    _queryOwnerRepairMethod() {
      this._listOwnerRepairs(1, this.ownerRepairManageInfo.rows)
    },
    _resetOwnerRepairMethod() {
      this._resetListOwnerRepairs()
    },
    handleSizeChange(val) {
      this.ownerRepairManageInfo.rows = val
      this._listOwnerRepairs(this.ownerRepairManageInfo.page, val)
    },
    handleCurrentChange(val) {
      this.ownerRepairManageInfo.page = val
      this._listOwnerRepairs(val, this.ownerRepairManageInfo.rows)
    }
  }
}
</script>

<style lang="scss" scoped>
.animated {
  padding: 20px;
}

.mt-20 {
  margin-top: 20px;
}

.right-panel {
  padding-left: 5px;
}

.left-panel {
  .parking-area-card {
    height: 700px;

    .parking-area-list-wrapper {
      height: 630px;
      overflow-y: auto;

      &::-webkit-scrollbar {
        width: 4px;
      }

      &::-webkit-scrollbar-thumb {
        background: #dcdfe6;
        border-radius: 2px;
      }

      &::-webkit-scrollbar-track {
        background: transparent;
      }

      .parking-area-list {
        list-style: none;
        padding: 0 5px;
        margin: 0;

        .parking-area-item {
          padding: 10px;
          text-align: center;
          cursor: pointer;
          border-radius: 4px;
          margin-bottom: 5px;
          transition: all 0.3s;
          font-size: 14px;
          color: #606266;

          &:hover {
            background-color: #f5f7fa;
          }

          &.selected {
            background-color: #409eff;
            color: white;
          }
        }
      }
    }
  }
}

.search-content {
  padding: 0px;
}
</style>