<template>
  <div>
    <el-row class="margin-top">
      <el-col :span="4">
        <el-select v-model="aFeeDetailFeeObjInfo.payerObjType" class="w-100" :placeholder="$t('aFeeDetailFeeObj.selectFeeObj')">
          <el-option :label="$t('aFeeDetailFeeObj.house')" value="3333"></el-option>
          <el-option :label="$t('aFeeDetailFeeObj.car')" value="6666"></el-option>
          <el-option :label="$t('aFeeDetailFeeObj.contract')" value="7777"></el-option>
        </el-select>
      </el-col>
      <el-col :span="4" class="pl-10">
        <el-button type="primary" size="small" @click="_qureyAFeeDetailFeeObj">
          {{$t('aFeeDetailFeeObj.query')}}
        </el-button>
      </el-col>
    </el-row>

    <div class="margin-top">
      <el-table :data="aFeeDetailFeeObjInfo.feeObjs" border style="width: 100%">
        <el-table-column prop="feeId" :label="$t('aFeeDetailFeeObj.feeId')" align="center"></el-table-column>
        <el-table-column :label="$t('aFeeDetailFeeObj.objType')" align="center">
          <template #default="{row}">
            <span v-if="row.payerObjType === '3333'">{{$t('aFeeDetailFeeObj.house')}}</span>
            <span v-else-if="row.payerObjType === '6666'">{{$t('aFeeDetailFeeObj.car')}}</span>
            <span v-else>{{$t('aFeeDetailFeeObj.contract')}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payerObjName" :label="$t('aFeeDetailFeeObj.objName')" align="center"></el-table-column>
        <el-table-column prop="ownerName" :label="$t('aFeeDetailFeeObj.ownerName')" align="center"></el-table-column>
        <el-table-column prop="ownerTel" :label="$t('aFeeDetailFeeObj.phone')" align="center"></el-table-column>
        <el-table-column prop="createTime" :label="$t('aFeeDetailFeeObj.createTime')" align="center"></el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="12"></el-col>
        <el-col :span="12">
          <el-pagination
            @current-change="handlePageChange"
            :current-page="currentPage"
            :page-size="pageSize"
            layout="total, prev, pager, next, jumper"
            :total="total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { listAdminConfigFeeObjs } from '@/api/aCommunity/aFeeDetailFeeObjApi'

export default {
  name: 'AFeeDetailFeeObj',
  data() {
    return {
      aFeeDetailFeeObjInfo: {
        feeObjs: [],
        configId: '',
        payerObjType: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(data) {
      this.aFeeDetailFeeObjInfo.configId = data.configId
      this._loadAFeeDetailFeeObjData()
    },
    handleSwitch(data) {
      this.aFeeDetailFeeObjInfo.configId = data.configId
      this._loadAFeeDetailFeeObjData()
    },
    handlePageChange(page) {
      this.currentPage = page
      this._loadAFeeDetailFeeObjData()
    },
    async _loadAFeeDetailFeeObjData() {
      try {
        const params = {
          configId: this.aFeeDetailFeeObjInfo.configId,
          payerObjType: this.aFeeDetailFeeObjInfo.payerObjType,
          page: this.currentPage,
          row: this.pageSize
        }
        
        const res = await listAdminConfigFeeObjs(params)
        this.aFeeDetailFeeObjInfo.feeObjs = res.data
        this.total = res.total
      } catch (error) {
        console.error('加载费用对象数据失败:', error)
      }
    },
    _qureyAFeeDetailFeeObj() {
      this.currentPage = 1
      this._loadAFeeDetailFeeObjData()
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}
.pl-10 {
  padding-left: 10px;
}
.w-100 {
  width: 100%;
}
</style>