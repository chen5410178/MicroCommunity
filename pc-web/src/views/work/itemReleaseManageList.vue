<template>
  <div class="item-release-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('itemReleaseManage.search.title') }}</span>
        <el-button type="text" style="float: right; padding: 3px 0" @click="_moreCondition">
          {{ itemReleaseManageInfo.moreCondition ? $t('common.hide') : $t('common.more') }}
        </el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-select v-model="itemReleaseManageInfo.conditions.typeId"
            :placeholder="$t('itemReleaseManage.placeholder.selectReleaseType')" style="width: 100%">
            <el-option v-for="item in itemReleaseManageInfo.itemReleaseTypes" :key="item.typeId" :label="item.typeName"
              :value="item.typeId" :disabled="item.state !== 'C'" />
          </el-select>
        </el-col>
        <el-col :span="8">
          <el-input v-model="itemReleaseManageInfo.conditions.applyCompany"
            :placeholder="$t('itemReleaseManage.placeholder.selectApplyCompany')" />
        </el-col>
        <el-col :span="6">
          <el-input v-model="itemReleaseManageInfo.conditions.applyPerson"
            :placeholder="$t('itemReleaseManage.placeholder.selectApplyPerson')" />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="_queryItemReleaseMethod">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="_resetItemReleaseMethod">
            <i class="el-icon-refresh"></i>
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>

      <el-row v-show="itemReleaseManageInfo.moreCondition" :gutter="20" style="margin-top: 20px">
        <el-col :span="6">
          <el-input v-model="itemReleaseManageInfo.conditions.idCard"
            :placeholder="$t('itemReleaseManage.placeholder.selectIdCard')" />
        </el-col>
        <el-col :span="8">
          <el-input v-model="itemReleaseManageInfo.conditions.applyTel"
            :placeholder="$t('itemReleaseManage.placeholder.selectPhone')" />
        </el-col>
        <el-col :span="6">
          <el-select v-model="itemReleaseManageInfo.conditions.state"
            :placeholder="$t('itemReleaseManage.placeholder.selectStatus')" style="width: 100%">
            <el-option :label="$t('itemReleaseManage.status.waiting')" value="W" />
            <el-option :label="$t('itemReleaseManage.status.processing')" value="D" />
            <el-option :label="$t('itemReleaseManage.status.completed')" value="C" />
            <el-option :label="$t('itemReleaseManage.status.failed')" value="F" />
          </el-select>
        </el-col>
      </el-row>
    </el-card>

    <!-- 物品放行列表 -->
    <el-card class="list-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('itemReleaseManage.list.title') }}</span>
        <el-button type="primary" size="small" @click="_openAddItemReleaseModal">
          <i class="el-icon-plus"></i>
          {{ $t('itemReleaseManage.button.applyRelease') }}
        </el-button>
      </div>

      <el-table :data="itemReleaseManageInfo.itemReleases" border style="width: 100%" v-loading="loading">
        <el-table-column prop="irId" :label="$t('itemReleaseManage.table.orderNo')" align="center" />
        <el-table-column prop="typeName" :label="$t('itemReleaseManage.table.releaseType')" align="center" />
        <el-table-column prop="applyCompany" :label="$t('itemReleaseManage.table.applyCompany')" align="center" />
        <el-table-column prop="applyPerson" :label="$t('itemReleaseManage.table.applyPerson')" align="center" />
        <el-table-column prop="idCard" :label="$t('itemReleaseManage.table.idCard')" align="center" />
        <el-table-column prop="applyTel" :label="$t('itemReleaseManage.table.phone')" align="center" />
        <el-table-column prop="passTime" :label="$t('itemReleaseManage.table.passTime')" align="center" />
        <el-table-column :label="$t('itemReleaseManage.table.items')" align="center">
          <template slot-scope="scope">
            {{ scope.row.amount }}(
            <el-link type="primary" @click="viewResName(scope.row)">
              {{ $t('itemReleaseManage.button.viewItems') }}
            </el-link>)
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('itemReleaseManage.table.status')" align="center" />
        <el-table-column prop="carNum" :label="$t('itemReleaseManage.table.carNumber')" align="center">
          <template slot-scope="scope">
            {{ scope.row.carNum || $t('common.none') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="240">
          <template slot-scope="scope">
            <el-button size="mini" @click="_openDetail(scope.row)">
              {{ $t('common.detail') }}
            </el-button>
            <el-button size="mini" type="primary" v-if="scope.row.state != 'C'" @click="_openEditItemReleaseModel(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="_openDeleteItemReleaseModel(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" style="margin-top: 20px" />
    </el-card>

    <!-- 组件 -->
    <edit-item-release ref="editItemRelease" @success="handleSuccess" />
    <delete-item-release ref="deleteItemRelease" @success="handleSuccess" />
    <view-item-release-res ref="viewItemReleaseRes" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listItemRelease, listItemReleaseType } from '@/api/work/itemReleaseManageApi'
import DeleteItemRelease from '@/components/work/deleteItemRelease'
import ViewItemReleaseRes from '@/components/work/viewItemReleaseRes'

export default {
  name: 'ItemReleaseManageList',
  components: {
    DeleteItemRelease,
    ViewItemReleaseRes
  },
  data() {
    return {
      loading: false,
      communityId: '',
      itemReleaseManageInfo: {
        itemReleases: [],
        total: 0,
        records: 1,
        moreCondition: false,
        irId: '',
        itemReleaseTypes: [],
        conditions: {
          typeId: '',
          applyCompany: '',
          applyPerson: '',
          idCard: '',
          applyTel: '',
          state: '',
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
    this.communityId = getCommunityId()
    this.itemReleaseManageInfo.conditions.communityId = this.communityId
    this._listItemReleases(this.page.current, this.page.size)
    this._listItemReleaseTypes()
  },
  methods: {
    async _listItemReleases(page, rows) {
      try {
        this.loading = true
        const params = {
          page,
          row: rows,
          ...this.itemReleaseManageInfo.conditions
        }
        const { data, total } = await listItemRelease(params)
        this.itemReleaseManageInfo.itemReleases = data
        this.page.total = total
      } catch (error) {
        console.error('获取物品放行列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    async _listItemReleaseTypes() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const { data } = await listItemReleaseType(params)
        this.itemReleaseManageInfo.itemReleaseTypes = data
      } catch (error) {
        console.error('获取放行类型失败:', error)
      }
    },
    _openAddItemReleaseModal() {
      this.$router.push('/views/work/addItemReleaseView')
    },
    _openEditItemReleaseModel(item) {
      this.$router.push(`/views/work/editItemReleaseView?irId=${item.irId}&flowId=${item.flowId}`)
    },
    _openDeleteItemReleaseModel(item) {
      this.$refs.deleteItemRelease.open(item)
    },
    _queryItemReleaseMethod() {
      this.page.current = 1
      this._listItemReleases(this.page.current, this.page.size)
    },
    _resetItemReleaseMethod() {
      this.itemReleaseManageInfo.conditions = {
        typeId: '',
        applyCompany: '',
        applyPerson: '',
        idCard: '',
        applyTel: '',
        state: '',
        communityId: this.communityId
      }
      this.page.current = 1
      this._listItemReleases(this.page.current, this.page.size)
    },
    _moreCondition() {
      this.itemReleaseManageInfo.moreCondition = !this.itemReleaseManageInfo.moreCondition
    },
    viewResName(item) {
      this.$refs.viewItemReleaseRes.open(item)
    },
    _openDetail(item) {
      this.$router.push(`/views/work/itemReleaseDetail?irId=${item.irId}&flowId=${item.flowId}`)
    },
    handleSuccess() {
      this._listItemReleases(this.page.current, this.page.size)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listItemReleases(this.page.current, val)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listItemReleases(val, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.item-release-manage-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .list-card {
    margin-bottom: 20px;
  }

  .el-select {
    width: 100%;
  }
}
</style>