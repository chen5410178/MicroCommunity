<template>
  <div class="community-manage-container">
    <!-- 查询条件 -->
    <el-card>
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('communityManage.search.title') }}</span>
        <div style="float: right;">
        </div>
      </div>
      <el-row :gutter="20">
        <el-col :span="4">
          <el-input v-model="communityManageInfo.conditions.communityId"
            :placeholder="$t('communityManage.search.communityId')" clearable />
        </el-col>
        <el-col :span="4">
          <el-input v-model="communityManageInfo.conditions.name" :placeholder="$t('communityManage.search.name')"
            clearable />
        </el-col>
        <el-col :span="12">
          <area-select @selectArea="selectArea" />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="_queryCommunityMethod">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 小区信息 -->

    <el-card>
      <div slot="header" class="clearfix flex justify-between">
        <span>{{ $t('communityManage.list.title') }}</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="toDoc('/pages/hc/addCommunity_cn.html')">
            {{ $t('common.document') }}
          </el-button>
          <el-button type="primary" size="small" @click="_openAddCommunityModal">
            {{ $t('communityManage.list.addCommunity') }}
          </el-button>
        </div>
      </div>
      <el-table :data="communityManageInfo.communitys" border style="width: 100%">
        <el-table-column prop="communityId" :label="$t('communityManage.table.communityId')" align="center" />
        <el-table-column prop="name" :label="$t('communityManage.table.name')" align="center" />
        <el-table-column prop="storeName" :label="$t('communityManage.table.storeName')" align="center">
          <template #default="{ row }">
            {{ row.storeName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="nearbyLandmarks" :label="$t('communityManage.table.nearbyLandmarks')" align="center" />
        <el-table-column prop="cityName" :label="$t('communityManage.table.cityCode')" align="center" />
        <el-table-column prop="createTime" :label="$t('communityManage.table.createTime')" align="center" />
        <el-table-column v-for="(item, index) in communityManageInfo.listColumns" :key="index" :label="item.specName"
          align="center">
          <template #default="{ row }">
            {{ row.listValues[index] }}
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('communityManage.table.state')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="220">
          <template #default="{ row }">
            <el-button-group>
              <el-button size="mini" @click="_openDeleteCommunityModel(row)">
                {{ $t('common.delete') }}
              </el-button>
              <el-button size="mini" @click="_openEditCommunityModel(row)">
                {{ $t('common.edit') }}
              </el-button>
              <el-button size="mini" @click="_sendCommunityIot(row)">
                {{ $t('communityManage.table.syncIot') }}
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>
    <!-- 组件 -->
    <add-community ref="addCommunity" @listData="_queryCommunityMethod" />
    <edit-community ref="editCommunity" @listData="_queryCommunityMethod" />
    <delete-community ref="deleteCommunity" @listData="_queryCommunityMethod" />
    <community-data-to-iot ref="communityDataToIot" />
  </div>
</template>

<script>
import { listCommunitys } from '@/api/community/communityManageApi'
import AreaSelect from '@/components/community/areaSelect'
import AddCommunity from '@/components/community/addCommunity'
import EditCommunity from '@/components/community/editCommunity'
import DeleteCommunity from '@/components/community/deleteCommunity'
import CommunityDataToIot from '@/components/community/communityDataToIot'
import { getAttrSpecList } from '@/api/dev/attrSpecApi'

export default {
  name: 'CommunityManageList',
  components: {
    AreaSelect,
    AddCommunity,
    EditCommunity,
    DeleteCommunity,
    CommunityDataToIot,
  },
  data() {
    return {
      communityManageInfo: {
        communitys: [],
        storeTypeCd: '',
        conditions: {
          name: '',
          cityCode: '',
          communityId: ''
        },
        listColumns: []
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this._initMethod()
  },
  methods: {
    async _initMethod() {
      await this._getColumns()
      this._listCommunitys(this.pagination.current, this.pagination.size)
    },
    _listCommunitys(page, size) {
      const params = {
        ...this.communityManageInfo.conditions,
        page,
        row: size
      }
      listCommunitys(params).then(res => {
        this.communityManageInfo.total = res.total
        this.communityManageInfo.records = res.records
        this.communityManageInfo.communitys = res.communitys
        this.dealCommunityAttr(res.communitys)
        this.pagination.total = res.total
      }).catch(error => {
        console.error('请求失败处理', error)
      })
    },
    selectArea(area) {
      this.communityManageInfo.conditions.cityCode = area.selectArea
    },
    _openAddCommunityModal() {
      this.$refs.addCommunity.openModal()
    },
    _openEditCommunityModel(community) {
      this.$refs.editCommunity.openModal(community)
    },
    _openDeleteCommunityModel(community) {
      this.$refs.deleteCommunity.openModal(community)
    },
    _queryCommunityMethod() {
      this.pagination.current = 1
      this._listCommunitys(this.pagination.current, this.pagination.size)
    },
    _resetCommunityMethod() {
      this.communityManageInfo.conditions = {
        name: '',
        cityCode: '',
        communityId: ''
      }
      this._listCommunitys(this.pagination.current, this.pagination.size)
    },
    _sendCommunityIot(community) {
      this.$refs.communityDataToIot.openModal(community)
    },
    dealCommunityAttr(communitys) {
      communitys.forEach(item => {
        this._getColumnsValue(item)
      })
    },
    _getColumnsValue(community) {
      community.listValues = []
      if (!community.communityAttrDtos || community.communityAttrDtos.length < 1) {
        this.communityManageInfo.listColumns.forEach(() => {
          community.listValues.push('')
        })
        return
      }
      this.communityManageInfo.listColumns.forEach(column => {
        let tmpValue = ''
        community.communityAttrDtos.forEach(attrItem => {
          if (column.specCd === attrItem.specCd) {
            tmpValue = attrItem.value
          }
        })
        community.listValues.push(tmpValue)
      })
    },
    async _getColumns() {
      const { data } = await getAttrSpecList({
        page: 1,
        row: 100,
        tableName: 'building_community_attr'
      })
      this.communityManageInfo.listColumns = []
      data.forEach(item => {
        if (item.listShow === 'Y') {
          this.communityManageInfo.listColumns.push({
            specCd: item.specCd,
            specName: item.specName
          })
        }
      })
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this._listCommunitys(this.pagination.current, val)
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this._listCommunitys(val, this.pagination.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.community-manage-container {
  padding: 20px;

  .el-card {
    margin-bottom: 20px;
  }
}
</style>