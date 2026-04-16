<template>
  <div class="visit-manage-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card class="tree-card">
          <ul class="visit-type-list">
            <li
              v-for="(item, index) in visitTypeList"
              :key="index"
              @click="switchVisitType(item)"
              :class="{'selected': visitManageInfo.conditions.typeId === item.typeId}"
            >
              {{ item.name }}
            </li>
          </ul>
        </el-card>
      </el-col>
      <el-col :span="20">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('visitManage.search.title') }}</span>
          </div>
          <el-form :inline="true" :model="visitManageInfo.conditions" class="search-form text-left">
            <el-form-item>
              <el-select
                v-model="visitManageInfo.conditions.state"
                :placeholder="$t('visitManage.search.state')"
                style="width:100%"
              >
                <el-option
                  v-for="(item, index) in stateList"
                  :key="index"
                  :label="item.name"
                  :value="item.statusCd"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="visitManageInfo.conditions.nameLike"
                :placeholder="$t('visitManage.search.nameLike')"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-select
                v-model="visitManageInfo.conditions.visitGender"
                :placeholder="$t('visitManage.search.gender')"
                style="width:100%"
              >
                <el-option :label="$t('visitManage.gender.male')" value="0"></el-option>
                <el-option :label="$t('visitManage.gender.female')" value="1"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="visitManageInfo.conditions.phoneNumberLike"
                :placeholder="$t('visitManage.search.phoneNumber')"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="visitManageInfo.conditions.roomNameLike"
                :placeholder="$t('visitManage.search.roomName')"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="_queryVisitMethod">{{ $t('common.search') }}</el-button>
              <el-button @click="_resetVisitMethod">{{ $t('common.reset') }}</el-button>
            </el-form-item>
          </el-form>
          <el-form :inline="true" :model="visitManageInfo.conditions" class="search-form text-left">
            <el-form-item>
              <el-input
                v-model="visitManageInfo.conditions.ownerNameLike"
                :placeholder="$t('visitManage.search.ownerName')"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-date-picker
                v-model="visitManageInfo.conditions.queryStartTime"
                type="datetime" value-format="yyyy-MM-dd HH:mm:ss"                :placeholder="$t('visitManage.search.startTime')" 
              ></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-date-picker
                v-model="visitManageInfo.conditions.queryEndTime"
                type="datetime" value-format="yyyy-MM-dd HH:mm:ss"                :placeholder="$t('visitManage.search.endTime')" 
              ></el-date-picker>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="mt-20">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('visitManage.list.title') }}</span>
            <el-button type="primary" size="small" style="float: right" @click="_toIotVisit">
              {{ $t('visitManage.manage') }}
            </el-button>
          </div>
          <el-table :data="visitManageInfo.visits" border style="width: 100%">
            <el-table-column prop="visitId" :label="$t('visitManage.table.visitId')" align="center"></el-table-column>
            <el-table-column prop="name" :label="$t('visitManage.table.name')" align="center"></el-table-column>
            <el-table-column :label="$t('visitManage.table.gender')" align="center">
              <template slot-scope="scope">
                {{ scope.row.visitGender === '0' ? $t('visitManage.gender.male') : $t('visitManage.gender.female') }}
              </template>
            </el-table-column>
            <el-table-column prop="phoneNumber" :label="$t('visitManage.table.phoneNumber')" align="center"></el-table-column>
            <el-table-column prop="roomName" :label="$t('visitManage.table.roomName')" align="center"></el-table-column>
            <el-table-column prop="ownerName" :label="$t('visitManage.table.ownerName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.ownerName || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="carNum" :label="$t('visitManage.table.carNum')" align="center">
              <template slot-scope="scope">
                {{ scope.row.carNum || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="visitTypeName" :label="$t('visitManage.table.visitType')" align="center"></el-table-column>
            <el-table-column prop="visitCase" :label="$t('visitManage.table.visitCase')" align="center">
              <template slot-scope="scope">
                {{ scope.row.visitCase || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="visitTime" :label="$t('visitManage.table.visitTime')" align="center">
              <template slot-scope="scope">
                {{ scope.row.visitTime || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="departureTime" :label="$t('visitManage.table.departureTime')" align="center">
              <template slot-scope="scope">
                {{ scope.row.departureTime || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="stateName" :label="$t('visitManage.table.state')" align="center"></el-table-column>
            <el-table-column prop="msg" :label="$t('visitManage.table.msg')" align="center">
              <template slot-scope="scope">
                {{ scope.row.msg || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('visitManage.table.createTime')" align="center"></el-table-column>
            <el-table-column :label="$t('common.operation')" align="center" width="120">
              <template slot-scope="scope">
                <el-button size="mini" @click="_toVisitDetail(scope.row)">{{ $t('common.detail') }}</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="page.current"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="page.size"
            layout="total, sizes, prev, pager, next, jumper"
            :total="visitManageInfo.total"
            class="pagination"
          ></el-pagination>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listVisits, listVisitTypes } from '@/api/oa/visitManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { jumpToIot } from '@/api/user/menuApi'

export default {
  name: 'VisitManageList',
  data() {
    return {
      visitManageInfo: {
        visits: [],
        total: 0,
        conditions: {
          visitId: '',
          nameLike: '',
          visitGender: '',
          phoneNumberLike: '',
          roomNameLike: '',
          ownerNameLike: '',
          communityId: '',
          typeId: '',
          state: '',
          queryStartTime: '',
          queryEndTime: '',
          page: 1,
          row: 10
        }
      },
      visitTypeList: [],
      stateList: [],
      page: {
        current: 1,
        size: 10
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.initData()
  },
  methods: {
    async initData() {
      try {
        await this.listVisitTypes()
        await this.getDictData()
        this._listVisits(1, 10)
      } catch (error) {
        console.error('初始化数据失败:', error)
      }
    },
    async getDictData() {
      try {
        const data = await getDict('visit', 'state')
        this.stateList = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async _listVisits(page, rows) {
      try {
        this.visitManageInfo.conditions.page = page
        this.visitManageInfo.conditions.row = rows
        this.visitManageInfo.conditions.communityId = this.communityId
        const { data, total } = await listVisits(this.visitManageInfo.conditions)
        this.visitManageInfo.visits = data
        this.visitManageInfo.total = total
        this.page.current = page
        this.page.size = rows
      } catch (error) {
        console.error('获取访客列表失败:', error)
      }
    },
    async listVisitTypes() {
      try {
        const { data } = await listVisitTypes({
          page: -1,
          row: 100,
          communityId: this.communityId
        })
        this.visitTypeList = [{ name: this.$t('common.all'), typeId: '' }, ...data]
      } catch (error) {
        console.error('获取访客类型失败:', error)
      }
    },
    _queryVisitMethod() {
      this._listVisits(1, this.page.size)
    },
    _resetVisitMethod() {
      this.visitManageInfo.conditions = {
        visitId: '',
        nameLike: '',
        visitGender: '',
        phoneNumberLike: '',
        roomNameLike: '',
        ownerNameLike: '',
        communityId: this.communityId,
        typeId: '',
        state: '',
        queryStartTime: '',
        queryEndTime: '',
        page: 1,
        row: this.page.size
      }
      this._listVisits(1, this.page.size)
    },
    switchVisitType(item) {
      this.visitManageInfo.conditions.typeId = item.typeId
      this._listVisits(1, this.page.size)
    },
    _toIotVisit() {
      //this.$router.push('/pages/accessControl/visitManage')
      jumpToIot('/#/pages/accessControl/visitManage')
    },
    _toVisitDetail(visit) {
      jumpToIot(`#/pages/accessControl/visitDetail?visitId=${visit.visitId}&phoneNumber=${visit.phoneNumber}`)
    },
    handleSizeChange(val) {
      this._listVisits(1, val)
    },
    handleCurrentChange(val) {
      this._listVisits(val, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.visit-manage-container {
  padding: 20px;

  .tree-card {
    height: 100%;

    .visit-type-list {
      list-style: none;
      padding: 0;
      margin: 0;

      li {
        padding: 10px;
        cursor: pointer;
        text-align: center;
        transition: all 0.3s;

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

  .search-form {
    margin-bottom: 0;

    .el-form-item {
      margin-bottom: 10px;
      margin-right: 10px;
    }
  }

  .mt-20 {
    margin-top: 20px;
  }

  .pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>