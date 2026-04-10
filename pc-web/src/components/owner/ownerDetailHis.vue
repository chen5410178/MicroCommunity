<template>
  <div class="owner-detail-his">
    <el-row class="margin-top">
      <el-table :data="ownerDetailHisInfo.owners" border style="width: 100%" class="footable">
        <el-table-column prop="operate" :label="$t('ownerDetailHis.action')" align="center" >
          <template slot-scope="scope">
            {{ _getHisOwnerOperate(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column prop="userName" :label="$t('ownerDetailHis.operator')" align="center" >
          <template slot-scope="scope">
            {{ scope.row.userName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('ownerDetailHis.operateTime')" align="center"
          width="180"></el-table-column>
        <el-table-column prop="name" :label="$t('ownerDetailHis.name')" align="center" >
          <template slot-scope="scope">
            {{ scope.row.name }}({{ scope.row.link }})
          </template>
        </el-table-column>
        <el-table-column prop="sex" :label="$t('ownerDetailHis.gender')" align="center" >
          <template slot-scope="scope">
            {{ scope.row.sex == 0 ? $t('ownerDetailHis.male') : $t('ownerDetailHis.female') }}
          </template>
        </el-table-column>
        <el-table-column prop="idCard" :label="$t('ownerDetailHis.idCard')" align="center" >
          <template slot-scope="scope">
            {{ scope.row.idCard || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="address" :label="$t('ownerDetailHis.address')" align="center" >
          <template slot-scope="scope">
            {{ scope.row.address || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-for="(item, index) in ownerDetailHisInfo.listColumns" :key="index" :label="item"
          align="center" width="150">
          <template slot-scope="scope">
            {{ scope.row.listValues[index] || '-' }}
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="24" class="text-right">
          <el-pagination @current-change="handleCurrentChange" :current-page="pagination.currentPage"
            :page-size="pagination.pageSize" layout="total, prev, pager, next, jumper"
            :total="pagination.total"></el-pagination>
        </el-col>
      </el-row>
    </el-row>
  </div>
</template>

<script>
import { queryHisOwner } from '@/api/owner/ownerDetailHisApi'
import { getCommunityId } from '@/api/community/communityApi'
import {getAttrSpecList} from '@/api/dev/attrSpecApi'
export default {
  name: 'OwnerDetailHis',
  data() {
    return {
      ownerDetailHisInfo: {
        owners: [],
        ownerId: '',
        ownerName: '',
        carNum: '',
        listColumns: [],
        logStartTime: '',
        logEndTime: '',
        ownerNameLike: '',
        staffNameLike: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    open(ownerId, ownerName, ownerNameLike, logStartTime, logEndTime, staffNameLike) {
      this.ownerDetailHisInfo.ownerId = ownerId
      this.ownerDetailHisInfo.ownerName = ownerName
      this.ownerDetailHisInfo.logStartTime = logStartTime
      this.ownerDetailHisInfo.logEndTime = logEndTime
      this.ownerDetailHisInfo.staffNameLike = staffNameLike
      this._getColumns(() => {
        this._loadOwnerDetailHisData(1, this.pagination.pageSize)
      })
    },
    _loadOwnerDetailHisData(page, row) {
      const params = {
        communityId: getCommunityId(),
        memberId: this.ownerDetailHisInfo.ownerId,
        ownerNameLike: this.ownerDetailHisInfo.ownerNameLike,
        logStartTime: this.ownerDetailHisInfo.logStartTime,
        logEndTime: this.ownerDetailHisInfo.logEndTime,
        staffNameLike: this.ownerDetailHisInfo.staffNameLike,
        page: page,
        row: row
      }

      queryHisOwner(params)
        .then(response => {
          const data = response.data
          this.ownerDetailHisInfo.owners = data
          this.dealOwnerAttr(data)
          this.pagination.total = response.records
          this.pagination.currentPage = page
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    },
    handleCurrentChange(currentPage) {
      this._loadOwnerDetailHisData(currentPage, this.pagination.pageSize)
    },
    dealOwnerAttr(owners) {
      if (!owners) {
        return
      }
      owners.forEach(item => {
        this._getColumnsValue(item)
      })
    },
    _getColumnsValue(owner) {
      owner.listValues = []
      if (!Object.prototype.hasOwnProperty.call(owner, 'ownerAttrDtos') || owner.ownerAttrDtos.length < 1) {
        this.ownerDetailHisInfo.listColumns.forEach(() => {
          owner.listValues.push('')
        })
        return
      }
      const ownerAttrDtos = owner.ownerAttrDtos
      this.ownerDetailHisInfo.listColumns.forEach(value => {
        let tmpValue = ''
        ownerAttrDtos.forEach(attrItem => {
          if (value === attrItem.specName) {
            tmpValue = attrItem.valueName
          }
        })
        owner.listValues.push(tmpValue)
      })
    },
    _getColumns(callback) {
      this.ownerDetailHisInfo.listColumns = []
      getAttrSpecList({specCd:'building_owner_attr',page:1,row:10}).then(data => {
        this.ownerDetailHisInfo.listColumns = []
        data.data.forEach(item => {
          if (item.listShow === 'Y') {
            this.ownerDetailHisInfo.listColumns.push(item.specName)
          }
        })
        callback()
      })
    },
    _getHisOwnerOperate(owner) {
      let ownerCount = 0
      this.ownerDetailHisInfo.owners.forEach(item => {
        if (owner.bId === item.bId) {
          ownerCount += 1
        }
      })
      if (ownerCount <= 1) {
        if (owner.operate === 'ADD') {
          return this.$t('ownerDetailHis.add')
        }
        if (owner.operate === 'DEL') {
          return this.$t('ownerDetailHis.delete')
        }
        return '-'
      }
      if (owner.operate === 'ADD') {
        return this.$t('ownerDetailHis.modifyNew')
      }
      if (owner.operate === 'DEL') {
        return this.$t('ownerDetailHis.modifyOld')
      }
      return '-'
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}

.text-right {
  text-align: right;
}
</style>