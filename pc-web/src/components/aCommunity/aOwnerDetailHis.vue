<template>
  <div class="owner-detail-his">
    <div class="margin-top">
      <el-table :data="aOwnerDetailHisInfo.owners" border style="width: 100%">
        <el-table-column prop="operate" :label="$t('aOwnerDetailHis.action')" align="center">
          <template #default="{ row }">
            {{ _getHisOwnerOperate(row) }}
          </template>
        </el-table-column>
        <el-table-column prop="userName" :label="$t('aOwnerDetailHis.operator')" align="center">
          <template #default="{ row }">
            {{ row.userName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('aOwnerDetailHis.operateTime')" align="center" />
        <el-table-column prop="name" :label="$t('aOwnerDetailHis.name')" align="center">
          <template #default="{ row }">
            {{ row.name }}({{ row.link }})
          </template>
        </el-table-column>
        <el-table-column prop="sex" :label="$t('aOwnerDetailHis.gender')" align="center">
          <template #default="{ row }">
            {{ row.sex == 0 ? $t('common.male') : $t('common.female') }}
          </template>
        </el-table-column>
        <el-table-column prop="idCard" :label="$t('aOwnerDetailHis.idCard')" align="center">
          <template #default="{ row }">
            {{ row.idCard || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="address" :label="$t('aOwnerDetailHis.address')" align="center">
          <template #default="{ row }">
            {{ row.address || '-' }}
          </template>
        </el-table-column>
        <el-table-column v-for="(item, index) in aOwnerDetailHisInfo.listColumns" :key="index" :label="item"
          align="center">
          <template #default="{ row }">
            {{ row.listValues[index] || '-' }}
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="4"></el-col>
        <el-col :span="20" class="text-right">
          <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
            layout="total, prev, pager, next" :total="total" />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { queryAdminHisOwner } from '@/api/aCommunity/aOwnerDetailHisApi'

export default {
  name: 'AOwnerDetailHis',
  data() {
    return {
      DEFAULT_PAGE: 1,
      DEFAULT_ROWS: 10,
      aOwnerDetailHisInfo: {
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
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(data) {
      this.aOwnerDetailHisInfo.ownerId = data.ownerId
      this.aOwnerDetailHisInfo.ownerName = data.ownerName
      this.aOwnerDetailHisInfo.ownerNameLike = data.ownerNameLike
      this.aOwnerDetailHisInfo.logStartTime = data.logStartTime
      this.aOwnerDetailHisInfo.logEndTime = data.logEndTime
      this.aOwnerDetailHisInfo.staffNameLike = data.staffNameLike
      this._getHisColumns(() => {
        this._loadAOwnerDetailHisData(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
      })
    },
    handleNotify() {
      this._loadAOwnerDetailHisData(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadAOwnerDetailHisData(val, this.DEFAULT_ROWS)
    },
    async _loadAOwnerDetailHisData(page, row) {
      const param = {
        memberId: this.aOwnerDetailHisInfo.ownerId,
        ownerNameLike: this.aOwnerDetailHisInfo.ownerNameLike,
        logStartTime: this.aOwnerDetailHisInfo.logStartTime,
        logEndTime: this.aOwnerDetailHisInfo.logEndTime,
        staffNameLike: this.aOwnerDetailHisInfo.staffNameLike,
        page,
        row
      }

      try {
        const response = await queryAdminHisOwner(param)
        this.aOwnerDetailHisInfo.owners = response.data
        this.dealOwnerAttr(response.data)
        this.total = response.total
        this.currentPage = page
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    dealOwnerAttr(owners) {
      if (!owners) return
      owners.forEach(item => {
        this._getHisColumnsValue(item)
      })
    },
    _getHisColumnsValue(owner) {
      owner.listValues = []
      if (!Object.prototype.hasOwnProperty.call(owner, 'ownerAttrDtos') || owner.ownerAttrDtos.length < 1) {
        this.aOwnerDetailHisInfo.listColumns.forEach(() => {
          owner.listValues.push('')
        })
        return
      }
      const ownerAttrDtos = owner.ownerAttrDtos
      this.aOwnerDetailHisInfo.listColumns.forEach(value => {
        let tmpValue = ''
        ownerAttrDtos.forEach(attrItem => {
          if (value === attrItem.specName) {
            tmpValue = attrItem.valueName
          }
        })
        owner.listValues.push(tmpValue)
      })
    },
    async _getHisColumns(callback) {
      this.aOwnerDetailHisInfo.listColumns = []
      try {
        const data = await getDict('building_owner_attr')
        this.aOwnerDetailHisInfo.listColumns = data.filter(item => item.listShow === 'Y').map(item => item.specName)
        callback()
      } catch (error) {
        console.error('获取属性规格失败:', error)
      }
    },
    _getHisOwnerOperate(owner) {
      let ownerCount = 0
      this.aOwnerDetailHisInfo.owners.forEach(item => {
        if (owner.bId === item.bId) {
          ownerCount += 1
        }
      })
      if (ownerCount <= 1) {
        if (owner.operate === 'ADD') return this.$t('aOwnerDetailHis.add')
        if (owner.operate === 'DEL') return this.$t('aOwnerDetailHis.delete')
        return '-'
      }
      if (owner.operate === 'ADD') return this.$t('aOwnerDetailHis.modifyNew')
      if (owner.operate === 'DEL') return this.$t('aOwnerDetailHis.modifyOld')
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